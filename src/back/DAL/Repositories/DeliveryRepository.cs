using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Services;
using back.DAL.Contexts;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class DeliveryRepository : IDeliveryRepository
    {
        Context _context;
        public DeliveryRepository(Context context)
        {
            _context = context;
        }

        public async Task<int> InsertDeliveryRequest(DeliveryRequestDto dto)
        {
            DeliveryRequest req = new DeliveryRequest
            {
                OrderId = dto.OrderId,
                ShopId = dto.ShopId,
                CreatedOn = DateTime.Now
            };

            await _context.DeliveryRequests.AddAsync(req);

            if (await _context.SaveChangesAsync() > 0) return req.Id;
            return -1;
        }

        public async Task<DeliveryRoute> InsertDeliveryRoute(DeliveryRouteDto route)
        {
            (double, double) startCoords = await HelperService.GetCoordinates(route.StartLocation);
            (double, double) endCoords = await HelperService.GetCoordinates(route.EndLocation);

            DeliveryRoute r = new DeliveryRoute
            {
                DeliveryPersonId = route.UserId,
                StartDate = route.StartDate,
                StartTime = TimeSpan.Parse(route.StartTime),
                StartLocation = route.StartLocation,
                EndLocation = route.EndLocation,
                StartLatitude = startCoords.Item1,
                StartLongitude = startCoords.Item2,
                EndLatitude = endCoords.Item1,
                EndLongitude = endCoords.Item2,
                FixedCost = (float)route.FixedCost
            };

            await _context.DeliveryRoutes.AddAsync(r);
            if (await _context.SaveChangesAsync() <= 0) return null;

            return r;
        }

        public async Task<bool> AddToRoute(int requestId, int routeId)
        {
            DeliveryRequest req = await _context.DeliveryRequests.FirstOrDefaultAsync(x => x.Id == requestId);
            req.RouteId = routeId;
            req.PickupDate = (await _context.DeliveryRoutes.FirstOrDefaultAsync(x => x.Id == routeId)).StartDate;

            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> DeclineRequest(int requestId)
        {
            DeliveryRequest req = await _context.DeliveryRequests.FirstOrDefaultAsync(x => x.Id == requestId);
            req.ChosenPersonId = null;

            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<List<DeliveryRequestCard>> GetRequestsForDeliveryPerson(int deliveryPerson)
        {
            return (await _context.DeliveryRequests.Where(x => x.RouteId == null && x.ChosenPersonId == deliveryPerson && !x.Accepted != true).Join(_context.Shop, dr => dr.ShopId, s => s.Id, (dr, s) => new { dr, s }).Join(_context.Orders, x => x.dr.OrderId, o => o.Id, (x, o) => new DeliveryRequestCard
            {
                Id = x.dr.Id,
                Locations = x.s.Address
                .Substring(x.s.Address.IndexOf(',') + 1)
                .Substring(0, x.s.Address.Substring(x.s.Address.IndexOf(',') + 1).IndexOf(','))
                .Trim() + " - " +
                o.ShippingAddress
                .Substring(o.ShippingAddress.IndexOf(',') + 1)
                .Substring(0, o.ShippingAddress.Substring(o.ShippingAddress.IndexOf(',') + 1).IndexOf(','))
                .Trim(),
                CreatedOn = x.dr.CreatedOn,
                StartAddress = x.s.Address,
                EndAddress = o.ShippingAddress
            }).ToListAsync());
        }

        public async Task<List<DeliveryRequestCard>> GetRequestsForShop(int userId)
        {
            int shopId = (await _context.Shop.FirstOrDefaultAsync(x => x.OwnerId == userId)).Id;

            return await _context.DeliveryRequests.Where(x => x.ShopId == shopId && x.RouteId == null && x.ChosenPersonId == null).Join(_context.Shop, dr => dr.ShopId, s => s.Id, (dr, s) => new { dr, s }).Join(_context.Orders, x => x.dr.OrderId, o => o.Id, (x, o) => new DeliveryRequestCard
            {
                Id = x.dr.Id,
                Locations = x.s.Address
                .Substring(x.s.Address.IndexOf(',') + 1)
                .Substring(0, x.s.Address.Substring(x.s.Address.IndexOf(',') + 1).IndexOf(','))
                .Trim() + " - " +
                o.ShippingAddress
                .Substring(o.ShippingAddress.IndexOf(',') + 1)
                .Substring(0, o.ShippingAddress.Substring(o.ShippingAddress.IndexOf(',') + 1).IndexOf(','))
                .Trim(),
                CreatedOn = x.dr.CreatedOn,
                StartAddress = x.s.Address,
                EndAddress = o.ShippingAddress
            }).ToListAsync();
        }

        public async Task<List<DeliveryRouteCard>> GetRoutesForDeliveryPerson(int userId)
        {
            return await _context.DeliveryRoutes.Where(x => x.DeliveryPersonId == userId).Select(x => new DeliveryRouteCard
            {
                EndAddress = x.EndLocation,
                StartAddress = x.StartLocation,
                Locations = x.StartLocation
                .Substring(x.StartLocation.IndexOf(',') + 1)
                .Substring(0, x.StartLocation.Substring(x.StartLocation.IndexOf(',') + 1).IndexOf(','))
                .Trim()
                + " - " +
                x.EndLocation
                .Substring(x.EndLocation.IndexOf(',') + 1)
                .Substring(0, x.EndLocation.Substring(x.EndLocation.IndexOf(',') + 1).IndexOf(','))
                .Trim(),
                Id = x.Id,
                StartTime = x.StartTime,
                Cost = x.FixedCost,
                CreatedOn = x.StartDate
            }).ToListAsync();
        }

        public async Task<DeliveryRequestCard> GetRequest(int requestId)
        {
            var dr = await _context.DeliveryRequests.FirstOrDefaultAsync(x => x.Id == requestId);
            string start = (await _context.Shop.FirstOrDefaultAsync(x => x.Id == dr.ShopId)).Address;
            string end = (await _context.Orders.FirstOrDefaultAsync(x => x.Id == dr.OrderId)).ShippingAddress;

            return new DeliveryRequestCard
            {
                Id = dr.Id,
                CreatedOn = dr.CreatedOn,
                Locations = start
                .Substring(start.IndexOf(',') + 1)
                .Substring(0, start.Substring(start.IndexOf(',') + 1).IndexOf(','))
                .Trim() + " - " +
                end
                .Substring(end.IndexOf(',') + 1)
                .Substring(0, end.Substring(end.IndexOf(',') + 1).IndexOf(','))
                .Trim(),
                StartAddress = start,
                EndAddress = end
            };
        }

        public async Task<DeliveryRoute> GetRoute(int routeId)
        {
            return await _context.DeliveryRoutes.FirstOrDefaultAsync(x => x.Id == routeId);
        }
        public async Task<DeliveryRequest> GetBaseRequest(int requestId)
        {
            return await _context.DeliveryRequests.FirstOrDefaultAsync(x => x.Id == requestId);
        }

        public async Task<int> GetCustomerIdForDelivery(int requestId)
        {
            int orderId  = (await _context.DeliveryRequests.FirstOrDefaultAsync(x => x.Id == requestId)).OrderId;
            return (await _context.Orders.FirstOrDefaultAsync(x => x.Id == orderId)).UserId;
        }

        public async Task<bool> ChooseDeliveryPerson(int requestId, int chosenPersonId)
        {
            DeliveryRequest req = await _context.DeliveryRequests.FirstOrDefaultAsync(x => x.Id == requestId);
            req.ChosenPersonId = chosenPersonId;
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<List<string>> GetRequestCoordinates(int routeId)
        {
            return await _context.DeliveryRequests.Where(x => x.RouteId == routeId).Join(_context.Orders, dr => dr.OrderId, o => o.Id, (dr, o) => o).Select(x => x.ShippingAddress).ToListAsync();
        }
    }
}
