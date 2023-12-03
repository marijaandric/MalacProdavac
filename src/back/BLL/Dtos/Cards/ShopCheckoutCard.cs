using back.Models;

namespace back.BLL.Dtos.Cards
{
    public class ShopCheckoutCard
    {
        public int Id {  get; set; }
        public string Name { get; set; }
        public string Address { get; set; }
        public string? Image { get; set; }
        public List<WorkingHours> WorkingHours { get; set; }
        public float? Latitude { get; set; }
        public float? Longitude { get; set; }
    }
}
