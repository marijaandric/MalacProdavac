using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface IAuthService
    {
        //string treba
        public Task<string> Login(LoginDto loginDto);
        public Task<string> Register(UserDto userDto);
        public Task<string> EditUser(EditUserDto values);
    }
}
