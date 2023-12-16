using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IUserRepository
    {
        public Task<MyProfileInfo> GetMyProfile(int id);
        public Task<PublicProfileInfo> GetPublicProfile(int targetId);
        public Task<List<ProductReviewExtended>> GetProductReviewsOfAShop(int userId, int shopId, int page);
        public Task<ShopReviewExtended> GetReviewOfAShop(int userId, int shopId);
        public Task<List<ProductCard>> GetReviewedProductsOfAShop(int userId, int shopId, int page);
        public Task<string> GetUsername(int userId);
        public Task<List<User>> GetDeliveryPeople();
        public Task<bool> RateUser(Rating rating);
    }
}
