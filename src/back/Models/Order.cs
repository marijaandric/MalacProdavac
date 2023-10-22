namespace back.Models
{
    public class Order
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public DateTime CreatedOn { get; set; }
        public int PaymentMethod { get; set; }
        public int DeliveryMethod { get; set; }
        public int Status { get; set; }

    }
}
