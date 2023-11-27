using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.DAL.Repositories;
using back.Models;

namespace back.BLL.Services
{
    public class DeliveryService : IDeliveryService
    {
        IDeliveryRepository _repository;
        IHelperService _helperService;
        IUserRepository _userRepository;

        public DeliveryService(IDeliveryRepository repository, IHelperService helperService, IUserRepository userRepository)
        {
            _repository = repository;
            _helperService = helperService;
            _userRepository = userRepository;
        }

        public async Task<bool> InsertDeliveryRequest(DeliveryRequestDto req)
        {
            return await _repository.InsertDeliveryRequest(req);
        }

        public async Task<bool> InsertDeliveryRoute(DeliveryRouteDto route)
        {
            bool? ind = await _repository.InsertDeliveryRoute(route);
            if (ind == null) throw new ArgumentException("Price is too high! The highest value is 350.");

            return (bool)ind;
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

        public async Task<List<DeliveryRouteCard>> GetRoutesForRequest(int userId, int requestId)
        {
            var routes = await _repository.GetRoutesForDeliveryPerson(userId);
            var request = await _repository.GetRequest(requestId);

            var result = routes
            .Select(routeItem => new
            {
                Route = routeItem,
                RouteDivergence = _helperService.Route(routeItem.StartAddress, routeItem.EndAddress, request.StartAddress, request.EndAddress)
            })
            .OrderBy(x => x.RouteDivergence.Result)
            .Take(10)
            .Select(x => new DeliveryRouteCard
            {
                CreatedOn = x.Route.CreatedOn,
                EndAddress = x.Route.EndAddress,
                Locations = x.Route.Locations,
                StartAddress = x.Route.StartAddress,
                RouteDivergence = x.RouteDivergence.Result,
                Cost = x.Route.Cost,
                Id = x.Route.Id,
                StartTime = x.Route.StartTime
            })
            .ToList();

            return result;

        }

        public async Task<List<DeliveryPersonCard>> GetDeliveryPeopleForRequest(int requestId)
        {
            List<User> deliveryPeople = await _userRepository.GetDeliveryPeople();
            List<DeliveryPersonCard> deliveryPeopleList = new List<DeliveryPersonCard>();

            foreach (var deliveryPerson in deliveryPeople)
            {
                var min = (await GetRoutesForRequest(deliveryPerson.Id, requestId)).First();
                deliveryPeopleList.Add(new DeliveryPersonCard
                {
                    DeliveryPerson = deliveryPerson.Username,
                    ClosestRouteDivergence = min.RouteDivergence,
                    Date = (DateTime)min.CreatedOn,
                    DeliveryPersonId = deliveryPerson.Id,
                    Price = min.Cost
                });
            }

            return deliveryPeopleList.OrderBy(x => x.ClosestRouteDivergence).Take(10).ToList();
        }
    }
}
