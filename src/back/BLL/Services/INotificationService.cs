using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface INotificationService
    {
        public Task<List<NotificationCard>> GetNotifications(int userId, int type, int page);
    }
}
