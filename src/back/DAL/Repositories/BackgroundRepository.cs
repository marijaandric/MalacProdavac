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

            userProductPairs.AddRange(await _context.DeliveryRequests.Where(x => ((DateTime)x.PickupDate).AddDays(1) <= DateTime.Now)
            .Join(_context.Orders, dr => dr.OrderId, o => o.Id, (dr, o) => o)
            .Join(_context.OrderItems, o => o.Id, oi => oi.OrderId, (o, oi) => new {o, oi})
            .Where(x => !_context.ProductReviews.Any(y => y.ReviewerId == x.o.UserId && y.ProductId == x.oi.ProductId))
            .Select(x => new PendingReview
            {
                ItemId = x.oi.ProductId,
                UserId = x.o.UserId
            }).ToListAsync());

            userProductPairs.AddRange(await _context.Orders.Where(x => x.PickupTime <= DateTime.Now)
            .Join(_context.OrderItems, o => o.Id, oi => oi.OrderId, (o, oi) => new { o, oi })
            .Where(x => !_context.ProductReviews.Any(y => y.ReviewerId == x.o.UserId && y.ProductId == x.oi.ProductId))
            .Select(x => new PendingReview
            {
                ItemId = x.oi.ProductId,
                UserId = x.o.UserId
            }).ToListAsync());

            return userProductPairs.Distinct().ToList();
        }

        public async Task<List<PendingReview>> PendingShopReviews()
        {
            List<PendingReview> userProductPairs = new List<PendingReview>();

            userProductPairs.AddRange(await _context.DeliveryRequests.Where(x => ((DateTime)x.PickupDate).AddDays(1) <= DateTime.Now)
            .Join(_context.Orders, dr => dr.OrderId, o => o.Id, (dr, o) => o)
            .Where(x => !_context.ShopReviews.Any(y => y.ReviewerId == x.UserId && y.ShopId == x.ShopId))
            .Select(x => new PendingReview
            {
                ItemId = x.ShopId,
                UserId = x.UserId
            }).ToListAsync());

            userProductPairs.AddRange(await _context.Orders.Where(x => x.PickupTime <= DateTime.Now && !_context.ShopReviews.Any(y => y.ReviewerId == x.UserId && y.ShopId== x.ShopId))
            .Select(x => new PendingReview
            {
                ItemId = x.ShopId,
                UserId = x.UserId
            }).ToListAsync());

            return userProductPairs.Distinct().ToList();
        }

        public async Task<List<PendingReview>> PendingDeliveryPersonReviews()
        {
            return await _context.DeliveryRequests.Where(x => ((DateTime)x.PickupDate).AddDays(1) <= DateTime.Now)
            .Join(_context.Orders, dr => dr.OrderId, o => o.Id, (dr, o) => new { dr, o })
            .Where(x => !_context.Ratings.Any(y => y.RaterId == x.o.UserId && y.RatedId == x.dr.ChosenPersonId))
            .Select(x => new PendingReview
            {
                ItemId = (int)x.dr.ChosenPersonId,
                UserId = x.o.UserId
            }).Distinct().ToListAsync();
        }

        public async Task DeletePastProductDisplays()
        {
            var toDelete = (await _context.ProductDisplays.ToListAsync()).Where(x => x.EndDate.Date < DateTime.Now || (x.EndDate.Date == DateTime.Now && x.EndTime <= DateTime.Now.TimeOfDay));
            _context.ProductDisplays.RemoveRange(toDelete);
            await _context.SaveChangesAsync();
        }

        public async Task ChangeDeliveryStatus()
        {
            var toChange = await _context.Orders.Where(x => x.DeliveryMethodId == 1 && x.PickupTime <= DateTime.Now).ToListAsync();
            foreach (var ch in toChange) ch.StatusId = (await _context.OrderStatuses.FirstOrDefaultAsync(x => x.Name == "Delivered")).Id;
        }
    }
}
