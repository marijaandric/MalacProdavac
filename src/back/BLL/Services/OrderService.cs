using back.BLL.Dtos.Cards;
using back.BLL.Dtos.Infos;
using back.DAL.Repositories;

namespace back.BLL.Services
{
    public class OrderService : IOrderService
    {
        IOrderRepository _repository;
        public OrderService(IOrderRepository repository)
        {
            _repository = repository;
        }

        public async Task<List<OrderCard>> GetOrders(int userId, int status, int page)
        {
            List<OrderCard> orders = await _repository.GetOrders(userId, status, page);
            if (orders.Count == 0) throw new ArgumentException("No orders!");

            return orders;
        }

        public async Task<OrderInfo> OrderDetails(int orderId)
        {
            OrderInfo info = await _repository.OrderDetails(orderId);
            if (info == null) throw new ArgumentException("No order found!");

            return info;
        }
    }
}
