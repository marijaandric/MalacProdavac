using back.DAL.Repositories;
using back.Models;

namespace back.BLL.Services
{
    public class ShopService : IShopService
    {
        IShopRepository _repository;
        public ShopService(IShopRepository repository)
        {
            _repository = repository;
        }

        public async Task<List<Shop>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page)
        {
            List<Shop> shops = await _repository.GetShops(userId, categories, rating, open, range, location, sort, search, page);
            if (shops.Count == 0) throw new ArgumentException("No shops found.");
            return shops;
        }

        public int ShopPages()
        {
            int total = _repository.ShopPages();
            if (total == 0) throw new ArgumentException("No pages.");

            return total;
        }
    }
}
