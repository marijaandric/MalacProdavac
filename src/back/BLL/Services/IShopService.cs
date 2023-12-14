using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
using back.Models;

namespace back.BLL.Services
{
    public interface IShopService
    {
        public Task<List<ShopCard>> GetShops(int? userId, List<int>? categories, int? rating, bool? open, int? range, string? location, int sort, string? search, int page, bool? favorite, float? currLat, float? currLong);
        public Task<int> ShopPages(int? userId, List<int>? categories, int? rating, bool? open, int? range, string? location, string? search, bool? favorite, float? currLat, float? currLong);
        public Task<ShopInfo> ShopDetails(int shopId, int userId);
        public Task<LikedShops> GetLike(int shopId, int userId);
        public Task<bool> ToggleLike(int shopId, int userId);
        public Task<bool> LeaveReview(ReviewDto review);
        public Task<List<ShopReviewExtended>> GetShopReviews(int shopId, int page);
        public Task<int> InsertShop(ShopDto shop);
        public Task<bool> EditShop(EditShopDto shop);
        public Task<bool> DeleteShop(int shopId);
        public Task<bool> InsertProductDisplay(ProductDisplayDto productDisplay);
        public Task<bool> EditProductDisplay(EditProductDisplayDto productDisplay);
        public Task<bool> DeleteProductDisplay(int id);
        public Task<ProductDisplayInfo> GetProductDisplay(int id);
        public Task<int> GetShopId(int userId);
        public Task<List<ShopCheckoutCard>> GetShopsForCheckout(List<int> shopIds);
    }
}
