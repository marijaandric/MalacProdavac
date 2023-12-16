using back.DAL.Repositories;

namespace back.BLL.Services
{
    public class BackgroundHandlerService : BackgroundService
    {
        private readonly ILogger _logger;
        private readonly IServiceProvider _serviceProvider;

        public BackgroundHandlerService(ILogger<BackgroundHandlerService> logger, IServiceProvider serviceProvider)
        {
            _logger = logger;
            _serviceProvider = serviceProvider;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            while (!stoppingToken.IsCancellationRequested)
            {
                using (var scope = _serviceProvider.CreateScope())
                {
                    var _notificationRepository = scope.ServiceProvider.GetRequiredService<INotificationRepository>();
                    var _backgroundRepository = scope.ServiceProvider.GetRequiredService<IBackgroundRepository>();
                    var _productRepository = scope.ServiceProvider.GetRequiredService<IProductRepository>();
                    var _shopRepository = scope.ServiceProvider.GetRequiredService<IShopRepository>();
                    var _userRepository = scope.ServiceProvider.GetRequiredService<IUserRepository>();

                    var pendingProductReviews = (await _backgroundRepository.PendingProductReviews()).Where(x => !_notificationRepository.NotificationExists(x.UserId, 7, x.ItemId));
                    var pendingShopReviews = (await _backgroundRepository.PendingShopReviews()).Where(x => !_notificationRepository.NotificationExists(x.UserId, 8, x.ItemId));
                    var pendingDeliveryPersonReviews = (await _backgroundRepository.PendingDeliveryPersonReviews()).Where(x => !_notificationRepository.NotificationExists(x.UserId, 1, x.ItemId));
                    var pendingCustomerReviews = (await _backgroundRepository.PendingCustomerReviews()).Where(x => !_notificationRepository.NotificationExists(x.UserId, 1, x.ItemId));

                    _logger.LogInformation("Pending product reviews");

                    foreach (var pr in pendingProductReviews)
                    {
                        _logger.LogInformation(pr.UserId + " " + pr.ItemId);
                        var product = await _productRepository.ProductDetails(pr.ItemId, null);
                        if (await _notificationRepository.InsertNotification(pr.UserId, 7, "Product review", "Would you like to review " + product.Name + " that you recently bought from " + product.ShopName + "?", pr.ItemId)) _logger.LogInformation("Notification sent!");
                    }

                    _logger.LogInformation("Pending shop reviews");

                    foreach (var sr in pendingShopReviews)
                    {
                        _logger.LogInformation(sr.UserId + " " + sr.ItemId);
                        var shop = await _shopRepository.GetShop(sr.ItemId);
                        if (await _notificationRepository.InsertNotification(sr.UserId, 8, "Shop review", "Would you like to review " + shop.Name + ", from which you recently bought from?", sr.ItemId)) _logger.LogInformation("Notification sent!");
                    }
                    _logger.LogInformation("Pending delivery person reviews");

                    foreach (var dr in pendingDeliveryPersonReviews)
                    {
                        _logger.LogInformation(dr.UserId + " " + dr.ItemId);
                        var user = await _userRepository.GetUsername(dr.ItemId);
                        if (await _notificationRepository.InsertNotification(dr.UserId, 1, "Delivery person review", "Would you like to review " + user + ", who recently delivered your order?", dr.ItemId)) _logger.LogInformation("Notification sent!");
                    }

                    foreach (var dr in pendingCustomerReviews)
                    {
                        _logger.LogInformation(dr.UserId + " " + dr.ItemId);
                        var user = await _userRepository.GetUsername(dr.ItemId);
                        if (await _notificationRepository.InsertNotification(dr.UserId, 1, "Customer review", "Would you like to review " + user + ", who you recently serviced?", dr.ItemId)) _logger.LogInformation("Notification sent!");
                    }

                    await _backgroundRepository.DeletePastProductDisplays();
                    await _backgroundRepository.ChangeDeliveryStatus();
                }

                await Task.Delay(3600000, stoppingToken);
            }
        }
    }
}
