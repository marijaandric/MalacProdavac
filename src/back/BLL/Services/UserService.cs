using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
using back.DAL.Repositories;
using back.Models;

namespace back.BLL.Services
{
    public class UserService : IUserService
    {
        IUserRepository _repository;
        IOrderRepository _orderRepository;
        public UserService(IUserRepository userRepository, IOrderRepository orderRepository)
        {
            _repository = userRepository;
            _orderRepository = orderRepository;
        }

        public async Task<MyProfileInfo> GetMyProfile(int id)
        {
            MyProfileInfo profile = await _repository.GetMyProfile(id);
            if (profile == null) throw new ArgumentException("No profile found!");
            if (profile.RoleId == 3) profile.MoneyEarned = await _orderRepository.TotalProfit(id);
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

        public async Task<bool> RateUser(RatingDto dto)
        {
            return await _repository.RateUser(new Rating
            {
                RaterId = dto.RaterId,
                RatedId = dto.RatedId,
                Communication = dto.Communication,
                Reliability = dto.Reliability,
                OverallExperience = dto.OverallExperience,
                Average = (dto.Communication + dto.Reliability + dto.OverallExperience) / 3
            }) ;
        }
    }
}
