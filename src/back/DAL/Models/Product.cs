using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace back.Models
{
    public class Product
    {
        [Key]
        public int Id { get; set; }
        [ForeignKey("Shop")]
        public int ShopId { get; set; }
        public string? Name { get; set; }
        public string? Description { get; set; }
        public float Price { get; set; }
        [ForeignKey("Metric")]
        public int MetricId { get; set; }
        [ForeignKey("Category")]
        public int CategoryId { get; set; }
        [ForeignKey("Subcategory")]
        public float SalePercentage { get; set; }
        public int SaleMinQuantity { get; set; }
        public string? SaleMessage { get; set; }
        public double? Mass { get; set; }

        public Shop Shop { get; set; }
        public Metric Metric { get; set; }
        public Category Category { get; set; }
    }
}
