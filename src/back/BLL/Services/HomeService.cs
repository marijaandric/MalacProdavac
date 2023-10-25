using back.BLL.Dtos;
using back.DAL.Repositories;
using back.Models;

namespace back.BLL.Services
{
    public class HomeService : IHomeService
    {
        IHomeRepository _repository;
        public HomeService(IHomeRepository repository)
        {
            _repository = repository;
        }

        public async Task<List<Category>> GetCategories()
        {
            List<Category> categories = await _repository.GetCategories();
            if (categories.Count == 0) throw new ArgumentException("No categories found!");
            return categories;
        }

        public async Task<bool> SaveChosenCategories(ChosenCategoriesDto categoriesDto)
        {
            if (categoriesDto.CategoryIds.Count < 2) throw new ArgumentException("You need to choose at least 2 categories.");
            if (!await _repository.SaveChosenCategories(categoriesDto.UserId, categoriesDto.CategoryIds)) throw new ArgumentException("Database error!");
            return true;
        }

        public async Task<List<Category>> GetChosenCategories(int id)
        {
            List<Category> categories = await _repository.GetChosenCategories(id);
            if (categories.Count == 0) throw new ArgumentException("No categories found!");
            return categories;
        }

        public async Task<List<Product>> GetHomeProducts(int id)
        {
            List<Product> products = await _repository.GetHomeProducts(id);
            if (products.Count == 0) throw new ArgumentException("No products found!");
            return products;
        }

        public async Task<List<Shop>> GetHomeShops(int id)
        {
            List<Shop> shops = await _repository.GetHomeShops(id);
            if (shops.Count == 0) throw new ArgumentException("No shops found!");
            return shops;
        }
    }
}
