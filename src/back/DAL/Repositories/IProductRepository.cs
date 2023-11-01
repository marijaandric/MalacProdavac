using back.Models;

namespace back.DAL.Repositories
{
    public interface IProductRepository
    {
        public Task<List<Product>> GetProducts(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search);
    }
}
