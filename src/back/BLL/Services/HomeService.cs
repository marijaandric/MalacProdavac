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

        public async Task<bool> UpdateChosenCategories(ChosenCategoriesDto categoriesDto)
        {
            List<int> existing = (await GetChosenCategories(categoriesDto.UserId)).Select(x => x.Id).ToList();
            List<int> newCategories = categoriesDto.CategoryIds.Where(x => !existing.Contains(x)).ToList();
            List<int> toDelete = existing.Where(x => !categoriesDto.CategoryIds.Contains(x)).ToList();

            if (categoriesDto.CategoryIds.Count < 2) throw new ArgumentException("You need to choose at least 2 categories.");
            if (toDelete.Count > 0 && !await _repository.DeleteChosenCategories(categoriesDto.UserId, toDelete)) throw new ArgumentException("Database error on deleting deselected items!");
            if (newCategories.Count > 0 && !await _repository.SaveChosenCategories(categoriesDto.UserId, newCategories)) throw new ArgumentException("Database error on inserting new values!");
            return true;
        }

        public async Task<List<Category>> GetChosenCategories(int id)
        {
            List<Category> categories = await _repository.GetChosenCategories(id);
            if (categories.Count == 0) throw new ArgumentException("No categories found!");
            return categories;
        }

        public async Task<List<ProductCard>> GetHomeProducts(int id)
        {
            List<ProductCard> products = await _repository.GetHomeProducts(id);
            if (products.Count == 0) throw new ArgumentException("No products found!");
            return products;
        }

        public async Task<List<ShopCard>> GetHomeShops(int id)
        {
            List<ShopCard> shops = await _repository.GetHomeShops(id);
            if (shops.Count == 0) throw new ArgumentException("No shops found!");
            return shops;
        }
    }
}
