using back.BLL.Dtos;
using back.DAL.Repositories;
using Newtonsoft.Json.Linq;
using System.Drawing;
using System.Drawing.Imaging;
using iText.Kernel.Pdf;
using iText.Layout;
using iText.IO.Image;
using back.Models;

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

        public static double CalculateDistance(double lat1, double lon1, double lat2, double lon2)
        {
            const double radiusOfEarth = 6371;

            // degrees -> radians
            double dLat = Math.PI / 180.0 * (lat2 - lat1);
            double dLon = Math.PI / 180.0 * (lon2 - lon1);

            // Haversine formula
            double a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                Math.Cos(Math.PI / 180.0 * lat1) * Math.Cos(Math.PI / 180.0 * lat2) *
                Math.Sin(dLon / 2) * Math.Sin(dLon / 2);
            double c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));

            double distance = radiusOfEarth * c;

            return distance;
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

        public async Task<double> Route(string start, string end, string shop, string shipping)
        {
            double shopLat, shopLong, shippingLat, shippingLong;
            (double, double) shopcoords = await GetCoordinates(shop);
            shopLat = shopcoords.Item1;
            shopLong = shopcoords.Item2;

            (double, double) shippingCoords = await GetCoordinates(shipping);
            shippingLat = shippingCoords.Item1;
            shippingLong = shippingCoords.Item2;

            using (var client = new HttpClient())
            {
                string apiKey = "Aj_nYJhXf_C_QoPf7gOQch6KOhTJo2iX2VIyvOlwb7hDpGCtS8rOhyQYp5kAbR54";

                string apiUrl = $"https://dev.virtualearth.net/REST/v1/Routes/Driving?wp.0={start}&wp.1={end}&key={apiKey}";

                HttpResponseMessage response = await client.GetAsync(apiUrl);

                if (response.IsSuccessStatusCode)
                {
                    string result = await response.Content.ReadAsStringAsync();
                    JObject json = JObject.Parse(result);

                    double minShopDistance = 100000;
                    double minShippingDistance = 100000;

                    double minShopLat = 0, minShopLong = 0;
                    bool ind = false;

                    foreach (var item in json["resourceSets"][0]["resources"][0]["routeLegs"][0]["itineraryItems"])
                    {
                        double xLat = (double)item["maneuverPoint"]["coordinates"][0];
                        double xLong = (double)item["maneuverPoint"]["coordinates"][1];

                        double distance = CalculateDistance(xLat, xLong, shopLat, shopLong);
                        if (distance < minShopDistance)
                        {
                            minShopDistance = distance;
                            minShopLat = xLat;
                            minShopLong = xLong;
                        }
                        Console.WriteLine("SHOP: " + distance);
                        
                    }

                    foreach (var item in json["resourceSets"][0]["resources"][0]["routeLegs"][0]["itineraryItems"])
                    {
                        double xLat = (double)item["maneuverPoint"]["coordinates"][0];
                        double xLong = (double)item["maneuverPoint"]["coordinates"][1];

                        if (xLat == minShopLat && xLong == minShopLong) ind = true;

                        double distance = CalculateDistance(xLat, xLong, shippingLat, shippingLong);
                        if (ind && distance < minShippingDistance)
                        {
                            minShippingDistance = distance;
                        }
                        Console.WriteLine("ADDRESS: " + distance);
                    }

                    return Math.Round(minShippingDistance + minShopDistance, 1);
                }
            }

            throw new ArgumentException("Error finding route!");

        }

        public async Task<List<Metric>> GetMetrics()
        {
            return await _productRepository.GetMetrics();
        }

        public async Task<List<DAL.Models.Size>> GetSizes()
        {
            return await _productRepository.GetSizes();
        }

        public async Task<bool> NearRoute(string routeStart, string routeEnd, double shopLat, double shopLong, double range)
        {
            using (var client = new HttpClient())
            {
                string apiKey = "Aj_nYJhXf_C_QoPf7gOQch6KOhTJo2iX2VIyvOlwb7hDpGCtS8rOhyQYp5kAbR54";

                string apiUrl = $"https://dev.virtualearth.net/REST/v1/Routes/Driving?wp.0={routeStart}&wp.1={routeEnd}&key={apiKey}";

                HttpResponseMessage response = await client.GetAsync(apiUrl);

                if (response.IsSuccessStatusCode)
                {
                    string result = await response.Content.ReadAsStringAsync();
                    JObject json = JObject.Parse(result);

                    foreach (var item in json["resourceSets"][0]["resources"][0]["routeLegs"][0]["itineraryItems"])
                    {
                        double xLat = (double)item["maneuverPoint"]["coordinates"][0];
                        double xLong = (double)item["maneuverPoint"]["coordinates"][1];

                        double distance = CalculateDistance(xLat, xLong, shopLat, shopLong);
                        
                        if (distance <= range) return true;
                    }
                }
            }

            return false;
        }
    }
}
