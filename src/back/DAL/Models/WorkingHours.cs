using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace back.Models
{
    [PrimaryKey(nameof(ShopId), nameof(Day))]
    public class WorkingHours
    {
        [ForeignKey("Shop")]
        public int ShopId { get; set; }
        public DateTime Day {  get; set; }
        public DateTime OpeningHours { get; set; }
        public DateTime ClosingHours { get; set;}

        public Shop Shop { get; set; }
    }
}
