using back.BLL.Dtos.HelpModels;
using back.DAL.Contexts;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class BackgroundRepository : IBackgroundRepository
    {
        private readonly Context _context;

        public BackgroundRepository(Context context)
        {
            _context = context;
        }

        public async Task<List<PendingReview>> PendingProductReviews()
        {
            List<PendingReview> userProductPairs = new List<PendingReview>();

            userProductPairs.AddRange(await _context.DeliveryRequests.Where(x => x.PickupDate <= DateTime.Now)
            .Join(_context.Orders, dr => dr.OrderId, o => o.Id, (dr, o) => o)
            .Join(_context.OrderItems, o => o.Id, oi => oi.OrderId, (o, oi) => new {o, oi})
            .Where(x => !_context.ProductReviews.Any(y => y.ReviewerId == x.o.UserId && y.ProductId == x.oi.ProductId))
            .Select(x => new PendingReview
            {
                ProductId = x.oi.ProductId,
                UserId = x.o.UserId
            }).ToListAsync());

            userProductPairs.AddRange(await _context.Orders.Where(x => x.PickupTime <= DateTime.Now)
            .Join(_context.OrderItems, o => o.Id, oi => oi.OrderId, (o, oi) => new { o, oi })
            .Where(x => !_context.ProductReviews.Any(y => y.ReviewerId == x.o.UserId && y.ProductId == x.oi.ProductId))
            .Select(x => new PendingReview
            {
                ProductId = x.oi.ProductId,
                UserId = x.o.UserId
            }).ToListAsync());

            return userProductPairs.Distinct().ToList();
        }
    }
}
