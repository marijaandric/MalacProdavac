using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface IAuthService
    {
        public Task<int> Login(string username, string password);
        public Task<int> Register(UserDto userDto);
    }
}
