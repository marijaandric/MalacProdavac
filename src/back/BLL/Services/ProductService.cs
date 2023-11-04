using back.BLL.Dtos;
using back.DAL.Repositories;
using back.Models;

namespace back.BLL.Services
{
    public class ProductService : IProductService
    {
        IProductRepository _repository;

        public ProductService(IProductRepository repository)
        {
            _repository = repository;
        }

        public async Task<List<ProductCard>> GetProducts(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page)
        {
            List<ProductCard> products = await _repository.GetProducts(userId, categories, rating, open, range, location, sort, search, page);
            if (products.Count == 0) throw new ArgumentException("No products found.");
            return products;
        }

        public int ProductPages()
        {
            int total = _repository.ProductPages();
            if (total == 0) throw new ArgumentException("No pages.");

            return total;
        }

        public async Task<ProductInfo> ProductDetails(int productId, int userId)
        {
            ProductInfo productInfo = await _repository.ProductDetails(productId, userId);
            if (productInfo == null) throw new ArgumentException("No product found!");

            return productInfo;
        }
    }
}
