using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface IOrderService
    {
        public Task<List<OrderCard>> GetOrders(int userId, int status, int page);
    }
}
