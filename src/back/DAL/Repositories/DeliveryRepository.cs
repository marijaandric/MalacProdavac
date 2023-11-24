using back.BLL.Dtos;
using back.DAL.Contexts;
using back.Models;

namespace back.DAL.Repositories
{
    public class DeliveryRepository : IDeliveryRepository
    {
        Context _context;

        public DeliveryRepository(Context context)
        {
            _context = context;
        }

        public async Task<bool> InsertDeliveryRequest(DeliveryRequestDto dto)
        {
            await _context.DeliveryRequests.AddAsync(new DeliveryRequest
            {
                OrderId = dto.OrderId,
                ShopId = dto.ShopId,
                CreatedOn = DateTime.Now
            });

            return await _context.SaveChangesAsync() > 0;
        }
    }
}
