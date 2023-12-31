﻿using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
using back.DAL.Models;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IProductRepository
    {
        public Task<List<ProductCard>> GetProducts(int? userId, List<int>? categories, int? rating, bool? open, int? range, string? location, int sort, string? search, int page, int? specificShopId, bool? favorite, float? currLat, float? currLong);
        public Task<int> ProductPages(int? userId, List<int>? categories, int? rating, bool? open, int? range, string? location, string? search, int? specificShopId, bool? favorite, float? currLat, float? currLong);
        public Task<ProductInfo> ProductDetails(int productId, int? userId);
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
        public Task<bool> AddProductPhoto(int id, string path);
        public Task<string> DeleteProductPhoto(int photoId);
        public Task<int> AddProduct(Product product);
        public Task<bool> AddProductSizes(List<ProductSize> sizes);
        public Task<List<ProductSize>> GetProductSizes(int productId);
        public Task<bool> EditProduct(EditProductDto product);
        public Task<bool> EditProductSizes(int productId, List<ProductSize> sizes);
        public Task<bool> AddProductSize(ProductSize ps);
        public Task<bool> DeleteProduct(int productId);
        public Task<List<int>> GetShopFollowers(int shopId);
        public Task<Product> GetProduct(int id);
        public Task<string> GetMetric(int metricId);
        public Task<List<Metric>> GetMetrics();
        public Task<List<Size>> GetSizes();
        public Task<bool> Subscribe(ProductSubscription ps);
        public Task<List<ProductSubscription>> GetSubscriptions(int productId);
        public Task<bool> DeleteSubscriptions(List<ProductSubscription> subscriptions);
    }
}
