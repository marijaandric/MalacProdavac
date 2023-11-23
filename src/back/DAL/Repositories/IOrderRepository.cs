using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.Infos;

namespace back.DAL.Repositories
{
    public interface IOrderRepository
    {
        public Task<List<OrderCard>> GetOrders(int userId, int? status, int page);
        public Task<OrderInfo> OrderDetails(int orderId);
        public Task<bool> InsertOrder(OrderDto order);
    }
}
