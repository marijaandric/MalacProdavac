using back.BLL.Dtos;

namespace back.DAL.Repositories
{
    public interface IUserRepository
    {
        public Task<MyProfileInfo> GetMyProfile(int id);
    }
}
