using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface IProductService
    {
        public Task<List<ProductCard>> GetProducts(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page);
        public int ProductPages();
        public Task<ProductInfo> ProductDetails(int productId, int userId);
    }
}
