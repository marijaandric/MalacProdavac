using back.BLL.Dtos.Cards;
using back.BLL.Dtos.Infos;

namespace back.BLL.Services
{
    public interface IOrderService
    {
        public Task<List<OrderCard>> GetOrders(int userId, int status, int page);
        public Task<OrderInfo> OrderDetails(int orderId);

    }
}
