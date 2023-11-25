using back.BLL.Dtos;

namespace back.DAL.Repositories
{
    public interface IDeliveryRepository
    {
        public Task<bool> InsertDeliveryRequest(DeliveryRequestDto req);
        public Task<bool> InsertDeliveryRoute(DeliveryRouteDto route);
        public Task<bool> AddToRoute(int requestId, int routeId);
    }
}
