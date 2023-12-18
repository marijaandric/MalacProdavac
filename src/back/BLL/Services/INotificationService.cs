using back.BLL.Dtos.Cards;

namespace back.BLL.Services
{
    public interface INotificationService
    {
        public Task<List<NotificationCard>> GetNotifications(int userId, List<int>? types, int page);
    }
}
