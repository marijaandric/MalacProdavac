using back.DAL.Contexts;
using back.Models;

namespace back.DAL.Repositories
{
    public class ShopRepository : IShopRepository
    {
        Context _context;
        int numberOfItems = 10;

        public ShopRepository(Context context)
        {
            _context = context;
        }

        public Task<List<Shop>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page)
        {
            throw new NotImplementedException();
        }

        public int ShopPages()
        {
            return (int)Math.Ceiling((double)_context.Shop.Count()/numberOfItems);
        }
    }
}
