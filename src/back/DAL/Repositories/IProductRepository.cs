using back.Models;

namespace back.DAL.Repositories
{
    public interface IProductRepository
    {
        public Task<List<Category>> GetCategories();
        public Task<bool> SaveChosenCategories(int userId, List<int> categoryIds);
        public Task<List<Category>> GetChosenCategories(int id);
        public Task<List<Product>> GetHomeProducts(int id);
    }
}
