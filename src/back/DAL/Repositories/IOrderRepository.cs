using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.Infos;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IOrderRepository
    {
        public Task<List<OrderCard>> GetOrders(int userId, int? status, int page);
        public Task<OrderInfo> OrderDetails(int orderId);
        public Task<Order> InsertOrder(OrderDto order);
        public Task<bool> UpdateResponse(int orderId, int resp);
        public Task<Order> GetOrder(int orderId);
        public Task<PaymentSlipInfo> GetPaymentSlipInfo(int userId, int shopId);
        public Task<bool> DeleteOrder(Order order);
    }
}
