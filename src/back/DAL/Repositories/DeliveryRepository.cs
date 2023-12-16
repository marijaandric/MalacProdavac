using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
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
                CreatedOn = DateTime.Now,
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
            Order order = await _context.Orders.FirstOrDefaultAsync(x => x.Id == req.OrderId);
            order.StatusId = (await _context.OrderStatuses.FirstOrDefaultAsync(x => x.Name == "Processing")).Id;
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
            return (await _context.DeliveryRequests.Where(x => x.RouteId == null && x.ChosenPersonId == deliveryPerson).Join(_context.Shop, dr => dr.ShopId, s => s.Id, (dr, s) => new { dr, s }).Join(_context.Orders, x => x.dr.OrderId, o => o.Id, (x, o) => new DeliveryRequestCard
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
            List<string> shippingAddresses = await _context.DeliveryRequests.Where(x => x.RouteId == routeId).Join(_context.Orders, dr => dr.OrderId, o => o.Id, (dr, o) => o).Select(x => x.ShippingAddress).ToListAsync();
            List<string> shops = await _context.DeliveryRequests.Where(x => x.RouteId == routeId).Join(_context.Orders, dr => dr.OrderId, o => o.Id, (dr, o) => o).Join(_context.Shop, o => o.ShopId, s => s.Id, (o, s) => s).Select(x => x.Address).ToListAsync();
            shippingAddresses.AddRange(shops);
            
            return shippingAddresses;
        }

        public async Task<List<DeliveryStop>> GetDeliveryStopDetails(int routeId)
        {
            DeliveryRoute route = await _context.DeliveryRoutes.FirstOrDefaultAsync(x => x.Id == routeId);

            List<DeliveryStop> stops = new List<DeliveryStop>
            {
                new DeliveryStop
                {
                    Address = route.StartLocation,
                    Latitude = (float)route.StartLatitude,
                    Longitude = (float)route.StartLongitude
                },
                new DeliveryStop
                {
                    Address = route.EndLocation,
                    Latitude = (float)route.EndLatitude,
                    Longitude = (float)route.EndLongitude
                }
            };

            var addresses = await _context.DeliveryRequests
            .Where(x => x.RouteId == routeId)
            .Join(_context.Orders, r => r.OrderId, o => o.Id, (r, o) => o)
            .Join(_context.OrderItems, o => o.Id, oi => oi.OrderId, (o, oi) => new { o, oi })
            .Join(_context.Products, order => order.oi.ProductId, p => p.Id, (order, p) => new { order.o, order.oi, p })
            .Join(_context.Metrics, order => order.p.MetricId, m => m.Id, (order, m) => new { order.o, order.oi, order.p, m })
            .GroupBy(x => new { x.o.ShippingAddress, x.o.Latitude, x.o.Longitude }).ToListAsync();
            stops.AddRange(addresses.Select(group => new DeliveryStop
            {
                Address = group.Key.ShippingAddress,
                Latitude = (float)group.Key.Latitude,
                Longitude = (float)group.Key.Longitude,
                Items = group.GroupBy(item => new { Name = item.p.Name, Metric = item.m.Name })
                     .Select(itemGroup => new DeliveryItem
                     {
                         Name = itemGroup.Key.Name,
                         Metric = itemGroup.Key.Metric,
                         Quantity = itemGroup.Sum(item => item.oi.Quantity)
                     })
                     .ToList()
            })
            .ToList());

            var shops = await _context.DeliveryRequests
                .Where(x => x.RouteId == routeId)
                .Join(_context.Orders, r => r.OrderId, o => o.Id, (r, o) => o)
                .Join(_context.OrderItems, o => o.Id, oi => oi.OrderId, (o, oi) => new { o, oi })
            .Join(_context.Products, order => order.oi.ProductId, p => p.Id, (order, p) => new { order.o, order.oi, p })
            .Join(_context.Metrics, order => order.p.MetricId, m => m.Id, (order, m) => new { order.o, order.oi, order.p, m })
            .Join(_context.Shop, order => order.o.ShopId, s => s.Id, (o, s) => new { o.o, o.oi, o.p, o.m, s }).ToListAsync();

            stops.AddRange(shops.GroupBy(x => new { x.s.Address, x.s.Latitude, x.s.Longitude, x.s.Name })        
           .Select(group => new DeliveryStop
            {
               Address = group.Key.Address,
               Latitude = (float)group.Key.Latitude,
               Longitude = (float)group.Key.Longitude,
               ShopName = group.Key.Name,
               Items = group.GroupBy(item => new { Name = item.p.Name, Metric = item.m.Name })
                     .Select(itemGroup => new DeliveryItem
                     {
                         Name = itemGroup.Key.Name,
                         Metric = itemGroup.Key.Metric,
                         Quantity = itemGroup.Sum(item => item.oi.Quantity)
                     })
                     .ToList()
           }).ToList());

            return stops;
        }

        public async Task<DeliveryRouteInfo> GetRouteDetails(int routeId)
        {
            DeliveryRoute route = await _context.DeliveryRoutes.FirstOrDefaultAsync(x => x.Id == routeId);         
            
            return new DeliveryRouteInfo
            {
                Locations = route.StartLocation
                .Substring(route.StartLocation.IndexOf(',') + 1)
                .Substring(0, route.StartLocation.Substring(route.StartLocation.IndexOf(',') + 1).IndexOf(','))
                .Trim() + " - " +
                route.EndLocation
                .Substring(route.EndLocation.IndexOf(',') + 1)
                .Substring(0, route.EndLocation.Substring(route.EndLocation.IndexOf(',') + 1).IndexOf(','))
                .Trim(),
                StartDate = route.StartDate.ToShortDateString(),
                StartTime = route.StartTime,
                Stops = await GetDeliveryStopDetails(routeId)
            };
        }

        public async Task<DeliveryRequestInfo> GetRequestDetails(int requestId)
        {
            var req = await _context.DeliveryRequests.FirstOrDefaultAsync(x => x.Id == requestId);
            int shopId = req.ShopId;
            int orderId = req.OrderId;

            var shop = await _context.Shop.FirstOrDefaultAsync(x => x.Id == shopId);
            var order = await _context.Orders.FirstOrDefaultAsync(x => x.Id == orderId);
            var items = await _context.OrderItems.Where(x => x.OrderId == orderId).Join(_context.Products, oi => oi.ProductId, p => p.Id, (oi, p) => new { oi, p }).Join(_context.Metrics, i => i.p.MetricId, m => m.Id, (i, m) => new {i, m}).Select(x => new DeliveryItem
            {
                Name = x.i.p.Name,
                Metric = x.m.Name,
                Quantity = x.i.oi.Quantity
            }).ToListAsync();

            return new DeliveryRequestInfo
            {
                Locations = shop.Address
                .Substring(shop.Address.IndexOf(',') + 1)
                .Substring(0, shop.Address.Substring(shop.Address.IndexOf(',') + 1).IndexOf(','))
                .Trim() + " - " +
                order.ShippingAddress
                .Substring(order.ShippingAddress.IndexOf(',') + 1)
                .Substring(0, order.ShippingAddress.Substring(order.ShippingAddress.IndexOf(',') + 1).IndexOf(','))
                .Trim(),
                Id = requestId,
                ShippingAddress = order.ShippingAddress,
                ShippingLatitude = (float)order.Latitude,
                ShippingLongitude = (float)order.Longitude,
                ShopAddress = shop.Address,
                ShopLatitude = (float)shop.Latitude,
                ShopLongitude = (float)shop.Longitude,
                Items = items
            };
        }

        public async Task<List<int>> GetRequestIdsByRoute(int routeId)
        {
            return await _context.DeliveryRequests.Where(x => x.RouteId == routeId).Select(x => x.Id).ToListAsync();
        }

        public async Task<bool> RemoveRequest(int requestId)
        {
            var req = await _context.DeliveryRequests.FirstOrDefaultAsync(x => x.Id == requestId);
            req.RouteId = null;
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> DeleteRoute(int routeId)
        {
            DeliveryRoute route = await GetRoute(routeId);
            _context.DeliveryRoutes.Remove(route);
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> EditRoute(DeliveryRoute newRoute)
        {
            DeliveryRoute existingRoute = await _context.DeliveryRoutes.FirstOrDefaultAsync(x => x.Id == newRoute.Id);
            existingRoute = newRoute;
            return await _context.SaveChangesAsync() > 0;
        }
    }
}
