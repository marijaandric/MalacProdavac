using back.BLL.Dtos;
using back.DAL.Contexts;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class UserRepository : IUserRepository
    {
        Context _context;
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

    }
}
