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

                    var pendingProductReviews = await _backgroundRepository.PendingProductReviews();
                    var pendingShopReviews = await _backgroundRepository.PendingShopReviews();
                    var pendingDeliveryPersonReviews = await _backgroundRepository.PendingDeliveryPersonReviews();

                    _logger.LogInformation("Pending product reviews");

                    foreach (var pr in pendingProductReviews)
                    {
                        _logger.LogInformation(pr.UserId + " " + pr.ItemId);
                    }

                    _logger.LogInformation("Pending shop reviews");

                    foreach (var sr in pendingShopReviews)
                    {
                        _logger.LogInformation(sr.UserId + " " + sr.ItemId);
                    }

                    _logger.LogInformation("Pending delivery person reviews");

                    foreach (var dr in pendingDeliveryPersonReviews)
                    {
                        _logger.LogInformation(dr.UserId + " " + dr.ItemId);
                    }

                    await _backgroundRepository.DeletePastProductDisplays();
                }

                await Task.Delay(3600000, stoppingToken);
            }
        }
    }
}
