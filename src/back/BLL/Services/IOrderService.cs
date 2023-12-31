﻿using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.Infos;
using back.Models;

namespace back.BLL.Services
{
    public interface IOrderService
    {
        public Task<int> GetOrdersPageCount(int userId, int? status);
        public Task<List<OrderCard>> GetOrders(int userId, int? status, int page);
        public Task<int> GetShopOrdersPageCount(int ownerId, int? status);
        public Task<List<OrderCard>> GetShopOrders(int ownerId, int? status, int page);
        public Task<OrderInfo> OrderDetails(int orderId);
        public Task<OrderInfo> ShopOrderDetails(int orderId);
        public Task<bool> InsertOrders(List<OrderDto> order);
        public Task<bool> RespondToPickupRequest(int orderId, int resp, string? message);
        public Task<List<Card>> GetCards(int userId);
        public Task<bool> InsertCard(CardDto dto);
        public Task<bool> DeleteCard(int cardId);
        public Task<List<CheckedStockDto>> CheckStock(List<StockToCheckDto> toCheck);
    }
}
