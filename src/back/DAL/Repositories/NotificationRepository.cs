using back.BLL.Dtos.Cards;
using back.DAL.Contexts;
using back.Models;
using FirebaseAdmin.Messaging;
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

        int numberOfItems = 3;

        public async Task<List<NotificationCard>> GetNotifications(int userId, List<int>? types, int page)
        {
            if (types == null || types.Count == 0)
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

            return await _context.Notifications.Where(x => x.UserId == userId && types.Contains(x.TypeId)).Skip(page-1 * numberOfItems).Take(numberOfItems).Select(x => new NotificationCard
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

        public async Task<bool> InsertNotification(int userId, int type, string title, string description, int referenceId)
        {
            back.Models.Notification notification = new back.Models.Notification
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

            if (await _context.SaveChangesAsync() > 0)
            {
                await SendNotificationAsync(title, description, userId);
                return true;
            }
            else return false;
        }

        private async Task SendNotificationAsync(string title, string body, int userID)
        {
            try
            {
                var user = _context.Users.FirstOrDefault(u => u.Id == userID);

                if (user == null)
                {
                    Console.WriteLine("User not found.");
                    return;
                }

                var fcmToken = user.FCMToken;

                if (string.IsNullOrEmpty(fcmToken))
                {
                    Console.WriteLine("FCM token not available for the user.");
                    return;
                }

                var message = new Message()
                {
                    Notification = new FirebaseAdmin.Messaging.Notification
                    {
                        Title = title,
                        Body = body
                    },
                    Token = fcmToken,
                };

                try
                {
                    var result = await FirebaseMessaging.DefaultInstance.SendAsync(message);
                    Console.WriteLine($"Notification sent successfully: {result}");
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Error sending notification: {ex.Message}");
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }
        }

        public bool NotificationExists(int userId, int type, int referenceId)
        {
            return _context.Notifications.Any(x => x.UserId == userId && x.TypeId == type && x.ReferenceId == referenceId);
        }

        public async Task<bool> MarkAsRead(int notificationId)
        {
            var notification = await _context.Notifications.FirstOrDefaultAsync(x => x.Id == notificationId);
            if (notification == null) return false;
            notification.Read = true;
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> DeleteNotification(back.Models.Notification notification)
        {
            _context.Notifications.Remove(notification);
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> DeleteAllNotifications(int userId)
        {
            var toDelete = await _context.Notifications.Where(x => x.UserId == userId).ToListAsync();
            _context.Notifications.RemoveRange(toDelete);

            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<back.Models.Notification> GetNotification(int notificationId)
        {
            return await _context.Notifications.FirstOrDefaultAsync(x => x.Id == notificationId);
        }
    }
}
