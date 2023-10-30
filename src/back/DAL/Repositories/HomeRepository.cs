﻿using back.DAL.Contexts;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class HomeRepository : IHomeRepository
    {
        Context _context;
        public HomeRepository(Context context)
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

        public async Task<List<Category>> GetChosenCategories(int id)
        {
            return await _context.ChosenCategories.Where(x => x.UserId == id).Join(_context.Categories, ccat => ccat.CategoryId, cat => cat.Id, (ccat, cat) => cat).ToListAsync();
        }

        public async Task<List<Product>> GetHomeProducts(int id)
        {
            List<Category> categories = await GetChosenCategories(id);
            int take;

            if (categories.Count > 6) take = 1;
            else take = 2;

            return categories.SelectMany(category => _context.Products.Where(x => x.Category == category).Take(take)).ToList();
        }

        public async Task<List<Shop>> GetHomeShops(int id)
        {
            List<int> categories = (await GetChosenCategories(id)).Select(x => x.Id).ToList();
            int take;

            if (categories.Count > 6) take = 1;
            else take = 2;

            List<int> shopIds = categories.SelectMany(category => _context.ShopCategories.Where(x => x.CategoryId == category).Take(take)).Select(x => x.ShopId).Distinct().ToList();
            return _context.Shop.Where(x => shopIds.Contains(x.Id)).ToList();

        }
    }
}
