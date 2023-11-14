using back.Models;

namespace back.DAL.Repositories
{
    public interface IAuthRepository
    {
        public Task<User> GetUser(string username);
        public Task<User> GetUser(int userId);
        public Task<List<User>> GetUsers();
        public int CountUsers(string username);
        public bool SameEmail(string email);
        public Task<bool> InsertUser(User user);
        public Task<Role> GetRole(string roleName);
        public Task<bool> EditUser(User newUser);
        public Task<bool> ChangeProfilePhoto(int id, string path);
    }
}
