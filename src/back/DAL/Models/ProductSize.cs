using System.ComponentModel.DataAnnotations.Schema;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Models
{
    [PrimaryKey(nameof(ProductId), nameof(SizeId))]
    public class ProductSize
    {
        [ForeignKey("Product")]
        public int ProductId { get; set; }
        [ForeignKey("Size")]
        public int SizeId { get; set; }
        public int Stock {  get; set; }

        public Product Product { get; set; }
        //public Size Size { get; set; }
    }
}
