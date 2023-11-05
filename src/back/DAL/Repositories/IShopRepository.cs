using back.BLL.Dtos;

namespace back.DAL.Repositories
{
    public interface IShopRepository
    {
        public Task<List<ShopCard>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page);
        public int ShopPages();
        public Task<ShopInfo> ShopDetails(int shopId, int userId);
    }
}
