using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
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

        public async Task<List<ShopCard>> GetShops(int userId, List<int>? categories, int? rating, bool? open, int? range, string? location, int sort, string? search, int page, bool? favorite, float? currLat, float? currLong)
        {
            List<ShopCard> shops = await _repository.GetShops(userId, categories, rating, open, range, location, sort, search, page, favorite, currLat, currLong);
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

        public async Task<LikedShops> GetLike(int shopId, int userId)
        {
            LikedShops like = await _repository.GetLike(shopId, userId);
            if (like == null) throw new ArgumentException("No like found!");

            return like;
        }
        public async Task<bool> ToggleLike(int shopId, int userId)
        {
            if (await _repository.GetLike(shopId, userId) == null)
            {
                if (!await _repository.LikeShop(shopId, userId)) throw new ArgumentException("Shop could not be liked!");
                return true;
            }

            if (!await _repository.DislikeShop(shopId, userId)) throw new ArgumentException("Shop could not be disliked!");
            return true;
        }

        public async Task<bool> LeaveReview(ReviewDto review)
        {
            if (await _repository.LeaveReview(review)) return true;
            throw new ArgumentException("Shop could not be reviewed!");
        }

        public async Task<List<ShopReviewExtended>> GetShopReviews(int shopId, int page)
        {
            List<ShopReviewExtended> reviews = await _repository.GetShopReviews(shopId, page);
            if (reviews.Count == 0) throw new ArgumentException("No reviews found!");

            return reviews;
        }

        public async Task<bool> InsertShop(ShopDto shop)
        {
            (double, double) coords = await HelperService.GetCoordinates(shop.Address);
            Shop s = new Shop
            {
                Address = shop.Address,
                Image = shop.Image,
                Name = shop.Name,
                OwnerId = shop.OwnerId,
                PIB = shop.PIB,
                Latitude = (float)coords.Item1,
                Longitude = (float)coords.Item2
            };

            if (!await _repository.InsertShop(s)) throw new ArgumentException("Shop couldn't be saved!");
            if (!await _repository.InsertShopCategories(shop.Categories, s.Id))
            {
                await _repository.DeleteShop(s.Id);
                throw new ArgumentException("Categories could not be saved!");
            }
            if (!await _repository.InsertWorkingHours(shop.WorkingHours, s.Id))
            {
                await _repository.DeleteShop(s.Id);
                throw new ArgumentException("Working hours could not be saved!");
            }

            return true;
        }

        public async Task<bool> EditShop(EditShopDto shop)
        {

            if (!await _repository.EditShop(shop)) throw new ArgumentException("Could not change shop info!");
            
            if (shop.WorkingHours != null && !await _repository.EditWorkingHours(shop.WorkingHours, shop.Id)) throw new ArgumentException("Working hours could not be changed!");

            if (shop.Categories != null)
            {
                List<int> existing = (await _repository.GetShopCategories(shop.Id)).Select(x => x.CategoryId).ToList();
                List<int> newCategories = shop.Categories.Where(x => !existing.Contains(x)).ToList();
                List<int> toDelete = existing.Where(x => !shop.Categories.Contains(x)).ToList();

                if (toDelete.Count > 0 && !await _repository.DeleteShopCategories(toDelete, shop.Id)) throw new ArgumentException("Database error on deleting deselected categories!");
                if (newCategories.Count > 0 && !await _repository.InsertShopCategories(newCategories, shop.Id)) throw new ArgumentException("Database error on inserting new categories!");
            }
            
            return true;
        }

        public async Task<bool> DeleteShop(int shopId)
        {
            if (!await _repository.DeleteShop(shopId)) throw new ArgumentException("Shop couldn't be deleted!");
            return true;
        }
    }
}
