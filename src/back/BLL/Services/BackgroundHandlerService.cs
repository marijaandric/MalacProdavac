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

                    foreach (var pr in await _backgroundRepository.PendingProductReviews())
                    {
                        _logger.LogInformation(pr.UserId + " " + pr.ProductId);
                    }

                }
                _logger.LogInformation("logovano");
                await Task.Delay(1000, stoppingToken);
            }
        }
    }
}
