using back.BLL.Dtos;

namespace back.DAL.Repositories
{
    public interface INotificationRepository
    {
        public Task<List<NotificationCard>> GetNotifications(int userId, int type, int page);
    }
}
