using back.BLL.Dtos;

namespace back.DAL.Repositories
{
    public interface IDeliveryRepository
    {
        public Task<bool> InsertDeliveryRequest(DeliveryRequestDto req);
    }
}
