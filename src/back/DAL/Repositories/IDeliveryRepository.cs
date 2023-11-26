using back.BLL.Dtos;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IDeliveryRepository
    {
        public Task<bool> InsertDeliveryRequest(DeliveryRequestDto req);
        public Task<bool> InsertDeliveryRoute(DeliveryRouteDto route);
        public Task<bool> AddToRoute(int requestId, int routeId);
        public Task<List<DeliveryRequestCard>> GetRequestsForShop(int userId);
        public Task<List<DeliveryRequestCard>> GetRequestsForDeliveryPerson(int deliveryPerson);
        public Task<List<DeliveryRoute>> GetRoutesForDeliveryPerson(int userId);
        public Task<DeliveryRequestCard> GetRequest(int requestId);
    }
}
