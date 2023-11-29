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
        IShopRepository _shopRepository;
        INotificationRepository _notificationRepository;

        public DeliveryService(IDeliveryRepository repository, IHelperService helperService, IUserRepository userRepository, IShopRepository shopRepository, INotificationRepository notificationRepository)
        {
            _repository = repository;
            _helperService = helperService;
            _userRepository = userRepository;
            _shopRepository = shopRepository;
            _notificationRepository = notificationRepository;
        }

        public async Task<bool> InsertDeliveryRequest(DeliveryRequestDto req)
        {
            return await _repository.InsertDeliveryRequest(req);
        }

        public async Task<List<int>> GetNearbySellers(string routeStart, string routeEnd, double range)
        {
            List<(double, double)> waypoints = await _helperService.GetWaypoints(routeStart, routeEnd);
            var shops = await _shopRepository.GetAllShops();
            return shops.Where(x => _helperService.NearRoute(waypoints, (double)x.Latitude, (double)x.Longitude, range).Result).Select(x => x.OwnerId).ToList();
        }
        
        public async Task<List<LikedShops>> GetFollowersForNearbySellers(List<int> sellerIds)
        {
            List<LikedShops> followers = new List<LikedShops>();
            foreach (int id in sellerIds) followers.AddRange(await _shopRepository.GetFollowerIds(id));

            return followers.Distinct().ToList();
        }

        public async Task<bool> InsertDeliveryRoute(DeliveryRouteDto route)
        {
            string username = await _userRepository.GetUsername(route.UserId);
            bool? ind = await _repository.InsertDeliveryRoute(route);
            if (ind == null) throw new ArgumentException("Price is too high! The highest value is 350.");
            
            List<int> ownerIds = await GetNearbySellers(route.StartLocation, route.EndLocation, 10);

            if (ind == false) return false;

            foreach (int ownerId in ownerIds)
            {
                if (await _notificationRepository.InsertNotification(ownerId, 0, "Delivery person available!", "Delivery person " + username + " just made a route close to your shop!\nThe route starts in " + route.StartLocation + " on " + route.StartDate.ToShortDateString() + " at " + route.StartTime + " and ends in " + route.EndLocation + ".", -1)) Console.WriteLine("Notification sent!");
            }

            List<LikedShops> followerIds = await GetFollowersForNearbySellers(ownerIds);

            foreach (LikedShops followerId in followerIds)
            {
                Shop s = await _shopRepository.GetShop(followerId.ShopId);
                if (followerId.UserId != route.UserId && await _notificationRepository.InsertNotification(followerId.UserId, 0, "Delivery person near "+ s.Name +"!", "Delivery person " + username + " just made a route close to your favorite shop "+ s.Name+"!\nThe route starts in " + route.StartLocation + " on " + route.StartDate.ToShortDateString() + " at " + route.StartTime + " and ends in " + route.EndLocation + ".", -1)) Console.WriteLine("Notification sent!");
            }
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
