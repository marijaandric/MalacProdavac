using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface IShopService
    {
        public Task<List<ShopCard>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page);
        public int ShopPages();
        public Task<ShopInfo> ShopDetails(int shopId, int userId);
    }
}
