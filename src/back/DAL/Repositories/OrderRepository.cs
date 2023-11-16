using back.BLL.Dtos;
using back.DAL.Contexts;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class OrderRepository : IOrderRepository
    {
        Context _context;
        public OrderRepository(Context context)
        {
            _context = context;
        }

        int numberOfItems = 10;

        public async Task<List<OrderCard>> GetOrders(int userId, int status, int page)
        {
            if (status == -1)
            {
                return await _context.Orders.Where(x => x.UserId == userId).Skip(page - 1 * numberOfItems).Take(numberOfItems).Select(x => new OrderCard
                {
                    Id = x.Id,
                    Quantity = _context.OrderItems.Where(i => i.OrderId == x.Id).Count(),
                    Amount = _context.OrderItems.Where(i => i.OrderId == x.Id).Sum(i => i.Price),
                    CreatedOn = x.CreatedOn,
                    Status = _context.OrderStatuses.FirstOrDefault(s => s.Id == x.Status).Name
                    
                }).ToListAsync();
            }

            return await _context.Orders.Where(x => x.UserId == userId && x.Status == status).Skip(page - 1 * numberOfItems).Take(numberOfItems).Select(x => new OrderCard
            {
                Id = x.Id,
                Quantity = _context.OrderItems.Where(i => i.OrderId == x.Id).Count(),
                Amount = _context.OrderItems.Where(i => i.OrderId == x.Id).Sum(i => i.Price),
                CreatedOn = x.CreatedOn,
                Status = _context.OrderStatuses.FirstOrDefault(s => s.Id == x.Status).Name

            }).ToListAsync();
        }
    }
}
