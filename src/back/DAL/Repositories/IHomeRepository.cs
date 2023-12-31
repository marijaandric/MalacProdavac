﻿using back.BLL.Dtos.Cards;
using back.Models;

namespace back.DAL.Repositories
{
    public interface IHomeRepository
    {
        public Task<List<Category>> GetCategories();
        public Task<bool> SaveChosenCategories(int userId, List<int> categoryIds);
        public Task<bool> DeleteChosenCategories(int userId, List<int> categoryIds);
        public Task<List<Category>> GetChosenCategories(int id);
        public Task<List<ProductCard>> GetHomeProducts(int id);
        public Task<List<ShopCard>> GetHomeShops(int id);
    }
}
