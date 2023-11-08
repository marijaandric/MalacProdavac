using System.Formats.Tar;
using back.BLL.Dtos;
using back.BLL.Dtos.HelpModels;
using back.DAL.Contexts;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class UserRepository : IUserRepository
    {
        Context _context;
        int numberOfItems = 5;
        public UserRepository(Context context)
        {
            _context = context;
        }

        public async Task<MyProfileInfo> GetMyProfile(int userId)
        {
            User user = await _context.Users.FirstOrDefaultAsync(x => x.Id == userId);
            string role = (await _context.Roles.FirstOrDefaultAsync(x => x.Id == user.RoleId)).Name;
            List<Rating> ratings = await _context.Ratings.Where(x => x.RatedId == userId).ToListAsync();
            float comm = ratings.Count > 0 ? ratings.Average(x => x.Communication) : 0;
            float rel = ratings.Count > 0 ? ratings.Average(x => x.Reliability) : 0;
            float oe = ratings.Count > 0 ? ratings.Average(x => x.OverallExperience) : 0;
            float avg = ratings.Count > 0 ? ratings.Average(x => x.Average) : 0;
            float moneySpent = await _context.Orders.Where(x => x.UserId == userId).Join(_context.OrderItems, o => o.Id, oi => oi.OrderId, (o, oi) => oi).SumAsync(x => x.Price * x.Quantity);
            float moneyEarned = 0;
            if (user.RoleId == 2)
            {
                int shopId = (await _context.Shop.FirstOrDefaultAsync(x => x.OwnerId == userId)).Id;
                moneyEarned = await _context.OrderItems.Join(_context.Products.Where(x => x.ShopId == shopId), oi => oi.ProductId, p => p.Id, (oi, p) => oi).SumAsync(x => x.Price * x.Quantity);
            }
            return new MyProfileInfo
            {
                Id = userId,
                Name = user.Name,
                Image = user.Image,
                Role = role,
                RoleId = user.RoleId,
                Username = user.Username,
                Address = user.Address,
                Email = user.Email,
                Rating = new Rating
                {
                    Communication = comm,
                    Reliability = rel,
                    OverallExperience = oe,
                    Average = avg
                },
                CreatedOn = user.CreatedOn,
                MoneySpent = moneySpent,
                MoneyEarned = moneyEarned
            };
        }

        public async Task<PublicProfileInfo> GetPublicProfile(int targetId)
        {
            User user = await _context.Users.FirstOrDefaultAsync(x => x.Id == targetId);
            string role = (await _context.Roles.FirstOrDefaultAsync(x => x.Id == user.RoleId)).Name;
            List<Rating> ratings = await _context.Ratings.Where(x => x.RatedId == targetId).ToListAsync();
            float comm = ratings.Count > 0 ? ratings.Average(x => x.Communication) : 0;
            float rel = ratings.Count > 0 ? ratings.Average(x => x.Reliability) : 0;
            float oe = ratings.Count > 0 ? ratings.Average(x => x.OverallExperience) : 0;
            float avg = ratings.Count > 0 ? ratings.Average(x => x.Average) : 0;

            return new PublicProfileInfo
            {
                Id = targetId,
                Name = user.Name,
                Image = user.Image,
                Role = role,
                RoleId = user.RoleId,
                Username = user.Username,
                Rating = new Rating
                {
                    Communication = comm,
                    Reliability = rel,
                    OverallExperience = oe,
                    Average = avg
                },
            };
        }

        public async Task<List<ProductReviewExtended>> GetProductReviewsOfAShop(int userId, int shopId, int page)
        {
            User user = await _context.Users.FirstOrDefaultAsync(x => x.Id == userId);
            return await _context.ProductReviews.Where(x => x.ReviewerId == userId).Join(_context.Products.Where(x => x.ShopId == shopId), pr => pr.ProductId, p => p.Id, (pr, p) => pr)
                        .Skip((page - 1) * numberOfItems).Take(numberOfItems).Select(x => new ProductReviewExtended
                        {
                            ReviewerId = userId,
                            ProductId = x.ProductId,
                            Rating = x.Rating,
                            Comment = x.Comment,
                            PostedOn = x.PostedOn,
                            Username = user.Username,
                            Image = user.Image,
                            Product = null
                        }).ToListAsync();
        }

        public async Task<ShopReviewExtended> GetReviewOfAShop(int userId, int shopId)
        {
            return await _context.ShopReviews.Where(x => x.ReviewerId == userId && x.ShopId == shopId).Select(x => new ShopReviewExtended
                        {
                            ReviewerId = x.ReviewerId,
                            ShopId = x.ShopId,
                            Rating = x.Rating,
                            Comment = x.Comment,
                            PostedOn = x.PostedOn,
                            Username = _context.Users.FirstOrDefault(u => u.Id == x.ReviewerId).Username,
                            Image = _context.Users.FirstOrDefault(i => i.Id == x.ReviewerId).Image,
                            Shop = null
            }).FirstOrDefaultAsync();
        }

    }
}
