using back.BLL.Dtos;
using back.DAL.Repositories;
using back.Models;

namespace back.BLL.Services
{
    public interface IProductService
    {
        public Task<List<Product>> GetProducts(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search);
    }
}
