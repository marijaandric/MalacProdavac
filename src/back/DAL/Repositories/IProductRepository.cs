using back.Models;

namespace back.DAL.Repositories
{
    public interface IProductRepository
    {
        public Task<List<Product>> GetProducts(List<int> categories, int rating, bool open, int range, string location);
    }
}
