using back.BLL.Dtos.Cards;

namespace back.BLL.Services
{
    public interface INotificationService
    {
        public Task<List<NotificationCard>> GetNotifications(int userId, int? type, int page);
    }
}
