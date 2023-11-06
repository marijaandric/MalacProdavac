using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace back.Models
{
    [PrimaryKey(nameof(ShopId), nameof(Day))]
    public class WorkingHours
    {
        [ForeignKey("Shop")]
        public int ShopId { get; set; }
        public DayOfWeek Day {  get; set; }
        public TimeSpan OpeningHours { get; set; }
        public TimeSpan ClosingHours { get; set;}

        public Shop Shop { get; set; }
    }
}
