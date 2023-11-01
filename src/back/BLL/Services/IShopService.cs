using back.Models;

namespace back.BLL.Services
{
    public interface IShopService
    {
        public Task<List<Shop>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page);
        public int ShopPages();
    }
}
