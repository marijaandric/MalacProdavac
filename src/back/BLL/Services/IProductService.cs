using back.BLL.Dtos;
using back.BLL.Dtos.HelpModels;

namespace back.BLL.Services
{
    public interface IProductService
    {
        public Task<List<ProductCard>> GetProducts(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page, int specificShopId, bool favorite);
        public int ProductPages();
        public Task<ProductInfo> ProductDetails(int productId, int userId);
        public Task<List<ProductReviewExtended>> GetProductReviews(int productId, int page);
        public Task<bool> ToggleLike(int productId, int userId);
        public Task<bool> AddToCart(int productId, int userId, int quantity);
        public Task<bool> RemoveFromCart(int productId, int userId);
        public Task<bool> LeaveReview(ReviewDto review);
        public Task<bool> LeaveQuestion(QnADto question);
        public Task<bool> DeleteProductPhoto(int photoId);
    }
}
