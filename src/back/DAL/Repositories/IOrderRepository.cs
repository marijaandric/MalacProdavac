using back.BLL.Dtos;

namespace back.DAL.Repositories
{
    public interface IOrderRepository
    {
        public Task<List<OrderCard>> GetOrders(int userId, int status, int page);
    }
}
