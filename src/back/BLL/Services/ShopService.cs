using back.BLL.Dtos;
using back.DAL.Repositories;

namespace back.BLL.Services
{
    public class ShopService : IShopService
    {
        IShopRepository _repository;
        public ShopService(IShopRepository repository)
        {
            _repository = repository;
        }

        public async Task<List<ShopCard>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page)
        {
            List<ShopCard> shops = await _repository.GetShops(userId, categories, rating, open, range, location, sort, search, page);
            if (shops.Count == 0) throw new ArgumentException("No shops found.");
            return shops;
        }

        public int ShopPages()
        {
            int total = _repository.ShopPages();
            if (total == 0) throw new ArgumentException("No pages.");

            return total;
        }

        public async Task<ShopInfo> ShopDetails(int shopId, int userId)
        {
            ShopInfo shop = await _repository.ShopDetails(shopId, userId);
            if (shop == null) throw new ArgumentException("No shop found!");

            return shop;
        }
    }
}
