using back.BLL.Dtos;
using back.BLL.Dtos.HelpModels;
using back.DAL.Contexts;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class ProductRepository : IProductRepository
    {
        Context _context;
        int numberOfItems = 10;
        public ProductRepository(Context context)
        {
            _context = context;
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

        public async Task<List<ProductCard>> GetProducts(int userId, List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page, int specificShopId, bool favorite)
        {
            List<ProductCard> products;
            User currentUser = _context.Users.FirstOrDefault(x => x.Id == userId);
            float currLat = currentUser.Latitude;
            float currLong = currentUser.Longitude;
            int usersShop = -1;
            if (_context.Shop.Any(x => x.OwnerId == userId)) usersShop = (await _context.Shop.FirstOrDefaultAsync(x => x.OwnerId == userId)).Id;

            if (categories.Count == 0) categories = await _context.Categories.Select(x => x.Id).ToListAsync();
            if (specificShopId == -1)
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
                
            if (favorite)
            {
                products = products.Join(_context.LikedProducts.Where(x => x.UserId == userId), p => p.Id, lp => lp.ProductId, (p, lp) => p).ToList();
            }

            if (open)
            {
                products = products
                        .Join(_context.Shop.Join(_context.WorkingHours, s => s.Id, w => w.ShopId, (s, w) => w), p => p.ShopId, w => w.ShopId, (p, w) => new {p, w})
                        .ToList()
                        .Where(x => x.w.Day == DateTime.Now.DayOfWeek && x.w.OpeningHours <= DateTime.Now.TimeOfDay && x.w.ClosingHours >= DateTime.Now.TimeOfDay)
                        .Select(x => x.p)
                        .ToList();
            }

            if (location.Trim().Length > 0 && location != "none")
            {
                products = products.Join(_context.Shop.Where(x => x.Address.Trim().ToLower().Contains(location.Trim().ToLower())), p => p.ShopId, s => s.Id, (p, s) => p).ToList();
            }
            else if (range > 0)
            {
                products = products.Join(_context.Shop, p => p.ShopId, s => s.Id, (p, s) => (p, s)).Where(x => CalculateDistance((float)x.s.Latitude, (float)x.s.Longitude, currLat, currLong) <= range).Select(x => x.p).ToList();
            }

            products = SortProducts(sort, products);

            return products.Skip((page-1) * numberOfItems).Take(numberOfItems).ToList();
        }

        public int ProductPages()
        {
            return (int)Math.Ceiling((double)_context.Products.Count()/numberOfItems);
        }

        public async Task<ProductInfo> ProductDetails(int productId, int userId)
        {
            Product product = await _context.Products.FirstOrDefaultAsync(x => x.Id == productId);
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
            List<ProductQuestion> questions = await _context.ProductQuestions.Where(x => x.ProductId == productId).ToListAsync();
            List<ProductAnswer> answers = await _context.ProductAnswers.Where(x => questions.Select(x => x.Id).Contains(x.QuestionId)).ToListAsync();
            List<QuestionWithAnswer> qna = questions.GroupJoin(answers, q => q.Id, a => a.QuestionId, (q, a) => (q, a))
                                                        .SelectMany(
                                                            q => q.a.DefaultIfEmpty(),
                                                            (q, a) => (q.q, a)
                                                        ).Select(x => new QuestionWithAnswer
                                                        {
                                                            QuestionId = x.q.Id,
                                                            AnswerId = x.a != null ? x.a.Id : -1,
                                                            QuestionText = x.q.QuestionText,
                                                            AnswerText = x.a != null ? x.a.AnswerText : null
                                                        }
                                                            ).ToList(); 
            List<Stock> sizes = await _context.ProductSizes.Where(x => x.ProductId == productId).Join(_context.Sizes, ps => ps.SizeId, s => s.Id, (ps, s) => new Stock{ Size = s.Name, Quantity = ps.Stock }).ToListAsync();
            List<ImageData> images = await _context.ProductImages.Where(x => x.ProductId == productId).Select(x => new ImageData{Id = x.Id, Image = x.Image}).ToListAsync();
            float average = 0;
            if (reviews.Count > 0) average = reviews.Select(x => x.Rating).Average();

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
                Subcategory = _context.Subcategories.FirstOrDefault(x => x.Id == product.SubcategoryId).Name,
                SalePercentage = product.SalePercentage,
                SaleMinQuantity = product.SaleMinQuantity,
                SaleMessage = product.SaleMessage,
                Liked = _context.LikedProducts.Any(x => x.ProductId == productId && x.UserId == userId),
                Bought = _context.Orders.Join(_context.OrderItems, o => o.Id, oi => oi.OrderId, (o, oi) => new { o, oi }).Any(x => x.oi.ProductId == productId && x.o.UserId == userId),
                Rated = _context.ProductReviews.Any(x => x.ProductId == productId && x.ReviewerId == userId),
                IsOwner = product.ShopId == userId,
                Rating = average,
                WorkingHours = workingHours,
                QuestionsAndAnswers = qna,
                Sizes = sizes,
                Images = images
            };

        }

        public async Task<List<ProductReviewExtended>> GetProductReviews(int productId, int page)
        {
            return await _context.ProductReviews.Where(x => x.ProductId == productId).Join(_context.Users, pr => pr.ReviewerId, u => u.Id, (pr, u) => new {pr, u}).Select(x => new ProductReviewExtended
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
                
            }).Skip((page-1) * numberOfItems).Take(numberOfItems).ToListAsync();
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
            _context.ProductReviews.AddAsync(new ProductReview
            {
                ReviewerId = review.UserId,
                ProductId = review.Id,
                Rating = review.Rating,
                Comment = review.Comment,
                PostedOn = DateTime.Now
            });

            return _context.SaveChanges() > 0;
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
    }
}
