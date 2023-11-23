using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface IAuthService
    {
        //string treba
        public Task<string> Login(LoginDto loginDto);
        public Task<string> Register(UserDto userDto);
        public Task<string> EditUser(EditUserDto values);
        public Task<bool> ChangePassword(ChangePasswordDto values);
        public Task<bool> DeleteProfilePhoto(int userId);
        public Task<bool> SaveFcmToken(int id, string token);
        public Task<bool> SaveLatestCoordinates(int userId, float lat, float lon);
    }
}
