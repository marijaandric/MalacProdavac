using System;
using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.HelpModels;
using back.BLL.Dtos.Infos;
using back.DAL.Contexts;
using back.DAL.Models;
using back.Models;
using FirebaseAdmin.Messaging;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class ProductRepository : IProductRepository
    {
        Context _context;
        FirebaseMessaging _firebaseMessaging;
        int numberOfItems = 10;
        int numberOfReviews = 3;
        public ProductRepository(Context context, FirebaseMessaging firebaseMessaging)
        {
            _context = context;
            _firebaseMessaging = firebaseMessaging;
        }

        #region filterHelp
        public List<ProductCard> SortProducts(int sort, List<ProductCard> products)
        {
            switch (sort)
            {
                case 1:
                    return products.OrderBy(x => x.Price).ToList();
                case 2:
                    return products.OrderByDescending(x => x.Price).ToList();
                case 3:
                    return products.OrderBy(x => x.Name).ToList();
                case 4:
                    return products.OrderByDescending(x => x.Name).ToList();
                case 5:
                    return products.OrderBy(x => x.Rating).ToList();
                case 6:
                    return products.OrderByDescending(x => x.Rating).ToList();
                default:
                    return products.OrderBy(x => x.Id).ToList();
            }
        }

        public static double CalculateDistance(float lat1, float lon1, float lat2, float lon2)
        {
            const double radiusOfEarth = 6371;

            // degrees -> radians
            double dLat = Math.PI / 180.0 * (lat2 - lat1);
            double dLon = Math.PI / 180.0 * (lon2 - lon1);

            // Haversine formula
            double a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                Math.Cos(Math.PI / 180.0 * lat1) * Math.Cos(Math.PI / 180.0 * lat2) *
                Math.Sin(dLon / 2) * Math.Sin(dLon / 2);
            double c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));

            double distance = radiusOfEarth * c;

            return distance;
        }

        #endregion

        public async Task<List<ProductCard>> GetUnsortedProducts(int? userId, List<int>? categories, int? rating, bool? open, int? range, string? location, string? search, int? specificShopId, bool? favorite, float? currLat, float? currLong)
        {
            List<ProductCard> products;
            int usersShop = -1;
            if (_context.Shop.Any(x => x.OwnerId == userId)) usersShop = (await _context.Shop.FirstOrDefaultAsync(x => x.OwnerId == userId)).Id;

            if (categories == null || categories.Count == 0) categories = await _context.Categories.Select(x => x.Id).ToListAsync();
            if (search == null) search = "";
            if (rating == null) rating = 0;

            if (specificShopId == null)
            {
                products = await _context.Products.Where(x => categories.Contains(x.CategoryId) && x.Name.ToLower().Contains(search.Trim().ToLower()) && x.ShopId != usersShop)
                                    .GroupJoin(_context.ProductReviews.GroupBy(x => x.ProductId).Select(group => new
                                    {
                                        ProductId = group.Key,
                                        avg = group.Average(x => x.Rating)
                                    }), p => p.Id, pr => pr.ProductId, (p, pr) => new ProductCard
                                    {
                                        Id = p.Id,
                                        ShopId = p.ShopId,
                                        Name = p.Name,
                                        Price = p.Price,
                                        Rating = pr.DefaultIfEmpty().Select(x => x.avg).FirstOrDefault(),
                                        Image = _context.ProductImages.FirstOrDefault(x => x.ProductId == p.Id).Image
                                    })
                                    .Where(x => x.Rating >= rating)
                                    .ToListAsync();
            }
            else
            {
                products = await _context.Products.Where(x => categories.Contains(x.CategoryId) && x.Name.ToLower().Contains(search.Trim().ToLower()) && x.ShopId == specificShopId)
                                    .GroupJoin(_context.ProductReviews.GroupBy(x => x.ProductId).Select(group => new
                                    {
                                        ProductId = group.Key,
                                        avg = group.Average(x => x.Rating)
                                    }), p => p.Id, pr => pr.ProductId, (p, pr) => new ProductCard
                                    {
                                        Id = p.Id,
                                        ShopId = p.ShopId,
                                        Name = p.Name,
                                        Price = p.Price,
                                        Rating = pr.DefaultIfEmpty().Select(x => x.avg).FirstOrDefault(),
                                        Image = _context.ProductImages.FirstOrDefault(x => x.ProductId == p.Id).Image
                                    })
                                    .Where(x => x.Rating >= rating)
                                    .ToListAsync();
            }

            if (favorite != null && favorite == true)
            {
                products = products.Join(_context.LikedProducts.Where(x => x.UserId == userId), p => p.Id, lp => lp.ProductId, (p, lp) => p).ToList();
            }

            if (open != null && open == true)
            {
                products = products
                        .Join(_context.Shop.Join(_context.WorkingHours, s => s.Id, w => w.ShopId, (s, w) => w), p => p.ShopId, w => w.ShopId, (p, w) => new { p, w })
                        .ToList()
                        .Where(x => x.w.Day == DateTime.Now.DayOfWeek && x.w.OpeningHours <= DateTime.Now.TimeOfDay && x.w.ClosingHours >= DateTime.Now.TimeOfDay)
                        .Select(x => x.p)
                        .ToList();
            }

            if (location != null && location.Trim().Length > 0)
            {
                products = products.Join(_context.Shop.Where(x => x.Address.Trim().ToLower().Contains(location.Trim().ToLower())), p => p.ShopId, s => s.Id, (p, s) => p).ToList();
            }
            else if (range != null && range > 0)
            {
                products = products.Join(_context.Shop, p => p.ShopId, s => s.Id, (p, s) => (p, s)).Where(x => CalculateDistance((float)x.s.Latitude, (float)x.s.Longitude, (float)currLat, (float)currLong) <= range).Select(x => x.p).ToList();
            }

            return products;
        }

        public async Task<List<ProductCard>> GetProducts(int? userId, List<int>? categories, int? rating, bool? open, int? range, string? location, int sort, string? search, int page, int? specificShopId, bool? favorite, float? currLat, float? currLong)
        {
            List<ProductCard> products = await GetUnsortedProducts(userId, categories, rating, open, range, location, search, specificShopId, favorite, currLat, currLong);
            products = SortProducts(sort, products);

            return products.Skip((page-1) * numberOfItems).Take(numberOfItems).ToList();
        }

        public async Task<int> ProductPages(int? userId, List<int>? categories, int? rating, bool? open, int? range, string? location, string? search, int? specificShopId, bool? favorite, float? currLat, float? currLong)
        {
            return (int)Math.Ceiling((double)(await GetUnsortedProducts(userId, categories, rating, open, range, location, search, specificShopId, favorite, currLat, currLong)).Count()/ numberOfItems);
        }

        public async Task<ProductInfo> ProductDetails(int productId, int? userId)
        {
            Product product = await _context.Products.FirstOrDefaultAsync(x => x.Id == productId);
            if (product == null) return null;

            List<WorkingHours> workingHours = await _context.WorkingHours.Where(x => x.ShopId == product.ShopId).Select(wh => new WorkingHours
                                                {
                                                    Day = wh.Day,
                                                    OpeningHours = wh.OpeningHours,
                                                    ClosingHours = wh.ClosingHours,
                                                    Shop = null
                                                }).ToListAsync();
            List<ProductReviewExtended> reviews = await _context.ProductReviews.Where(x => x.ProductId == productId).Select(pr => new ProductReviewExtended
                                                {
                                                    ReviewerId = pr.ReviewerId,
                                                    ProductId = pr.ProductId,
                                                    Rating = pr.Rating,
                                                    Comment = pr.Comment,
                                                    PostedOn = pr.PostedOn,
                                                    Username = _context.Users.FirstOrDefault(x => x.Id == pr.ReviewerId).Username,
                                                    Image = _context.Users.FirstOrDefault(x => x.Id == pr.ReviewerId).Image,
                                                    Product = null 
                                                }).ToListAsync();

            List<QuestionWithAnswer> qna;

            if (_context.Shop.FirstOrDefault(x => x.Id == product.ShopId).OwnerId == userId)
            {
                List<ProductQuestion> questions = await _context.ProductQuestions.Where(x => x.ProductId == productId).ToListAsync();
                List<ProductAnswer> answers = await _context.ProductAnswers.Where(x => questions.Select(x => x.Id).Contains(x.QuestionId)).ToListAsync();
                qna = questions.GroupJoin(answers, q => q.Id, a => a.QuestionId, (q, a) => (q, a))
                                                             .SelectMany(
                                                                 q => q.a.DefaultIfEmpty(),
                                                                 (q, a) => (q.q, a)
                                                             ).Select(x => new QuestionWithAnswer
                                                             {
                                                                 QuestionId = x.q.Id,
                                                                 AnswerId = x.a != null ? x.a.Id : -1,
                                                                 QuestionText = x.q.QuestionText,
                                                                 AnswerText = x.a != null ? x.a.AnswerText : null
                                                             }).ToList();
            }
            else
            {
                qna = await _context.ProductQuestions.Where(x => x.ProductId == productId).Join(_context.ProductAnswers, q => q.Id, a => a.QuestionId, (q, a) => new { q, a }).Select(x => new QuestionWithAnswer
                {
                    QuestionId = x.q.Id,
                    AnswerId = x.a.Id,
                    QuestionText = x.q.QuestionText,
                    AnswerText = x.a.AnswerText
                }).ToListAsync();
            }
           
            List<Stock> sizes = await _context.ProductSizes.Where(x => x.ProductId == productId).Join(_context.Sizes, ps => ps.SizeId, s => s.Id, (ps, s) => new Stock{ SizeId = s.Id, Size = s.Name, Quantity = ps.Stock }).ToListAsync();
            List<ImageData> images = await _context.ProductImages.Where(x => x.ProductId == productId).Select(x => new ImageData{Id = x.Id, Image = x.Image}).ToListAsync();
            float average = 0;
            if (_context.ProductReviews.Where(x => x.ProductId == productId).Count() > 0) average = _context.ProductReviews.Where(x => x.ProductId == productId).Select(x => x.Rating).Average();

            return new ProductInfo
            {
                Id = productId,
                ShopId = product.ShopId,
                ShopName = _context.Shop.FirstOrDefault(x => x.Id == product.ShopId).Name,
                Name = product.Name,
                Description = product.Description,
                Price = product.Price,
                Metric = _context.Metrics.FirstOrDefault(x => x.Id == product.MetricId).Name,
                Category = _context.Categories.FirstOrDefault(x => x.Id == product.CategoryId).Name,
                SalePercentage = product.SalePercentage,
                SaleMinQuantity = product.SaleMinQuantity,
                SaleMessage = product.SaleMessage,
                Liked = _context.LikedProducts.Any(x => x.ProductId == productId && x.UserId == userId),
                Bought = _context.Orders.Join(_context.OrderItems, o => o.Id, oi => oi.OrderId, (o, oi) => new { o, oi }).Any(x => x.oi.ProductId == productId && x.o.UserId == userId),
                Rated = _context.ProductReviews.Any(x => x.ProductId == productId && x.ReviewerId == userId),
                IsOwner = _context.Shop.FirstOrDefault(x => x.Id == product.ShopId).OwnerId == userId,
                Rating = average,
                WorkingHours = workingHours,
                QuestionsAndAnswers = qna,
                Sizes = sizes,
                Images = images
            };

        }

        public async Task<List<ProductReviewExtended>> GetProductReviews(int productId, int page)
        {
            if (page == 0)
            {
                return await _context.ProductReviews.Where(x => x.ProductId == productId).Join(_context.Users, pr => pr.ReviewerId, u => u.Id, (pr, u) => new { pr, u }).Select(x => new ProductReviewExtended
                {
                    ProductId = productId,
                    ReviewerId = x.pr.ReviewerId,
                    Comment = x.pr.Comment,
                    Rating = x.pr.Rating,
                    PostedOn = x.pr.PostedOn,
                    Image = x.u.Image,
                    Username = x.u.Username,
                    Product = null,
                    Reviewer = null

                }).Take(1).ToListAsync();
            }

            return await _context.ProductReviews.Where(x => x.ProductId == productId).Join(_context.Users, pr => pr.ReviewerId, u => u.Id, (pr, u) => new { pr, u }).Select(x => new ProductReviewExtended
            {
                ProductId = productId,
                ReviewerId = x.pr.ReviewerId,
                Comment = x.pr.Comment,
                Rating = x.pr.Rating,
                PostedOn = x.pr.PostedOn,
                Image = x.u.Image,
                Username = x.u.Username,
                Product = null,
                Reviewer = null

            }).Skip((page - 1) * numberOfReviews).Take(numberOfReviews).ToListAsync();            
        }

        #region likes
        public async Task<LikedProducts> GetLike(int productId, int userId)
        {
            return await _context.LikedProducts.FirstOrDefaultAsync(x => x.ProductId == productId && x.UserId == userId);
        }

        public async Task<bool> LikeProduct(int productId, int userId)
        {
            await _context.LikedProducts.AddAsync(new LikedProducts { ProductId = productId, UserId = userId });
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> DislikeProduct(int productId, int userId)
        {
            _context.LikedProducts.Remove(await GetLike(productId, userId));
            return await _context.SaveChangesAsync() > 0;
        }

        #endregion

        #region cart

        public async Task<Cart> GetCartItem(int productId, int userId)
        {
            return await _context.Carts.FirstOrDefaultAsync(x => x.ProductId == productId && x.UserId == userId);
        }

        public async Task<bool> AddToCart(int productId, int userId, int quantity)
        {
            await _context.Carts.AddAsync(new Cart { ProductId = productId, UserId = userId, Quantity = quantity });
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> RemoveFromCart(int productId, int userId)
        {
            _context.Carts.Remove(await GetCartItem(productId, userId));
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> UpdateCart(int productId, int userId, int quantity)
        {
            Cart item = await GetCartItem(productId, userId);
            item.Quantity += quantity;
            return await _context.SaveChangesAsync() > 0;
        }

        #endregion

        public async Task<bool> LeaveReview(ReviewDto review)
        {
            try
            {
                var newReview = new ProductReview
                {
                    ReviewerId = review.UserId,
                    ProductId = review.Id,
                    Rating = review.Rating,
                    Comment = review.Comment,
                    PostedOn = DateTime.Now
                };

                await _context.ProductReviews.AddAsync(newReview);
                await _context.SaveChangesAsync();

                int ownerID = 1;

                await SendNotificationAsync("New Review", "You have a new review to review!",ownerID);

                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        private async Task SendNotificationAsync(string title, string body, int userID)
{
    try
    {
        var user = _context.Users.FirstOrDefault(u => u.Id == userID);

        if (user == null)
        {
            Console.WriteLine("User not found.");
            return;
        }

        var fcmToken = user.FCMToken;

        if (string.IsNullOrEmpty(fcmToken))
        {
            Console.WriteLine("FCM token not available for the user.");
            return;
        }

        var message = new Message()
        {
            Notification = new Notification
            {
                Title = title,
                Body = body
            },
            Token = fcmToken,
        };

        try
        {
            var result = await FirebaseMessaging.DefaultInstance.SendAsync(message);
            Console.WriteLine($"Notification sent successfully: {result}");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error sending notification: {ex.Message}");
        }
    }
    catch (Exception ex)
    {
        Console.WriteLine($"Error: {ex.Message}");
    }
}




        public async Task<bool> LeaveQuestion(QnADto question)
        {
            _context.ProductQuestions.AddAsync(new ProductQuestion
            {
                PosterId = question.UserId,
                ProductId = question.Id,
                QuestionText = question.Text,
                PostedOn = DateTime.Now
            });

            return _context.SaveChanges() > 0;
        }

        public async Task<bool> AddProductPhoto(int id, string path)
        {
            _context.ProductImages.Add(new ProductImage { Image = path, ProductId = id });
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<string> DeleteProductPhoto(int photoId)
        {
            ProductImage pi = await _context.ProductImages.FirstOrDefaultAsync(x => x.Id == photoId);
            string name = pi.Image;
            _context.ProductImages.Remove(pi);
            await _context.SaveChangesAsync();
            return name;
        }

        public async Task<int> AddProduct(Product product)
        {
            _context.Products.AddAsync(product);
            await _context.SaveChangesAsync();
            int id = product.Id;
            return  id;
        }

        public async Task<bool> AddProductSize(int id, int sizeId, int quantity)
        {
            await _context.ProductSizes.AddAsync(new ProductSize { ProductId = id, SizeId = sizeId, Stock = quantity });
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<List<ProductSize>> GetProductSizes(int productId)
        {
            return await _context.ProductSizes.Where(x => x.ProductId == productId).ToListAsync();
        }

        public async Task<bool> DeleteProduct(int productId)
        {
            Product p = await _context.Products.FirstOrDefaultAsync(x => x.Id == productId);
            _context.Products.Remove(p);

            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> EditProduct(EditProductDto product)
        {
            if (product.Name == null && product.Description == null && product.SaleMinQuantity == null && product.SalePercentage == null && product.SaleMessage == null) return true; 

            Product p = await _context.Products.FirstOrDefaultAsync(x => x.Id == product.Id);

            if (product.Name != null) p.Name = product.Name;
            if (product.Description != null) p.Description = product.Description;
            if (product.SaleMinQuantity != null) p.SaleMinQuantity = (int)product.SaleMinQuantity;
            if (product.SalePercentage != null) p.SalePercentage = (float)product.SalePercentage;
            if (product.SaleMessage != null) p.SaleMessage = product.SaleMessage;

            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> EditProductSize(int id, int sizeId, int quantity)
        {
            ProductSize ps = await _context.ProductSizes.FirstOrDefaultAsync(x => x.ProductId == id && x.SizeId == sizeId);
            ps.Stock = quantity;
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<List<int>> GetShopFollowers(int shopId)
        {
            return await _context.LikedShops.Where(x => x.ShopId == shopId).Select(x => x.UserId).ToListAsync();
        }

        public async Task<Product> GetProduct(int id)
        {
            return await _context.Products.FirstOrDefaultAsync(x => x.Id == id);
        }

        public async Task<string> GetMetric(int metricId)
        {
            return (await _context.Metrics.FirstOrDefaultAsync(x => x.Id == metricId)).Name;
        }

        public async Task<List<Metric>> GetMetrics()
        {
            return await _context.Metrics.ToListAsync();
        }

        public async Task<List<Size>> GetSizes()
        {
            return await _context.Sizes.ToListAsync();
        }
    }
}
