using back.DAL.Contexts;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class AuthRepository : IAuthRepository
    {
        Context _context;
        public AuthRepository(Context context)
        {
            _context = context;
        }

        public async Task<bool> Save()
        {
            var save = await _context.SaveChangesAsync();
            return save > 0;
        }
        public async Task<User> GetUser(string username)
        {
            return await _context.Users.Where(x => x.Username.Equals(username) || x.Email.Equals(username)).FirstOrDefaultAsync();
        }
        public async Task<User> GetUser(int userId)
        {
            return await _context.Users.Where(x => x.Id == userId).FirstOrDefaultAsync();
        }
        public bool SameEmail(string email)
        {
            return _context.Users.Any(x => x.Email.Equals(email));
        }

        public async Task<List<User>> GetUsers()
        {
            return await _context.Users.ToListAsync();
        }

        public int CountUsers(string username)
        {
            return _context.Users.Where(x => x.Name.Contains(username)).Count();
        }

        public async Task<bool> InsertUser(User user)
        {
            await _context.Users.AddAsync(user);
            return await Save();
        }

        public Task<Role> GetRole(string roleName)
        {
            return _context.Roles.Where(x => x.Name.Equals(roleName)).FirstOrDefaultAsync();
        }

        public async Task<bool> EditUser(User newUser)
        {
            User user = await _context.Users.FirstOrDefaultAsync(x => x.Id == newUser.Id);
            user = newUser;
            return await Save();
        }

        public async Task<bool> ChangeProfilePhoto(int id, string path)
        {
            User user = await _context.Users.FirstOrDefaultAsync(x => x.Id == id);
            user.Image = path;
            return await Save();
        }

        public async Task<string> DeleteProfilePhoto(int userId)
        {
            User user = await _context.Users.FirstOrDefaultAsync(x => x.Id == userId);
            string name = user.Image;
            user.Image = "default.png";
            await _context.SaveChangesAsync();
            return name;
        }

        public async Task<bool> SaveFcmToken(int id, string token)
        {
            User user = await _context.Users.FirstOrDefaultAsync(x => x.Id == id);

            user.FCMToken = token;
            
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> SaveLatestCoordinates(int userId, float lat, float lon)
        {
            User user = await _context.Users.FirstOrDefaultAsync(x => x.Id == userId);

            user.LatestLatitude = lat;
            user.LatestLongitude = lon;

            return await _context.SaveChangesAsync() > 0;
        }
    }
}
