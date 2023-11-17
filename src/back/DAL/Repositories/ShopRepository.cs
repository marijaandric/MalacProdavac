using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
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
                case 1:
                    return shops.OrderBy(x => x.Rating).ToList();
                case 2:
                    return shops.OrderByDescending(x => x.Rating).ToList();
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

        public async Task<List<ShopCard>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string? location, int sort, string? search, int page, bool favorite)
        {
            User currentUser = _context.Users.FirstOrDefault(x => x.Id == userId);
            float currLat = currentUser.Latitude;
            float currLong = currentUser.Longitude;

            if (categories.Count == 0) categories = await _context.Categories.Select(x => x.Id).ToListAsync();

            List<ShopCard>  shops = await _context.Shop
                    .Where(x => x.Name.ToLower().Contains(search.Trim().ToLower()) && x.OwnerId != userId)
                    .GroupJoin(_context.ShopReviews.GroupBy(x => x.ShopId).Select(group => new
                    {
                        ShopId = group.Key,
                        AvgRating = group.Average(x => x.Rating)
                    }), s => s.Id, sr => sr.ShopId, (s, sr) => new ShopCard
                    {
                        Id = s.Id,
                        Name = s.Name,
                        Address = s.Address,
                        Image = s.Image,
                        WorkingHours = _context.WorkingHours.Where(x => x.ShopId == s.Id).ToList(),
                        Liked = _context.LikedShops.Any(x => x.ShopId == s.Id && x.UserId == userId),
                        Rating = sr.DefaultIfEmpty().Select(x => x.AvgRating).FirstOrDefault()
                    })
                .Where(x => x.Rating >= rating)
                .Join(_context.ShopCategories.Where(x => categories.Contains(x.CategoryId)), s => s.Id, sr => sr.ShopId, (s, sr) => s)
                .GroupBy(x => x.Id).Select(x => x.First())
                .ToListAsync();
            
            if (favorite)
            {
                shops = shops.Join(_context.LikedShops.Where(x => x.UserId == userId), s => s.Id, ls => ls.ShopId, (s, ls) => s).ToList();
            }

            if (open)
            {
                shops = shops
                        .Join(_context.WorkingHours, s => s.Id, w => w.ShopId, (s, w) => new { s , w })
                        .ToList()
                        .Where(x => x.w.Day == DateTime.Now.DayOfWeek && x.w.OpeningHours <= DateTime.Now.TimeOfDay && x.w.ClosingHours >= DateTime.Now.TimeOfDay)
                        .Select(x => x.s)
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
        
        public async Task<ShopInfo> ShopDetails(int shopId, int userId)
        {
            Shop shop = await _context.Shop.FirstOrDefaultAsync(x => x.Id == shopId);
            List<ShopReviewExtended> reviews = await _context.ShopReviews.Where(x => x.ShopId == shopId).Select(x => new ShopReviewExtended
                                    {
                                        ReviewerId = x.ReviewerId,
                                        ShopId = x.ShopId,
                                        Rating = x.Rating,
                                        Comment = x.Comment,
                                        PostedOn = x.PostedOn,
                                        Username = _context.Users.FirstOrDefault(u => u.Id == x.ReviewerId).Username,
                                        Image = _context.Users.FirstOrDefault(i => i.Id == x.ReviewerId).Image,
                                        Shop = null
                                    }).ToListAsync();
            List<string> categories = await _context.ShopCategories.Where(x => x.ShopId == shopId).Join(_context.Categories, sc => sc.CategoryId, c => c.Id, (sc, c) => c).Select(x => x.Name).ToListAsync();
            List<string> subcategories = await _context.ShopSubcategories.Where(x => x.ShopId == shopId).Join(_context.Subcategories, sc => sc.SubcategoryId, c => c.Id, (sc, c) => c).Select(x => x.Name).ToListAsync();
            List<WorkingHours> workingHours = await _context.WorkingHours.Where(x => x.ShopId == shopId).Select(wh => new WorkingHours
                                {
                                    Day = wh.Day,
                                    OpeningHours = wh.OpeningHours,
                                    ClosingHours = wh.ClosingHours,
                                    Shop = null
                                }).ToListAsync();

            float avg = 0;
            if (reviews.Count > 0) avg = reviews.Average(x => x.Rating);

            return new ShopInfo
            {
                Id = shop.Id,
                Name = shop.Name,
                Address = shop.Address,
                Image = shop.Image,
                Rating = avg,
                Liked = _context.LikedShops.Any(x => x.ShopId == shopId && x.UserId ==  userId),
                BoughtFrom = _context.Orders.Join(_context.OrderItems, o => o.Id, oi => oi.OrderId, (o, oi) => new { o, oi })
                            .Join(_context.Products, order => order.oi.ProductId, p => p.Id, (order, p) => new { order, p })
                            .Any(x => x.p.ShopId == shopId  && x.order.o.UserId == userId),
                Rated = _context.ShopReviews.Any(x => x.ShopId == shopId && x.ReviewerId == userId),
                IsOwner = shop.OwnerId == userId,
                Reviews = reviews,
                Categories = categories,
                Subcategories = subcategories,
                WorkingHours = workingHours,
            };
        }

        #region likes
        public async Task<LikedShops> GetLike(int shopId, int userId)
        {
            return await _context.LikedShops.FirstOrDefaultAsync(x => x.ShopId == shopId && x.UserId == userId);
        }

        public async Task<bool> LikeShop(int shopId, int userId)
        {
            await _context.LikedShops.AddAsync(new LikedShops { ShopId = shopId, UserId = userId });
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> DislikeShop(int shopId, int userId)
        {
            _context.LikedShops.Remove(await GetLike(shopId, userId));
            return await _context.SaveChangesAsync() > 0;
        }
        #endregion

        public async Task<bool> LeaveReview(ReviewDto review)
        {
            _context.ShopReviews.AddAsync(new ShopReview
            {
                ReviewerId = review.UserId,
                ShopId = review.Id,
                Rating = review.Rating,
                Comment = review.Comment,
                PostedOn = DateTime.Now
            });

            return _context.SaveChanges() > 0;
        }

        public async Task<bool> ChangeShopPhoto(int id, string path)
        {
            Shop s = await _context.Shop.FirstOrDefaultAsync(x => x.Id == id);
            s.Image = path;
            return await _context.SaveChangesAsync() > 0;
        }
    }
}
