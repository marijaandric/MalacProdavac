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

        public Task<List<Shop>> GetShops(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page)
        {
            throw new NotImplementedException();
        }

        public int ShopPages()
        {
            int total = _repository.ShopPages();
            if (total == 0) throw new ArgumentException("No pages.");

            return total;
        }
    }
}
