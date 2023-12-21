using back.BLL.Dtos.Cards;

namespace back.BLL.Services
{
    public interface INotificationService
    {
        public Task<List<NotificationCard>> GetNotifications(int userId, List<int>? types, int page);
        public int PageCount(int userId, List<int>? types);
        public Task<bool> MarkAsRead(int notificationId);
        public Task<bool> DeleteNotification(int notificationId);
        public Task<bool> DeleteAllNotifications(int userId);
    }
}
