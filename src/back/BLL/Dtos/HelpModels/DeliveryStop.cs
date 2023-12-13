namespace back.BLL.Dtos.HelpModels
{
    public class DeliveryStop
    {
        public float Latitude { get; set; }
        public float Longitude { get; set; }
        public string Address { get; set; }
        public string? ShopName { get; set; }
        public List<DeliveryItem>? Items { get; set; }
    }
}
