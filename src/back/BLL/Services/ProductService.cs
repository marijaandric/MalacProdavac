using back.BLL.Dtos;
using back.DAL.Repositories;
using back.Models;

namespace back.BLL.Services
{
    public class ProductService : IProductService
    {
        IProductRepository _repository;
        public ProductService(IProductRepository repository)
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
    }
}
