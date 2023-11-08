using back.BLL.Dtos;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IShopRepository
    {
        public Task<List<ShopCard>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page);
        public int ShopPages();
        public Task<ShopInfo> ShopDetails(int shopId, int userId);
        public Task<LikedShops> GetLike(int shopId, int userId);
        public Task<bool> LikeShop(int shopId, int userId);
        public Task<bool> DislikeShop(int shopId, int userId);
        public Task<bool> LeaveReview(ReviewDto review);
    }
}
