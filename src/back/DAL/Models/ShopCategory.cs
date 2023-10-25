using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace back.Models
{
    [PrimaryKey(nameof(ShopId), nameof(CategoryId))]
    public class ShopCategory
    {
        [ForeignKey("Shop")]
        public int ShopId { get; set; }
        [ForeignKey("Category")]
        public int CategoryId { get; set; }

        public Shop Shop { get; set; }
        public Category Category { get; set; }
    }
}
