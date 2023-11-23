using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace back.Models
{
    public class Order
    {
        [Key]
        public int Id { get; set; }
        [ForeignKey("User")]
        public int UserId { get; set; }
        public DateTime CreatedOn { get; set; }
        [ForeignKey("PaymentMethod")]
        public int PaymentMethodId { get; set; }
        [ForeignKey("DeliveryMethod")]
        public int DeliveryMethodId { get; set; }
        [ForeignKey("Status")]
        public int StatusId { get; set; }
        public string ShippingAddress { get; set; }
        public User User { get; set; }
        public PaymentMethod PaymentMethod { get; set; }
        public DeliveryMethod DeliveryMethod { get; set; }
        public OrderStatus Status {  get; set; }

    }
}
