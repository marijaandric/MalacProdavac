using back.BLL.Dtos;
using back.BLL.Services;
using back.DAL.Contexts;
using back.Models;

namespace back.DAL.Repositories
{
    public class DeliveryRepository : IDeliveryRepository
    {
        Context _context;

        public DeliveryRepository(Context context)
        {
            _context = context;
        }

        public async Task<bool> InsertDeliveryRequest(DeliveryRequestDto dto)
        {
            await _context.DeliveryRequests.AddAsync(new DeliveryRequest
            {
                OrderId = dto.OrderId,
                ShopId = dto.ShopId,
                CreatedOn = DateTime.Now
            });

            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> InsertDeliveryRoute(DeliveryRouteDto route)
        {
            (double, double) startCoords = await HelperService.GetCoordinates(route.StartLocation);
            (double, double) endCoords = await HelperService.GetCoordinates(route.EndLocation);

            await _context.DeliveryRoutes.AddAsync(new DeliveryRoute
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
                FixedCost = route.FixedCost
            });

            return await _context.SaveChangesAsync() > 0;
        }
    }
}
