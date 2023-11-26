using back.BLL.Dtos;
using back.DAL.Repositories;

namespace back.BLL.Services
{
    public class DeliveryService : IDeliveryService
    {
        IDeliveryRepository _repository;
        IHelperService _helperService;

        public DeliveryService(IDeliveryRepository repository, IHelperService helperService)
        {
            _repository = repository;
            _helperService = helperService;
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

        public async Task<List<DeliveryRequestCard>> GetRequestsForDeliveryPerson(int deliveryPerson)
        {
            List<DeliveryRequestCard> requests = await _repository.GetRequestsForDeliveryPerson(deliveryPerson);
            if (requests == null || requests.Count == 0) throw new ArgumentException("No requests!");
            return requests;
        }

        public async Task<List<DeliveryRequestCard>> GetRequestsForShop(int userId)
        {
            List<DeliveryRequestCard> requests = await _repository.GetRequestsForShop(userId);
            if (requests == null || requests.Count == 0) throw new ArgumentException("No requests!");
            return requests;
        }

        public async Task<List<DeliveryRequestCard>> GetRoutesForRequest(int userId, int requestId)
        {
            var routes = await _repository.GetRoutesForDeliveryPerson(userId);
            var request = await _repository.GetRequest(requestId);

            var result = routes
            .Select(routeItem => new
            {
                Route = routeItem,
                RouteDivergence = _helperService.Route(routeItem.StartLocation, routeItem.EndLocation, request.StartAddress, request.EndAddress)
            })
            .OrderBy(x => x.RouteDivergence.Result)
            .Take(10)
            .Select(x => new DeliveryRequestCard
            {
                CreatedOn = request.CreatedOn,
                EndAddress = request.EndAddress,
                Locations = request.Locations,
                StartAddress = request.StartAddress,
                RouteDivergence = x.RouteDivergence.Result
            })
            .ToList();

            return result;

        }
    }
}
