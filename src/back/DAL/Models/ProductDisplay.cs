using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace back.Models
{
    public class ProductDisplay
    {
        [Key]
        public int Id { get; set; }
        [ForeignKey("Shop")]
        public int ShopId { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }
        public TimeSpan StartTime { get; set; }
        public TimeSpan EndTime { get; set; }
        public string? Address { get; set; }

        public Shop Shop { get; set; }
    }
}
