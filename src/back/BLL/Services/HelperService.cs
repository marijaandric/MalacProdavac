using back.DAL.Repositories;
using Newtonsoft.Json.Linq;

namespace back.BLL.Services
{
    public class HelperService : IHelperService
    {
        IShopRepository _shopRepository;
        IProductRepository _productRepository;
        IAuthRepository _authRepository;
        
        public string folderPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "..\\..\\..\\images");

        public HelperService(IShopRepository shopRepository, IProductRepository productRepository, IAuthRepository authRepository)
        {
            _shopRepository = shopRepository;
            _productRepository = productRepository;
            _authRepository = authRepository;
        }

        public async Task<bool> UploadImage(IFormFile image, int type, int id)
        {
            if (!IsImageFile(image.FileName))
            {
                throw new ArgumentException("Invalid file format. Image files only.");
            }

            string uniqueFileName = GetUniqueFileName(image.FileName);

            string path = Path.Combine(folderPath, uniqueFileName);

            using (var stream = new FileStream(path, FileMode.Create))
            {
                image.CopyTo(stream);
            }

            if (!File.Exists(path))
            {
                throw new ArgumentException("Image could not be saved!");
            }

            switch (type)
            {
                case 0:
                    return await _authRepository.ChangeProfilePhoto(id, uniqueFileName);
                case 1:
                    return await _productRepository.AddProductPhoto(id, uniqueFileName);
                case 2:
                    return await _shopRepository.ChangeShopPhoto(id, uniqueFileName);
                default:
                    throw new ArgumentException("Invalid type!");
            }
        }

        private bool IsImageFile(string fileName)
        {
            string[] allowedExtensions = { ".jpg", ".jpeg", ".png", ".gif" };
            string extension = Path.GetExtension(fileName).ToLower();

            return allowedExtensions.Contains(extension);
        }

        private string GetUniqueFileName(string fileName)
        {
            string fileNameWithoutExtension = Path.GetFileNameWithoutExtension(fileName);
            string extension = Path.GetExtension(fileName);
            string uniqueFileName = $"{fileNameWithoutExtension}_{DateTime.Now:yyyyMMddHHmmssfff}{extension}";

            return uniqueFileName;
        }

        public static async Task<(double, double)> GetCoordinates(string address)
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


    }
}
