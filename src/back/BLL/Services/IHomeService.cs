using back.BLL.Dtos;
using back.Models;

namespace back.BLL.Services
{
    public interface IHomeService
    {
        public Task<List<Category>> GetCategories();
        public Task<bool> SaveChosenCategories(ChosenCategoriesDto categoriesDto);
        public Task<bool> UpdateChosenCategories(ChosenCategoriesDto categoriesDto);
        public Task<List<Category>> GetChosenCategories(int id);
        public Task<List<ProductCard>> GetHomeProducts(int id);
        public Task<List<ShopCard>> GetHomeShops(int id);
    }
}
