using back.BLL.Dtos;
using back.BLL.Dtos.HelpModels;
using back.DAL.Repositories;
using back.Models;

namespace back.BLL.Services
{
    public class UserService : IUserService
    {
        IUserRepository _repository;
        public UserService(IUserRepository userRepository)
        {
            _repository = userRepository;
        }

        public async Task<MyProfileInfo> GetMyProfile(int id)
        {
            MyProfileInfo profile = await _repository.GetMyProfile(id);
            if (profile == null) throw new ArgumentException("No profile found!");

            return profile;
        }

        public async Task<PublicProfileInfo> GetPublicProfile(int targetId)
        {
            PublicProfileInfo profile = await _repository.GetPublicProfile(targetId);
            if (profile == null) throw new ArgumentException("No profile found!");

            return profile;
        }

        public async Task<List<ProductReviewExtended>> GetProductReviewsOfAShop(int userId, int shopId, int page)
        {
            List<ProductReviewExtended> reviews = await _repository.GetProductReviewsOfAShop(userId, shopId, page);
            if (reviews.Count == 0) throw new ArgumentException("No reviews found!");

            return reviews;
        }

        public async Task<ShopReviewExtended> GetReviewOfAShop(int userId, int shopId)
        {
            ShopReviewExtended review = await _repository.GetReviewOfAShop(userId, shopId);
            if (review == null) throw new ArgumentException("No review found!");

            return review;
        }

        public async Task<List<ProductCard>> GetReviewedProductsOfAShop(int userId, int shopId, int page)
        {
            List<ProductCard> products = await _repository.GetReviewedProductsOfAShop(userId, shopId, page);
            if (products.Count == 0) throw new ArgumentException("No products found!");

            return products;
        }
    }
}
