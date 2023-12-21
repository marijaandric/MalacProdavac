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

        public int PageCount(int userId, List<int>? types)
        {
            return _repository.PageCount(userId, types);
        }

        public async Task<bool> MarkAsRead(int notificationId)
        {
            return await _repository.MarkAsRead(notificationId);
        }

        public async Task<bool> DeleteNotification(int notificationId)
        {
            var not = await _repository.GetNotification(notificationId);
            return await _repository.DeleteNotification(not);
        }

        public async Task<bool> DeleteAllNotifications(int userId)
        {
            return await _repository.DeleteAllNotifications(userId);
        }
    }
}
