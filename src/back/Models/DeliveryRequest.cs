namespace back.Models
{
    public class DeliveryRequest
    {
        public int Id { get; set; }
        public int RouteId { get; set; }
        public int ShopId { get; set; }
        public int OrderId { get; set; }
        public DateTime? PickupDate { get; set; }
        public DateTime CreatedOn { get; set; }
        //status?
    }
}
