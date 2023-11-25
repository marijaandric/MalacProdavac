using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface IDeliveryService
    {
        public Task<bool> InsertDeliveryRequest(DeliveryRequestDto req);
        public Task<bool> InsertDeliveryRoute(DeliveryRouteDto route);
    }
}
