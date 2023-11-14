using back.DAL.Repositories;
using back.Models;

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

    }
}
