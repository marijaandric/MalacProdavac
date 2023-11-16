using back.BLL.Dtos.Cards;

namespace back.BLL.Dtos.Infos
{
    public class OrderInfo : OrderCard
    {
        public List<OrderItemCard> Items { get; set; }
        public string ShippingAddress { get; set; }
        public string PaymentMethod { get; set; }
        public string DeliveryMethod { get; set; }
    }
}
