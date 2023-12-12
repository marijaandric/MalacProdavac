using back.BLL.Dtos;
using back.DAL.Repositories;
using back.Models;
using Microsoft.IdentityModel.Tokens;
using Newtonsoft.Json.Linq;
using System.Globalization;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using System.Text.RegularExpressions;

namespace back.BLL.Services
{
    public class AuthService : IAuthService
    {
        IAuthRepository _authRepository;
        private readonly IConfiguration _config;
        public AuthService(IAuthRepository authRepository, IConfiguration config)
        {
            _authRepository = authRepository;
            _config = config;
        }

        public string defaultImagePath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "..\\..\\..\\images\\default.png");
        string imagesFolderPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "..\\..\\..\\images");

        #region registrationHelperMethods
        public string CreateUsername(string name, string lastname)
        {
            string username = name.ToLower() + "." + lastname.ToLower();
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

        public bool CheckAddress(string address)
        {
            string pattern = @"^[^,]+, [^,]+, [^,]+$";

            Regex regex = new Regex(pattern);

            if (regex.IsMatch(address)) return true;

            return false;
        }

        public async Task<(double, double)> GetCoordinates(string address)
        {
            var bingMapsApiKey = "Aj_nYJhXf_C_QoPf7gOQch6KOhTJo2iX2VIyvOlwb7hDpGCtS8rOhyQYp5kAbR54";

            var urlBuilder = new UriBuilder("http://dev.virtualearth.net/REST/v1/Locations");
            urlBuilder.Query = $"q={Uri.EscapeDataString(address)}&key={bingMapsApiKey}";
            var url = urlBuilder.ToString();

            HttpClient _httpClient = new HttpClient();

            var response = await _httpClient.GetAsync(url);
            var responseString = await response.Content.ReadAsStringAsync();

            var data = JObject.Parse(responseString);
            var latitude = data["resourceSets"][0]["resources"][0]["point"]["coordinates"][0].Value<double>();
            var longitude = data["resourceSets"][0]["resources"][0]["point"]["coordinates"][1].Value<double>();


            return (latitude, longitude);
        }

        public async Task<string> CreateToken(User user)
        {
            List<Claim> claims = new List<Claim>
            {
                new Claim("name", user.Username),
                new Claim("role", user.RoleId.ToString()),
                new Claim("sub", user.Id.ToString())

            };

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config.GetSection("AppSettings:Key").Value));
            var cred = new SigningCredentials(key, SecurityAlgorithms.HmacSha256Signature);
            var token = new JwtSecurityToken(
                issuer: _config.GetSection("AppSettings:Issuer").Value,
                audience: _config.GetSection("AppSettings:Audience").Value,
                claims: claims, expires: DateTime.Now.AddMinutes(int.Parse(_config.GetSection("AppSettings:AccessTokenValidity").Value)),
                signingCredentials: cred
            );
            var jwt = new JwtSecurityTokenHandler().WriteToken(token);

            return jwt;
        }

        public RefreshToken GenerateRefreshToken()
        {
            return new RefreshToken
            {
                Token = Convert.ToBase64String(RandomNumberGenerator.GetBytes(64)),
                Created = DateTime.Now,
                Expires = DateTime.Now.AddDays(int.Parse(_config.GetSection("AppSettings:RefreshTokenValidity").Value))
            };
        }
        #endregion

        public async Task<string> Register(UserDto userDto)
        {
            #region validation
            string checkEmail = CheckEmail(userDto.Email.ToLower());
            string checkPassword = CheckPassword(userDto.Password);

            if (!checkEmail.Equals("")) throw new ArgumentException(checkEmail);
            if (!checkPassword.Equals("")) throw new ArgumentException(checkPassword);
            if (!CheckAddress(userDto.Address)) throw new ArgumentException("Invalid form. \nRequired form: Street, City, Country");
            #endregion

            TextInfo textInfo = CultureInfo.CurrentCulture.TextInfo;

            User user = new User();

            user.Name = textInfo.ToTitleCase(userDto.Name);
            user.Lastname = textInfo.ToTitleCase(userDto.Lastname);
            user.Username = CreateUsername(user.Name, user.Lastname);
            user.Email = userDto.Email.ToLower();
            user.Address = userDto.Address;
            user.RoleId = userDto.RoleId;
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

            if (File.Exists(defaultImagePath)) user.Image = "default.png";

            var coordinates = await GetCoordinates(userDto.Address);
            user.Latitude = (float)coordinates.Item1;
            user.Longitude = (float)coordinates.Item2;

            if (await _authRepository.InsertUser(user)) return await CreateToken(user);
            return "";
        }

        //treba da vraca string
        public async Task<string> Login(LoginDto loginDto)
        {
            User user = await _authRepository.GetUser(loginDto.username);
            if (user == null) throw new ArgumentException("Invalid username.");

            using (var hmac = new HMACSHA512(user.PasswordSalt))
            {
                var hashPass = hmac.ComputeHash(Encoding.UTF8.GetBytes(loginDto.password));

                if (!hashPass.SequenceEqual(user.Password)) throw new ArgumentException("Invalid password");
            }

            return await CreateToken(user);
        }

        public async Task<string> EditUser(EditUserDto values)
        {
            User newUser = await _authRepository.GetUser(values.Id);
            if (values.Address != null && values.Name != null && values.Username != null && values.RoleId != null && values.Address == newUser.Address && values.Name == newUser.Name && values.Username == newUser.Username && values.RoleId == newUser.RoleId) return await CreateToken(newUser);
            if (values.Username.Length > 0)
            {
                if (_authRepository.CountUsers(values.Username) == 0) newUser.Username = values.Username;
                else throw new ArgumentException("Username taken!");
            }

            if (values.Address.Length > 0)
            {
                if (CheckAddress(values.Address))
                {
                    newUser.Address = values.Address;
                    (double, double) coordinates = await GetCoordinates(values.Address);
                    newUser.Latitude = (float)coordinates.Item1;
                    newUser.Longitude = (float)coordinates.Item2;
                }
                else throw new ArgumentException("Invalid address!");
            }


            if (values.RoleId != null) newUser.RoleId = (int)values.RoleId;
            if (values.Name.Length > 0) newUser.Name = values.Name;

            if (await _authRepository.EditUser(newUser)) return await CreateToken(newUser);
            else throw new ArgumentException("Changes could not be saved!");
        }

        public async Task<bool> ChangePassword(ChangePasswordDto values)
        {
            User user = await _authRepository.GetUser(values.Id);

            if (values.Password.Length == 0 || values.OldPassword.Length == 0) throw new ArgumentException("Check fields and try again!");

            using (HMACSHA512 hmac = new HMACSHA512(user.PasswordSalt))
            {
                if (hmac.ComputeHash(Encoding.UTF8.GetBytes(values.OldPassword)).SequenceEqual(user.Password)) user.Password = hmac.ComputeHash(Encoding.UTF8.GetBytes(values.Password));
                else throw new ArgumentException("Passwords do not match!");
            };

            return await _authRepository.EditUser(user);
        }

        public async Task<bool> DeleteProfilePhoto(int userId)
        {
            string img = await _authRepository.DeleteProfilePhoto(userId);
            if (img == null) throw new ArgumentException("No image found!");

            string path = Path.Combine(imagesFolderPath, img);

            if (File.Exists(path))
            {
                File.Delete(path);
                return true;
            }

            return false;
        }

        public async Task<bool> SaveFcmToken(int id, string token)
        {
            return await _authRepository.SaveFcmToken(id, token);
        }

        public async Task<bool> SaveLatestCoordinates(int userId, float lat, float lon)
        {
            if (!await _authRepository.SaveLatestCoordinates(userId, lat, lon)) throw new ArgumentException("Coordinates could not be saved!");
            return true;
        }
    }
}
