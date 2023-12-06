using back.DAL.Models;
using back.Models;

namespace back.BLL.Services
{
    public interface IHelperService
    {
        public Task<bool> UploadImage(IFormFile image, int type, int id);
        public Task<string> GeneratePaymentSlip(int userId, int shopId, float amount, string? address);
        public Task<double> Route(string start, string end, string shop, string shipping);
        public Task<List<Metric>> GetMetrics();
        public Task<List<Size>> GetSizes();
        public Task<List<(double, double)>> GetWaypoints(string routeStart, string routeEnd, List<string>? waypoints);
        public Task<bool> NearRoute(List<(double, double)> routeWaypoints, double shopLat, double shopLong, double range);
    }
}
