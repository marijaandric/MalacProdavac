using back.BLL.Dtos.HelpModels;

namespace back.BLL.Dtos
{
    public class OrderDto
    {
        public int UserId { get; set; }
        public int ShopId { get; set; }
        public int PaymentMethod {  get; set; }
        public int DeliveryMethod { get; set; }
        public string? ShippingAddress { get; set; }
        public DateTime? PickupTime { get; set; }
        public List<ItemQuantity> Products { get; set; }
    }
}
