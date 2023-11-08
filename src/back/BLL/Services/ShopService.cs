using back.BLL.Dtos;
using back.DAL.Repositories;
using back.Models;

namespace back.BLL.Services
{
    public class ShopService : IShopService
    {
        IShopRepository _repository;
        public ShopService(IShopRepository repository)
        {
            _repository = repository;
        }

        public async Task<List<ShopCard>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page)
        {
            List<ShopCard> shops = await _repository.GetShops(userId, categories, rating, open, range, location, sort, search, page);
            if (shops.Count == 0) throw new ArgumentException("No shops found.");
            return shops;
        }

        public int ShopPages()
        {
            int total = _repository.ShopPages();
            if (total == 0) throw new ArgumentException("No pages.");

            return total;
        }

        public async Task<ShopInfo> ShopDetails(int shopId, int userId)
        {
            ShopInfo shop = await _repository.ShopDetails(shopId, userId);
            if (shop == null) throw new ArgumentException("No shop found!");

            return shop;
        }

        public async Task<LikedShops> GetLike(int shopId, int userId)
        {
            LikedShops like = await _repository.GetLike(shopId, userId);
            if (like == null) throw new ArgumentException("No like found!");

            return like;
        }
        public async Task<bool> ToggleLike(int shopId, int userId)
        {
            if (await _repository.GetLike(shopId, userId) == null)
            {
                if (!await _repository.LikeShop(shopId, userId)) throw new ArgumentException("Shop could not be liked!");
                return true;
            }

            if (!await _repository.DislikeShop(shopId, userId)) throw new ArgumentException("Shop could not be disliked!");
            return true;
        }

        public async Task<bool> LeaveReview(ReviewDto review)
        {
            if (await _repository.LeaveReview(review)) return true;
            throw new ArgumentException("Shop could not be reviewed!");
        }
    }
}
