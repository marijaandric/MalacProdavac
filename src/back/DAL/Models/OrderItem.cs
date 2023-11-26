using System.ComponentModel.DataAnnotations.Schema;
using back.DAL.Models;
using Microsoft.EntityFrameworkCore;

namespace back.Models
{
    [PrimaryKey(nameof(OrderId), nameof(ProductId), nameof(SizeId))]
    public class OrderItem
    {
        [ForeignKey("Order")]
        public int OrderId { get; set; }
        [ForeignKey("Product")]
        public int ProductId { get; set; }
        public int Quantity { get; set; }
        public float Price { get; set; }
        [ForeignKey("Size")]
        public int SizeId { get; set; }


        public Order Order { get; set; }
        public Product Product { get; set; }
       // public Size Size { get; set; }
    }
}
