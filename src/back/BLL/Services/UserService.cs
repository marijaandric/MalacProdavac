using back.BLL.Dtos;
using back.DAL.Repositories;

namespace back.BLL.Services
{
    public class UserService : IUserService
    {
        IUserRepository _repository;
        public UserService(IUserRepository userRepository)
        {
            _repository = userRepository;
        }

        public async Task<MyProfileInfo> GetMyProfile(int id)
        {
            MyProfileInfo profile = await _repository.GetMyProfile(id);
            if (profile == null) throw new ArgumentException("No profile found!");

            return profile;
        }
    }
}
