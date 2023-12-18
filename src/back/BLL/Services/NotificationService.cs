using back.BLL.Dtos.Cards;
using back.DAL.Repositories;

namespace back.BLL.Services
{
    public class NotificationService : INotificationService
    {
        INotificationRepository _repository;
        public NotificationService(INotificationRepository repository)
        {
            _repository = repository;
        }

        public async Task<List<NotificationCard>> GetNotifications(int userId, List<int>? types, int page)
        {
            List<NotificationCard> notifications = await _repository.GetNotifications(userId, types, page);
            if (notifications.Count == 0) throw new ArgumentException("No notifications!");

            return notifications;
        }
    }
}
