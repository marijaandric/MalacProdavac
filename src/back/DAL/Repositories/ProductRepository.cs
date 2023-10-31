using back.DAL.Contexts;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class ProductRepository : IProductRepository
    {
        Context _context;
        public ProductRepository(Context context)
        {
            _context = context;
        }
        public async Task<List<Product>> GetProducts(List<int> categories, int rating, bool open, int range, string location)
        {
            if (categories.Count == 0) categories = await _context.Categories.Select(x => x.Id).ToListAsync();
            
            List<Product> products = await _context.Products.Where(x => categories.Contains(x.CategoryId))
                                    .Join(_context.ProductReviews.GroupBy(x => x.ProductId).Select(group => new
                                    {
                                        ProductId = group.Key,
                                        avg = group.Average(x => x.Rating)
                                    }).Where(x => x.avg >= rating), p => p.Id, pr => pr.ProductId, (p, pr) => p)
                                    .ToListAsync();

            if (open)
            {
                products = products
                        .Join(_context.Shop.Join(_context.WorkingHours.Where(x => x.Day.Day == DateTime.Now.Day && x.OpeningHours.TimeOfDay <= DateTime.Now.TimeOfDay && x.ClosingHours.TimeOfDay >= DateTime.Now.TimeOfDay), s => s.Id, w => w.ShopId, (s, w) => s), p => p.ShopId, s => s.Id, (p, s) => p)
                        .ToList();
            }

            return products;
        }
    }
}
