using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IShopRepository
    {
        public Task<List<ShopCard>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page, bool favorite);
        public int ShopPages();
        public Task<ShopInfo> ShopDetails(int shopId, int userId);
        public Task<LikedShops> GetLike(int shopId, int userId);
        public Task<bool> LikeShop(int shopId, int userId);
        public Task<bool> DislikeShop(int shopId, int userId);
        public Task<bool> LeaveReview(ReviewDto review);
        public Task<bool> ChangeShopPhoto(int id, string path);
        public Task<List<ShopReviewExtended>> GetShopReviews(int shopId, int page);
        public Task<bool> InsertShop(Shop shop);
        public Task<bool> InsertWorkingHours(List<WorkingHoursDto> workingHours, int shopId);
        public Task<bool> InsertShopCategories(List<int> shopCategories, int shopId);
    }
}
