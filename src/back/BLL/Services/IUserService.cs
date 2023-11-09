using back.BLL.Dtos;
using back.BLL.Dtos.HelpModels;

namespace back.BLL.Services
{
    public interface IUserService
    {
        public Task<MyProfileInfo> GetMyProfile(int id);
        public Task<PublicProfileInfo> GetPublicProfile(int targetId);
        public Task<List<ProductReviewExtended>> GetProductReviewsOfAShop(int userId, int shopId, int page);
        public Task<ShopReviewExtended> GetReviewOfAShop(int userId, int shopId);
        public Task<List<ProductCard>> GetReviewedProductsOfAShop(int userId, int shopId, int page);
    }
}
