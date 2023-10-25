using back.BLL.Dtos;
using back.Models;

namespace back.BLL.Services
{
    public interface IProductService
    {
        public Task<List<Category>> GetCategories();
        public Task<bool> SaveChosenCategories(ChosenCategoriesDto categoriesDto);
        public Task<List<Category>> GetChosenCategories(int id);
        public Task<List<Product>> GetHomeProducts(int id);
    }
}
