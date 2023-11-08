using back.BLL.Dtos;

namespace back.BLL.Services
{
    public interface IUserService
    {
        public Task<MyProfileInfo> GetMyProfile(int id);
    }
}
