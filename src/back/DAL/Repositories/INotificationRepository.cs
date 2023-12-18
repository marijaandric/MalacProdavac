using back.BLL.Dtos.Cards;
using back.Models;

namespace back.DAL.Repositories
{
    public interface INotificationRepository
    {
        public Task<List<NotificationCard>> GetNotifications(int userId, List<int>? types, int page);
        public Task<bool> InsertNotification(int userId, int type, string title, string description, int referenceId);
        public bool NotificationExists(int userId, int type, int referenceId);
        public Task<bool> MarkAsRead(int notificationId);
        public Task<bool> DeleteNotification(Notification notification);
        public Task<bool> DeleteAllNotifications(int userId);
        public Task<Notification> GetNotification(int notificationId);

    }
}
