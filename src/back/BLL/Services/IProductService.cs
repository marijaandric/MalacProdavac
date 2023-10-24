using back.BLL.Dtos;
using back.Models;

namespace back.BLL.Services
{
    public interface IProductService
    {
        public Task<List<Category>> GetCategories();
        public Task<bool> SaveChosenCategories(ChosenCategoriesDto categoriesDto);
    }
}
