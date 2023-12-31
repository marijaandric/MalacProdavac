﻿using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.Infos;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IDeliveryRepository
    {
        public Task<int> InsertDeliveryRequest(DeliveryRequestDto req);
        public Task<DeliveryRoute> InsertDeliveryRoute(DeliveryRouteDto route);
        public Task<bool> AddToRoute(int requestId, int routeId);
        public Task<bool> DeclineRequest(int requestId);
        public Task<List<DeliveryRequestCard>> GetRequestsForShop(int userId);
        public Task<List<DeliveryRequestCard>> GetRequestsForDeliveryPerson(int deliveryPerson);
        public Task<List<DeliveryRouteCard>> GetRoutesForDeliveryPerson(int userId);
        public Task<DeliveryRequestCard> GetRequest(int requestId);
        public Task<DeliveryRequest> GetBaseRequest(int requestId);
        public Task<DeliveryRoute> GetRoute(int routeId);
        public Task<int> GetCustomerIdForDelivery(int requestId);
        public Task<bool> ChooseDeliveryPerson(int requestId, int chosenPersonId);
        public Task<List<string>> GetRequestCoordinates(int routeId);
        public Task<DeliveryRouteInfo> GetRouteDetails(int routeId);
        public Task<DeliveryRequestInfo> GetRequestDetails(int requestId);
        public Task<List<int>> GetRequestIdsByRoute(int routeId);
        public Task<bool> RemoveRequest(int requestId);
        public Task<bool> DeleteRoute(int routeId);
        public Task<bool> EditRoute(DeliveryRoute newRoute);
    }
}
