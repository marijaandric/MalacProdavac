using back.BLL.Dtos;
using back.BLL.Dtos.HelpModels;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IProductRepository
    {
        public Task<List<ProductCard>> GetProducts(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page, int specificShopId);
        public int ProductPages();
        public Task<ProductInfo> ProductDetails(int productId, int userId);
        public Task<List<ProductReviewExtended>> GetProductReviews(int productId, int page);
        public Task<bool> LikeProduct(int productId, int userId);
        public Task<bool> DislikeProduct(int productId, int userId);
        public Task<LikedProducts> GetLike(int productId, int userId);
        public Task<bool> AddToCart(int productId, int userId, int quantity);
        public Task<bool> RemoveFromCart(int productId, int userId);
        public Task<Cart> GetCartItem(int productId, int userId);
        public Task<bool> UpdateCart(int productId, int userId, int quantity);
        public Task<bool> LeaveReview(ReviewDto review);
        public Task<bool> LeaveQuestion(QnADto question);
    }
}
