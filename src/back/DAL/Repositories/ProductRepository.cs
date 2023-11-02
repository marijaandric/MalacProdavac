using back.DAL.Contexts;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class ProductRepository : IProductRepository
    {
        Context _context;
        int numberOfItems = 10;
        public ProductRepository(Context context)
        {
            _context = context;
        }

        #region filterHelp
        public List<Product> SortProducts(int sort, List<Product> products)
        {
            switch (sort)
            {
                case 1:
                    return products.OrderBy(x => x.Price).ToList();
                case 2:
                    return products.OrderByDescending(x => x.Price).ToList();
                case 3:
                    return products.OrderBy(x => x.Name).ToList();
                case 4:
                    return products.OrderByDescending(x => x.Name).ToList();
                default:
                    return products.OrderBy(x => x.Id).ToList();
            }
        }

        public static double CalculateDistance(float lat1, float lon1, float lat2, float lon2)
        {
            const double radiusOfEarth = 6371;

            // degrees -> radians
            double dLat = Math.PI / 180.0 * (lat2 - lat1);
            double dLon = Math.PI / 180.0 * (lon2 - lon1);

            // Haversine formula
            double a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                Math.Cos(Math.PI / 180.0 * lat1) * Math.Cos(Math.PI / 180.0 * lat2) *
                Math.Sin(dLon / 2) * Math.Sin(dLon / 2);
            double c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));

            double distance = radiusOfEarth * c;

            return distance;
        }

        #endregion

        public async Task<List<Product>> GetProducts(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page)
        {
            List<Product> products;
            User currentUser = _context.Users.FirstOrDefault(x => x.Id == userId);
            float currLat = currentUser.Latitude;
            float currLong = currentUser.Longitude;

            if (categories.Count == 0) categories = await _context.Categories.Select(x => x.Id).ToListAsync();
            
            if (rating > 0)
            {
                products = await _context.Products.Where(x => categories.Contains(x.CategoryId) && x.Name.ToLower().Contains(search.Trim().ToLower()))
                                    .Join(_context.ProductReviews.GroupBy(x => x.ProductId).Select(group => new
                                    {
                                        ProductId = group.Key,
                                        avg = group.Average(x => x.Rating)
                                    }).Where(x => x.avg >= rating), p => p.Id, pr => pr.ProductId, (p, pr) => p)
                                    .ToListAsync();
            }
            else
            {
                products = await _context.Products.Where(x => categories.Contains(x.CategoryId) && x.Name.ToLower().Contains(search.Trim().ToLower())).ToListAsync();
            }
            

            if (open)
            {
                products = products
                        .Join(_context.Shop.Join(_context.WorkingHours.Where(x => x.Day.Day == DateTime.Now.Day && x.OpeningHours.TimeOfDay <= DateTime.Now.TimeOfDay && x.ClosingHours.TimeOfDay >= DateTime.Now.TimeOfDay), s => s.Id, w => w.ShopId, (s, w) => s), p => p.ShopId, s => s.Id, (p, s) => p)
                        .ToList();
            }

            if (location.Trim().Length > 0 && location != "none")
            {
                products = products.Join(_context.Shop.Where(x => x.Address.Trim().ToLower().Contains(location.Trim().ToLower())), p => p.ShopId, s => s.Id, (p, s) => p).ToList();
            }
            else if (range > 0)
            {
                products = products.Join(_context.Shop, p => p.ShopId, s => s.Id, (p, s) => (p, s)).Where(x => CalculateDistance((float)x.s.Latitude, (float)x.s.Longitude, currLat, currLong) <= range).Select(x => x.p).ToList();
            }

            products = SortProducts(sort, products);

            return products.Skip((page-1) * numberOfItems).Take(numberOfItems).ToList();
        }

        public int ProductPages()
        {
            return (int)Math.Ceiling((double)_context.Products.Count()/numberOfItems);
        }
    }
}
