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

        public async Task<List<Product>> GetProducts(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int numberOfItems, int page)
        {
            List<Product> products = await _repository.GetProducts(userId, categories, rating, open, range, location, sort, search, numberOfItems, page);
            if (products.Count == 0) throw new ArgumentException("No products found.");
            return products;
        }
    }
}
