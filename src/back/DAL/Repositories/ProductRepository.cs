using back.DAL.Contexts;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class ProductRepository : IProductRepository
    {
        Context _context;
        public ProductRepository(Context context)
        {
            _context = context;
        }

        public async Task<bool> Save()
        {
            var save = await _context.SaveChangesAsync();
            return save > 0;
        }

        public async Task<List<Category>> GetCategories()
        {
            return await _context.Categories.ToListAsync();
        }

        public async Task<bool> SaveChosenCategories(int userId, List<int> categoryIds)
        {
            foreach (int categoryId in categoryIds) {
                ChosenCategory cat = new ChosenCategory();
                cat.CategoryId = categoryId;
                cat.UserId = userId;
                await _context.ChosenCategories.AddAsync(cat);
            }

            return await Save();
        }

    }
}
