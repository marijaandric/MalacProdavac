﻿using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.Infos;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IOrderRepository
    {
        public Task<int> GetOrdersPageCount(int userId, int? status);
        public Task<float> DeliveryPrice(int orderId);
        public Task<float> RouteProfit(int routeId);
        public Task<float> TotalProfit(int userId);
        public Task<List<OrderCard>> GetOrders(int userId, int? status, int page);
        public Task<int> GetShopOrdersPageCount(int ownerId, int? status);
        public Task<List<OrderCard>> GetShopOrders(int ownerId, int? status, int page);
        public Task<OrderInfo> OrderDetails(int orderId);
        public  Task<OrderInfo> ShopOrderDetails(int orderId);
        public Task<Order> InsertOrder(OrderDto order);
        public Task<bool> UpdateResponse(int orderId, int resp);
        public Task<Order> GetOrder(int orderId);
        public Task<PaymentSlipInfo> GetPaymentSlipInfo(int userId, int shopId);
        public Task<bool> DeleteOrder(Order order);
        public Task<List<Card>> GetCards(int userId);
        public Task<bool> InsertCard(Card card);
        public Task<bool> DeleteCard(int cardId);
        public Task<CheckedStockDto> CheckStock(StockToCheckDto toCheck);
    }
}
