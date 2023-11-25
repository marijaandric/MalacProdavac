using back.BLL.Dtos;
using back.DAL.Repositories;
using Newtonsoft.Json.Linq;
using System.Drawing;
using System.Drawing.Imaging;
using iText.Kernel.Pdf;
using iText.Layout;
using iText.Layout.Element;
using iText.IO.Image;

namespace back.BLL.Services
{
    public class HelperService : IHelperService
    {
        IShopRepository _shopRepository;
        IProductRepository _productRepository;
        IAuthRepository _authRepository;
        IOrderRepository _orderRepository;
        
        public string folderPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "..\\..\\..\\images");

        public HelperService(IShopRepository shopRepository, IProductRepository productRepository, IAuthRepository authRepository, IOrderRepository orderRepository)
        {
            _shopRepository = shopRepository;
            _productRepository = productRepository;
            _authRepository = authRepository;
            _orderRepository = orderRepository;
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

        public async Task<string> GeneratePaymentSlip(int userId, int shopId, float amount, string? address)
        {

            PaymentSlipInfo info = await _orderRepository.GetPaymentSlipInfo(userId, shopId);
            if (address != null) info.Address = address;

            string imagePath = Path.Combine(folderPath, "template.jpg");
            string output, outputPath;

            using (Bitmap bitmap = new Bitmap(imagePath))
            {
                using (var graphics = Graphics.FromImage(bitmap))
                {
                    Font font = new Font("Arial", 10, FontStyle.Regular, GraphicsUnit.Pixel);

                    string snd = info.Name + " " + info.Lastname + "\n" + info.Address;
                    string purpose = "Placanje porudzbine";
                    string rcv = info.NameRcv + " " + info.LastnameRcv + "\n" + info.AddressRcv;
                    string amountStr = "= " + amount.ToString("N2", new System.Globalization.CultureInfo("de-DE"));
                    string rcvnum = info.AccountNumberRcv;
                    string currency = "RSD";

                    SolidBrush brush = new SolidBrush(Color.Black);
                    graphics.DrawString(snd, font, brush, new PointF(35, 53));
                    graphics.DrawString(purpose, font, brush, new PointF(35, 115));
                    graphics.DrawString(rcv, font, brush, new PointF(35, 177));
                    graphics.DrawString(amountStr, font, brush, new PointF(458, 66));
                    graphics.DrawString(currency, font, brush, new PointF(400, 66));
                    graphics.DrawString(rcvnum, font, brush, new PointF(400, 101));

                    output = GetUniqueFileName("payment_slip.png");
                    outputPath = Path.Combine(folderPath, output);
                    bitmap.Save(outputPath, ImageFormat.Png);
                }
            }

            string pdfOutput = output.Substring(0, output.LastIndexOf(".")) + ".pdf";

            using (var pdfWriter = new PdfWriter(Path.Combine(folderPath, pdfOutput)))
            {
                using (var pdfDocument = new PdfDocument(pdfWriter))
                {
                    using (var document = new Document(pdfDocument))
                    {
                        iText.Layout.Element.Image img = new iText.Layout.Element.Image(ImageDataFactory.Create(outputPath));
                        document.Add(img);
                    }
                }
            }

            File.Delete(outputPath);

            return pdfOutput;
        }

    }
}
