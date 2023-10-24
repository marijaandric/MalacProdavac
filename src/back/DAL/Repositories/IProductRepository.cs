using back.Models;

namespace back.DAL.Repositories
{
    public interface IProductRepository
    {
        public Task<List<Category>> GetCategories();
        public Task<bool> SaveChosenCategories(int userId, List<int> categoryIds);
    }
}
