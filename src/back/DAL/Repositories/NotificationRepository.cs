using back.BLL.Dtos.Cards;
using back.DAL.Contexts;
using back.Models;
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

        public async Task<List<NotificationCard>> GetNotifications(int userId, int? type, int page)
        {
            if (type == null)
            {
                return await _context.Notifications.Where(x => x.UserId == userId).Skip(page - 1 * numberOfItems).Take(numberOfItems).Select(x => new NotificationCard
                {
                    Id = x.Id,
                    Title = x.Title,
                    Text = x.Text,
                    CreatedOn = x.CreatedOn,
                    TypeId = x.TypeId,
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
                TypeId = (int)type,
                ReferenceId = x.ReferenceId,
                Read = x.Read
            }).ToListAsync();
        }

        public async Task<bool> InsertNotification(int userId, int type, string title, string description, int referenceId)
        {
            Notification notification = new Notification
            {
                UserId = userId,
                CreatedOn = DateTime.Now,
                Read = false,
                TypeId = type,
                ReferenceId = referenceId,
                Text = description,
                Title = title
            };

            await _context.Notifications.AddAsync(notification);

            return await _context.SaveChangesAsync() > 0;
        }

        public bool NotificationExists(int userId, int type, int referenceId)
        {
            return _context.Notifications.Any(x => x.UserId == userId && x.TypeId == type && x.ReferenceId == referenceId);
        }
    }
}
