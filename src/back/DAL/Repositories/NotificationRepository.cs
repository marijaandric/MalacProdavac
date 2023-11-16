using back.BLL.Dtos;
using back.DAL.Contexts;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class NotificationRepository : INotificationRepository
    {
        Context _context;

        public NotificationRepository(Context context)
        {
            _context = context;
        }

        int numberOfItems = 10;

        public async Task<List<NotificationCard>> GetNotifications(int userId, int type, int page)
        {
            if (type == -1)
            {
                return await _context.Notifications.Where(x => x.UserId == userId).Skip(page - 1 * numberOfItems).Take(numberOfItems).Select(x => new NotificationCard
                {
                    Id = x.Id,
                    Title = x.Title,
                    Text = x.Text,
                    CreatedOn = x.CreatedOn,
                    TypeId = type,
                    ReferenceId = x.ReferenceId,
                    Read = x.Read
                }).ToListAsync();
            }

            return await _context.Notifications.Where(x => x.UserId == userId && x.TypeId == type).Skip(page-1 * numberOfItems).Take(numberOfItems).Select(x => new NotificationCard
            {
                Id = x.Id,
                Title = x.Title,
                Text = x.Text,
                CreatedOn = x.CreatedOn,
                TypeId = type,
                ReferenceId = x.ReferenceId,
                Read = x.Read
            }).ToListAsync();
        }
    }
}
