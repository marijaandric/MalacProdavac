using System.ComponentModel.DataAnnotations.Schema;

namespace back.Models
{
    public class Cart
    {
        [ForeignKey("User")]
        public int UserId { get; set; }
        [ForeignKey("Product")]
        public int ProductId { get; set; }
        public int Quantity { get; set; }

        public User User { get; set; }
        public Product Product { get; set; }
    }
}
