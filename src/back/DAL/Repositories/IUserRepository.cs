using back.BLL.Dtos;
using back.BLL.Dtos.HelpModels;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IUserRepository
    {
        public Task<MyProfileInfo> GetMyProfile(int id);
        public Task<PublicProfileInfo> GetPublicProfile(int targetId);
        public Task<List<ProductReviewExtended>> GetProductReviewsOfAShop(int userId, int shopId, int page);
        public Task<ShopReviewExtended> GetReviewOfAShop(int userId, int shopId);
    }
}
