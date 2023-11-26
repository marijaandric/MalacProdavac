using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface IDeliveryService
    {
        public Task<bool> InsertDeliveryRequest(DeliveryRequestDto req);
        public Task<bool> InsertDeliveryRoute(DeliveryRouteDto route);
        public Task<bool> AddToRoute(int requestId, int routeId);
        public Task<List<DeliveryRequestCard>> GetRequestsForDeliveryPerson(int deliveryPerson);
        public Task<List<DeliveryRequestCard>> GetRequestsForShop(int userId);
        public Task<List<DeliveryRequestCard>> GetRoutesForRequest(int userId, int requestId);
    }
}
