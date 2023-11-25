using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IShopRepository
    {
        public Task<List<ShopCard>> GetShops(int? userId, List<int>? categories, int? rating, bool? open, int? range, string? location, int sort, string? search, int page, bool? favorite, float? currLat, float? currLong);
        public Task<int> ShopPages(int? userId, List<int>? categories, int? rating, bool? open, int? range, string? location, string? search, bool? favorite, float? currLat, float? currLong);
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
        public Task<bool> EditShop(EditShopDto shop);
        public Task<bool> EditWorkingHours(List<WorkingHoursDto> workingHours, int shopId);
        public Task<bool> DeleteShopCategories(List<int> shopCategories, int shopId);
        public Task<List<ShopCategory>> GetShopCategories(int shopId);
        public Task<bool> DeleteShop(int shopId);
        public Task<Shop> GetShop(int id);
        public Task<bool> InsertProductDisplay(ProductDisplay productDisplay);
        public Task<List<int>> GetNearbyUsers(float latitude, float longitude, int ownerId);
        public Task<bool> EditProductDisplay(EditProductDisplayDto productDisplayDto);
        public Task<ProductDisplay> GetProductDisplay(int id);
        public Task<bool> DeleteProductDisplay(int id);
    }
}
