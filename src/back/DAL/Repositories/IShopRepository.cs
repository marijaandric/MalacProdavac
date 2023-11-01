using back.Models;

namespace back.DAL.Repositories
{
    public interface IShopRepository
    {
        public Task<List<Shop>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page);
        public int ShopPages();
    }
}
