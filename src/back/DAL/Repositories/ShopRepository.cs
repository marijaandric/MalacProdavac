using back.BLL.Dtos;
using back.DAL.Contexts;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class ShopRepository : IShopRepository
    {
        Context _context;
        int numberOfItems = 10;

        public ShopRepository(Context context)
        {
            _context = context;
        }

        #region filterHelp
        public List<ShopCard> SortShops(int sort, List<ShopCard> shops)
        {
            switch (sort)
            {
                case 3:
                    return shops.OrderBy(x => x.Name).ToList();
                case 4:
                    return shops.OrderByDescending(x => x.Name).ToList();
                default:
                    return shops.OrderBy(x => x.Id).ToList();
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

        public async Task<List<ShopCard>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page)
        {
            User currentUser = _context.Users.FirstOrDefault(x => x.Id == userId);
            float currLat = currentUser.Latitude;
            float currLong = currentUser.Longitude;

            if (categories.Count == 0) categories = await _context.Categories.Select(x => x.Id).ToListAsync();

            List<ShopCard>  shops = await _context.Shop
                    .Where(x => x.Name.ToLower().Contains(search.Trim().ToLower()))
                    .GroupJoin(_context.ShopReviews.GroupBy(x => x.ShopId).Select(group => new
                    {
                        ShopId = group.Key,
                        AvgRating = group.Average(x => x.Rating)
                    }), s => s.Id, sr => sr.ShopId, (s, sr) => new ShopCard
                    {
                        Id = s.Id,
                        OwnerId = s.OwnerId,
                        Name = s.Name,
                        Address = s.Address,
                        Latitude = s.Latitude,
                        Longitude = s.Longitude,
                        Image = s.Image,
                        Rating = sr.DefaultIfEmpty().Select(x => x.AvgRating).FirstOrDefault()
                    })
                .Where(x => x.Rating >= rating)
                .Join(_context.ShopCategories.Where(x => categories.Contains(x.CategoryId)), s => s.Id, sr => sr.ShopId, (s, sr) => s).Distinct()
                .ToListAsync();
            

            if (open)
            {
                shops = shops
                        .Join(_context.WorkingHours.Where(x => x.Day.Day == DateTime.Now.Day && x.OpeningHours.TimeOfDay <= DateTime.Now.TimeOfDay && x.ClosingHours.TimeOfDay >= DateTime.Now.TimeOfDay), s => s.Id, w => w.ShopId, (s, w) => s)
                        .ToList();
            }

            if (location.Trim().Length > 0 && location != "none")
            {
                shops = shops.Where(x => x.Address.Trim().ToLower().Contains(location.Trim().ToLower())).ToList();
            }
            else if (range > 0)
            {
                shops = shops.Where(x => CalculateDistance((float)x.Latitude, (float)x.Longitude, currLat, currLong) <= range).ToList();
            }

            shops = SortShops(sort, shops);

            return shops.Skip((page - 1) * numberOfItems).Take(numberOfItems).ToList();
        }

        public int ShopPages()
        {
            return (int)Math.Ceiling((double)_context.Shop.Count()/numberOfItems);
        }
    }
}
