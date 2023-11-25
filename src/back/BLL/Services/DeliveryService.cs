using back.BLL.Dtos;
using back.DAL.Repositories;

namespace back.BLL.Services
{
    public class DeliveryService : IDeliveryService
    {
        IDeliveryRepository _repository;

        public DeliveryService(IDeliveryRepository repository)
        {
            _repository = repository;
        }

        public async Task<bool> InsertDeliveryRequest(DeliveryRequestDto req)
        {
            return await _repository.InsertDeliveryRequest(req);
        }

        public async Task<bool> InsertDeliveryRoute(DeliveryRouteDto route)
        {
            return await _repository.InsertDeliveryRoute(route);
        }

        public async Task<bool> AddToRoute(int requestId, int routeId)
        {
            return await _repository.AddToRoute(requestId, routeId);
        }
    }
}
