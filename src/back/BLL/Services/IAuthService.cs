using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface IAuthService
    {
        public Task<int> Login(LoginDto loginDto);
        public Task<int> Register(UserDto userDto);
    }
}
