using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace back.Models
{
    [PrimaryKey(nameof(ShopId), nameof(SubcategoryId))]
    public class ShopSubcategory
    {
        [ForeignKey("Shop")]
        public int ShopId { get; set; }
        [ForeignKey("Subcategory")]
        public int SubcategoryId { get; set; }

        public Shop Shop { get; set; }
        public Subcategory Subcategory { get; set; }
    }
}
