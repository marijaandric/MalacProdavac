using back.BLL.Dtos.Cards;
using back.BLL.Dtos.Infos;
using back.DAL.Contexts;
using back.Models;
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

        public async Task<OrderInfo> OrderDetails(int orderId)
        {
            OrderInfo order = await _context.Orders.Select(x => new OrderInfo
            {
                Id = x.Id,
                Quantity = _context.OrderItems.Where(i => i.OrderId == x.Id).Count(),
                Amount = _context.OrderItems.Where(i => i.OrderId == x.Id).Sum(i => i.Price),
                CreatedOn = x.CreatedOn,
                Status = _context.OrderStatuses.FirstOrDefault(s => s.Id == x.Status).Name,
                DeliveryMethod = _context.DeliveryMethods.FirstOrDefault(dm => dm.Id == x.DeliveryMethod).Name,
                PaymentMethod = _context.PaymentMethods.FirstOrDefault(pm => pm.Id == x.PaymentMethod).Name,
                ShippingAddress = x.ShippingAddress

            }).FirstOrDefaultAsync(x => x.Id == orderId);

            List<OrderItemCard> items = await _context.OrderItems.Where(x => x.OrderId == orderId).Select(x => new OrderItemCard
            {
                Name = _context.Products.FirstOrDefault(p => p.Id == x.ProductId) != null ? _context.Products.FirstOrDefault(p => p.Id == x.ProductId).Name : _context.ArchivedProducts.FirstOrDefault(p => p.Id == x.ProductId).Name,
                Shop = _context.Products.FirstOrDefault(p => p.Id == x.ProductId) != null ? _context.Shop.FirstOrDefault(s => s.Id == _context.Products.FirstOrDefault(p => p.Id == x.ProductId).ShopId).Name : _context.Shop.FirstOrDefault(s => s.Id == _context.ArchivedProducts.FirstOrDefault(p => p.Id == x.ProductId).ShopId).Name,
                Price = x.Price,
                Metric = _context.Products.FirstOrDefault(p => p.Id == x.ProductId) != null ? _context.Metrics.FirstOrDefault(m => m.Id == _context.Products.FirstOrDefault(p => p.Id == x.ProductId).MetricId).Name : _context.Metrics.FirstOrDefault(m => m.Id == _context.ArchivedProducts.FirstOrDefault(p => p.Id == x.ProductId).MetricId).Name,
                Quantity = x.Quantity,
                Image = _context.ProductImages.FirstOrDefault(pi => pi.ProductId == x.ProductId).Image

            }).ToListAsync();

            return new OrderInfo
            {
                Id = order.Id,
                Quantity = order.Quantity,
                Amount = order.Amount,
                CreatedOn = order.CreatedOn,
                Status = order.Status,
                Items = items,
                DeliveryMethod = order.DeliveryMethod,
                PaymentMethod = order.DeliveryMethod,
                ShippingAddress = order.ShippingAddress 
            };
        }
    }
}
