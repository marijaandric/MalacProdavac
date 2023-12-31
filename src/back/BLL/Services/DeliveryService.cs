﻿using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
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
        IOrderRepository _orderRepository;

        public static double priceMax = 350;

        public DeliveryService(IDeliveryRepository repository, IHelperService helperService, IUserRepository userRepository, IShopRepository shopRepository, INotificationRepository notificationRepository, IOrderRepository orderRepository)
        {
            _repository = repository;
            _helperService = helperService;
            _userRepository = userRepository;
            _shopRepository = shopRepository;
            _notificationRepository = notificationRepository;
            _orderRepository = orderRepository;
        }

        public async Task<bool> InsertDeliveryRequest(DeliveryRequestDto req)
        {
            if (await _repository.InsertDeliveryRequest(req) == -1) return false;
            return true;
        }

        public async Task<List<int>> GetNearbySellers(int routeId, double range)
        {
            DeliveryRoute route = await _repository.GetRoute(routeId);
            List<(double, double)> waypoints = await _helperService.GetWaypoints(route.StartLocation, route.EndLocation);
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
            if (route.FixedCost > priceMax) throw new ArgumentException("Price is too high! The highest value is 350.");
            
            string username = await _userRepository.GetUsername(route.UserId);
            DeliveryRoute r = await _repository.InsertDeliveryRoute(route);
            
            if (r == null) return false;
            
            List<int> ownerIds = await GetNearbySellers(r.Id, 10);

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
            return true;
        }

        public async Task<bool> AddToRoute(int requestId, int routeId)
        {
            DeliveryRoute route = await _repository.GetRoute(routeId);
            int userId = await _repository.GetCustomerIdForDelivery(requestId);
            string username = await _userRepository.GetUsername(route.DeliveryPersonId);
            int orderId = (await _repository.GetBaseRequest(requestId)).OrderId;
            float deliveryPrice = await _orderRepository.DeliveryPrice(orderId);

            if (!await _repository.AddToRoute(requestId, routeId)) return false;
            if (await _notificationRepository.InsertNotification(userId, 6, "Delivery accepted!", "Delivery person " + username + " just added your delivery request to their route!\nYou can expect your order to arrive on " + route.StartDate.ToShortDateString() + ".\nThe delivery price is:" + deliveryPrice + "\nTap to view the order.", orderId)) Console.WriteLine("Notification sent!");

            return true;
        }

        public async Task<bool> DeclineRequest(int requestId)
        {
            DeliveryRequest req = await _repository.GetBaseRequest(requestId);
            Order order = await _orderRepository.GetOrder(req.OrderId);
            int userId = await _shopRepository.GetOwnerId(order.ShopId);
            string username = await _userRepository.GetUsername((int)req.ChosenPersonId);

            if (!await _repository.DeclineRequest(requestId)) return false;
            if (await _notificationRepository.InsertNotification(userId, 5, "Delivery declined!", "Delivery person " + username + " declined the delivery request for order #!"+ order.Id +" \nPlease choose another delivery person by clicking here.", requestId)) Console.WriteLine("Notification sent!");

            return true;
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

        public async Task<List<DeliveryRouteCard>> GetRoutesForDeliveryPerson(int userId)
        {
            var routes = await _repository.GetRoutesForDeliveryPerson(userId);
            if (routes.Count == 0) throw new ArgumentException("No routes found!");
            return routes;
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

        public async Task<bool> ChooseDeliveryPerson(int requestId, int chosenPersonId)
        {
            DeliveryRequest req = await _repository.GetBaseRequest(requestId);
            Order o = await _orderRepository.GetOrder(req.OrderId);
            string shopName = (await _shopRepository.GetShop(o.ShopId)).Name;

            if (!await _repository.ChooseDeliveryPerson(requestId, chosenPersonId)) return false;
            if (await _notificationRepository.InsertNotification(chosenPersonId, 5, "New delivery request!", "You have a delivery request from " + shopName + " for order #" + o.Id + "!\nPlease respond to this request.", req.Id)) Console.WriteLine("Notification sent!");

            return true;
        }

        public async Task<DeliveryRouteInfo> GetRouteDetails(int routeId)
        {
            DeliveryRouteInfo route = await _repository.GetRouteDetails(routeId);
            if (route == null) throw new ArgumentException("No route found!");
            
            var start = route.Stops[0];
            var end = route.Stops[1];
            var tail = route.Stops.Skip(2).ToList();
            var shops = tail.Where(x => x.ShopName != null);
            var addresses = tail.Where(x => x.ShopName == null);
            shops.OrderBy(x => HelperService.CalculateDistance(start.Latitude, start.Longitude, x.Latitude, x.Longitude));
            addresses.OrderBy(x => HelperService.CalculateDistance(start.Latitude, start.Longitude, x.Latitude, x.Longitude));
            route.Stops = new List<DeliveryStop>{ start };
            route.Stops.AddRange(shops);
            route.Stops.AddRange(addresses);
            route.Stops.Add(end);
            return route;
        }

        public async Task<DeliveryRequestInfo> GetRequestDetails(int requestId)
        {
            DeliveryRequestInfo req = await _repository.GetRequestDetails(requestId);
            if (req == null) throw new ArgumentException("No request found!");

            return req;
        }

        public async Task<bool> RemoveRequest(int requestId)
        {
            return await _repository.RemoveRequest(requestId);
        }

        public async Task<bool> DeleteRoute(int routeId)
        {
            var reqs = await _repository.GetRequestIdsByRoute(routeId);
            foreach (var req in reqs) await _repository.RemoveRequest(req);

            return await _repository.DeleteRoute(routeId);
        }

        public async Task<bool> EditRoute(EditDeliveryRouteDto dto)
        {
            DeliveryRoute route = await _repository.GetRoute(dto.Id);
            if ((await _repository.GetRequestCoordinates(dto.Id)).Count > 0) throw new ArgumentException("Can't edit a route with existing requests!");

            if (dto.StartDate != null) route.StartDate = DateTime.Parse(dto.StartDate);
            if (dto.StartTime != null) route.StartTime = TimeSpan.Parse(dto.StartTime);
            if (dto.FixedCost != null)
            {
                if (dto.FixedCost <= priceMax) route.FixedCost = (float)dto.FixedCost;
                else throw new ArgumentException("Cost is too high!");
            }
            if (dto.StartLocation != null)
            {
                route.StartLocation = dto.StartLocation;
                (double, double) coords = await HelperService.GetCoordinates(dto.StartLocation);
                route.StartLatitude = coords.Item1;
                route.StartLongitude = coords.Item2;
            }
            if (dto.EndLocation != null)
            {
                route.EndLocation = dto.EndLocation;
                (double, double) coords = await HelperService.GetCoordinates(dto.EndLocation);
                route.EndLatitude = coords.Item1;
                route.EndLongitude = coords.Item2;
            }

            return await _repository.EditRoute(route);
        }

        public async Task<bool> ConfirmRouteEnd(int routeId)
        {
            var route = await _repository.GetRoute(routeId);
            if (route.StartDate > DateTime.Now) throw new ArgumentException("You cannot end that route, the start date hasn't come yet.");
            route.Finished = true;
            return await _repository.EditRoute(route);
        }
    }
}
