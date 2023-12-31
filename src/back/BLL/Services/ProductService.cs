﻿using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
using back.DAL.Models;
using back.DAL.Repositories;
using back.Models;

namespace back.BLL.Services
{
    public class ProductService : IProductService
    {
        IProductRepository _repository;
        INotificationRepository _notificationRepository;
        IShopRepository _shopRepository;

        public ProductService(IProductRepository repository, INotificationRepository notificationRepository, IShopRepository shopRepository)
        {
            _repository = repository;
            _notificationRepository = notificationRepository;
            _shopRepository = shopRepository;
        }

        string imagesFolderPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "..\\..\\..\\images");

        public async Task<List<ProductCard>> GetProducts(int? userId, List<int>? categories, int? rating, bool? open, int? range, string? location, int sort, string? search, int page, int? specificShopId, bool? favorite, float? currLat, float? currLong)
        {
            List<ProductCard> products = await _repository.GetProducts(userId, categories, rating, open, range, location, sort, search, page, specificShopId, favorite, currLat, currLong);
            if (products.Count == 0) throw new ArgumentException("No products found.");
            return products;
        }

        public async Task<int> ProductPages(int? userId, List<int>? categories, int? rating, bool? open, int? range, string? location, string? search, int? specificShopId, bool? favorite, float? currLat, float? currLong)
        {
            int total = await _repository.ProductPages(userId, categories, rating, open, range, location, search, specificShopId, favorite, currLat, currLong);
            if (total == 0) throw new ArgumentException("No pages.");

            return total;
        }

        public async Task<ProductInfo> ProductDetails(int productId, int userId)
        {
            ProductInfo productInfo = await _repository.ProductDetails(productId, userId);
            if (productInfo == null) throw new ArgumentException("No product found!");

            return productInfo;
        }

        public async Task<List<ProductReviewExtended>> GetProductReviews(int productId, int page)
        {
            List<ProductReviewExtended> reviews = await _repository.GetProductReviews(productId, page);
            if (reviews.Count == 0) throw new ArgumentException("No reviews found!");

            return reviews;
        }
        public async Task<bool> ToggleLike(int productId, int userId)
        {
            if (await _repository.GetLike(productId, userId) == null)
            {
                if (!await _repository.LikeProduct(productId, userId)) throw new ArgumentException("Product could not be liked!");
                return true;
            }

            if (!await _repository.DislikeProduct(productId, userId)) throw new ArgumentException("Product could not be disliked!");
            return true;
        }

        public async Task<bool> AddToCart(int productId, int userId, int quantity)
        {
            var item = await _repository.GetCartItem(productId, userId);
            
            if (item != null)
            {
                if (await _repository.UpdateCart(productId, userId, quantity)) return true;
                throw new ArgumentException("Quantity could not be updated!");
            }

            if (await _repository.AddToCart(productId, userId, quantity)) return true;
            throw new ArgumentException("Item could not be added!");
        }

        public async Task<bool> RemoveFromCart(int productId, int userId)
        {
            if (await _repository.RemoveFromCart(productId, userId)) return true;
            throw new ArgumentException("Item could not be removed!");
        }

        public async Task<bool> LeaveReview(ReviewDto review)
        {
            if (await _repository.LeaveReview(review)) return true;
            throw new ArgumentException("Product could not be reviewed!");
        }

        public async Task<bool> LeaveQuestion(QnADto question)
        {
            if (await _repository.LeaveQuestion(question)) return true;
            throw new ArgumentException("Question could not be saved!");
        }

        public async Task<bool> DeleteProductPhoto(int photoId)
        {
            string img = await _repository.DeleteProductPhoto(photoId);
            if (img == null) throw new ArgumentException("No image found!");

            string path = Path.Combine(imagesFolderPath, img);

            if (File.Exists(path))
            {
                File.Delete(path);
                return true;
            }

            return false;
        }

        public async Task<int> AddProduct(ProductDto product)
        {
            Product newProduct = new Product
            {
                CategoryId = product.CategoryId,
                Description = product.Description,
                MetricId = product.MetricId,
                Price = product.Price,
                Name = product.Name,
                SaleMessage = product.SaleMessage,
                SaleMinQuantity = product.SaleMinQuantity,
                SalePercentage = product.SalePercentage,
                ShopId = product.ShopId,
            };

            if (await _repository.AddProduct(newProduct) == 0) throw new ArgumentException("The product could not be added!");

            foreach (StockDto size in product.Sizes)
            {
                if (!await _repository.AddProductSize(new ProductSize { ProductId = newProduct.Id, SizeId = size.SizeId, Stock = size.Quantity }))
                {
                    await _repository.DeleteProduct(newProduct.Id);
                    throw new ArgumentException("Error on saving sizes, deleting product!");
                }
            }

            List<int> usersToNotify = await _repository.GetShopFollowers(product.ShopId);

            foreach(int user in usersToNotify)
            {
                string shopName = (await _shopRepository.GetShop(newProduct.ShopId)).Name;
                if (await _notificationRepository.InsertNotification(user, 2, "New product!", shopName + " has added new product: " + newProduct.Name + ".\nTap to learn more.", newProduct.Id)) Console.WriteLine("Notification sent!");
            }
            

            return newProduct.Id;
        }

        public async Task<bool> EditProduct(EditProductDto productDto)
        {
            var oldSizes = await _repository.GetProductSizes(productDto.Id);
            bool ind = false;

            if (productDto.Sizes != null)
            {

                var joined = oldSizes.Join(productDto.Sizes, old => old.SizeId, edited => edited.SizeId, (old, edited) => new { old, edited });
                foreach (var size in joined)
                {
                    if (size.old.Stock == 0 && size.edited.Quantity > 0)
                    {
                        Console.WriteLine($"Old Stock: {size.old.Stock}, Edited Quantity: {size.edited.Quantity}");

                        ind = true;
                        break;
                    }
                }
            }

            if (!await _repository.EditProduct(productDto)) throw new ArgumentException("Product could not be saved!");

            if (productDto.Sizes != null && productDto.Sizes.Count > 0)
            {
                if (!await _repository.EditProductSizes(productDto.Id, productDto.Sizes.Select(x => new ProductSize { ProductId = productDto.Id, SizeId = x.SizeId, Stock = x.Quantity}).ToList())) throw new ArgumentException("Error on updating sizes!");
            }

            Product editedProduct = await _repository.GetProduct(productDto.Id);

            if (productDto.SalePercentage != null && productDto.SalePercentage > 0)
            {
                List<int> usersToNotify = await _repository.GetShopFollowers(editedProduct.ShopId);

                foreach (int user in usersToNotify)
                {
                    string shopName = (await _shopRepository.GetShop(editedProduct.ShopId)).Name;

                    if (editedProduct.SaleMinQuantity > 1)
                        if (await _notificationRepository.InsertNotification(user, 2, editedProduct.Name + " on sale!", shopName + " is having " + editedProduct.SalePercentage + "% off on all " + editedProduct.Name.ToLower() + " orders over " + editedProduct.SaleMinQuantity + " " + await _repository.GetMetric(editedProduct.MetricId) + "s.\nTap to learn more.", editedProduct.Id)) Console.WriteLine("Notification sent!");
                    else if (editedProduct.SaleMinQuantity == 1)
                        if (await _notificationRepository.InsertNotification(user, 2, editedProduct.Name + " on sale!", shopName + " is having " + editedProduct.SalePercentage + "% off on all " + editedProduct.Name.ToLower() + " orders over 1 " + await _repository.GetMetric(editedProduct.MetricId) + ".\nTap to learn more.", editedProduct.Id)) Console.WriteLine("Notification sent!");
                }
            }

            if (ind)
            {
                List<int> usersToNotify = await _repository.GetShopFollowers(editedProduct.ShopId);
                var subscribers = await _repository.GetSubscriptions(productDto.Id);
                usersToNotify.AddRange(subscribers.Select(x => x.UserId));
                usersToNotify = usersToNotify.Distinct().ToList();


                foreach (int user in usersToNotify)
                {
                    string shopName = (await _shopRepository.GetShop(editedProduct.ShopId)).Name;
                    if (await _notificationRepository.InsertNotification(user, 2, "Back in stock!", editedProduct.Name + " is back in stock in " + shopName + ".\nTap to learn more.", editedProduct.Id)) Console.WriteLine("Notification sent!");
                }

                await _repository.DeleteSubscriptions(subscribers);
            }

            return true;
        }

        public async Task<bool> DeleteProduct(int productId)
        {
            if (!await _repository.DeleteProduct(productId)) throw new ArgumentException("Product could not be deleted!");
            return true;
        }

        public async Task<bool> Subscribe(ProductSubscriptionDto dto)
        {
            return await _repository.Subscribe(new ProductSubscription {  ProductId = dto.ProductId, UserId = dto.UserId });
        }
    }
}
