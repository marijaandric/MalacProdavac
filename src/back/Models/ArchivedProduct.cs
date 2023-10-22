using System.ComponentModel.DataAnnotations.Schema;

namespace back.Models
{
    public class ArchivedProduct
    {
        public int Id { get; set; }
        [ForeignKey("Shop")]
        public int ShopId { get; set; }
        [ForeignKey("Metric")]
        public string? Name { get; set; }
        public int MetricId { get; set; }

        public Shop Shop { get; set; }
        public Metric Metric { get; set; }
    }
}
