using back.BLL.Dtos;
using back.DAL.Repositories;
using back.Models;
using Microsoft.AspNetCore.Identity;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using System.Text.RegularExpressions;

namespace back.BLL.Services
{
    public class AuthService : IAuthService
    {
        IAuthRepository _authRepository;
        public AuthService(IAuthRepository authRepository)
        {
            _authRepository = authRepository;
        }

        public string defaultImagePath = Path.Combine("images/default.png", AppDomain.CurrentDomain.BaseDirectory);

        #region registrationHelperMethods
        public string CreateUsername(string name, string lastname)
        {
            string username = name + "." + lastname;
            int count = _authRepository.CountUsers(username);
            if (count > 0) username += count;

            return username;
        }

        public string CheckEmail(string email)
        {
            string pattern = @"^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$";
            Regex regex = new Regex(pattern);

            if (!regex.IsMatch(email)) return "Invalid email format.";
            if (_authRepository.SameEmail(email)) return "Email address already registered.";

            return "";
        }

        public string CheckPassword(string password)
        {
            string pattern = @"^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*\W).{8,}$";

            Regex regex = new Regex(pattern);

            if (password.Length < 8) return "Password must be at least 8 characters long.";
            if (!regex.IsMatch(password)) return "Password must contain at least one lowercase and one uppercase letter, a digit and a special character.";

            return "";
        }
        #endregion

        public async Task<int> Register(UserDto userDto)
        {
            #region validation
            string checkEmail = CheckEmail(userDto.Email);
            string checkPassword = CheckPassword(userDto.Password);

            if (!checkEmail.Equals("")) throw new ArgumentException(checkPassword);
            if (!checkPassword.Equals("")) throw new ArgumentException(checkPassword);
            #endregion

            User user = new User();

            user.Name = userDto.Name;
            user.Lastname = userDto.Lastname;
            user.Username = CreateUsername(user.Name, user.Lastname);
            user.Email = userDto.Email;
            user.Address = userDto.Address;
            user.RoleId = (await _authRepository.GetRole("Customer")).Id;
            user.LoggedIn = true;
            user.LightTheme = true;
            user.CreatedOn = DateTime.Now;

            byte[] passwordHash;
            byte[] passwordSalt;

            using (HMACSHA512 hmac = new HMACSHA512())
            {
                passwordSalt = hmac.Key;
                passwordHash = hmac.ComputeHash(Encoding.UTF8.GetBytes(userDto.Password));
            }

            user.Password = passwordHash;
            user.PasswordSalt = passwordSalt;

            if (System.IO.File.Exists(defaultImagePath))
            {
                using (var stream = new FileStream(defaultImagePath, FileMode.Open))
                {
                    var bytes = new byte[stream.Length];
                    await stream.ReadAsync(bytes, 0, (int)stream.Length);

                    user.Image = Convert.ToBase64String(bytes);
                }
            }

            if (await _authRepository.InsertUser(user)) return user.Id;
            return -1;
        }

        public async Task<int> Login(string username, string password)
        {
            User user = await _authRepository.GetUser(username);
            if (user == null) throw new ArgumentException("Invalid username.");

            using (var hmac = new HMACSHA512(user.PasswordSalt))
            {
                var hashPass = hmac.ComputeHash(Encoding.UTF8.GetBytes(password));

                if (!hashPass.SequenceEqual(user.Password)) throw new ArgumentException("Invalid password");
            }

            return user.Id;
        }
    }
}
