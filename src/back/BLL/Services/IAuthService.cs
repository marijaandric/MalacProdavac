using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface IAuthService
    {
        //string treba
        public Task<string> Login(LoginDto loginDto);
        public Task<int> Register(UserDto userDto);
    }
}
