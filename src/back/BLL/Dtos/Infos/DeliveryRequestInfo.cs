using back.BLL.Dtos.HelpModels;

namespace back.BLL.Dtos.Infos
{
    public class DeliveryRequestInfo
    {
        public int Id { get; set; }
        public string Locations { get; set; }
        public string ShopAddress { get; set; }
        public string ShippingAddress { get; set; }
        public float ShopLatitude { get; set; }
        public float ShopLongitude { get; set; }
        public float ShippingLatitude { get; set; }
        public float ShippingLongitude { get; set;}
        public List<DeliveryItem> Items { get; set; }
    }
}
