using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace back.Models
{
    [PrimaryKey(nameof(RaterId), nameof(RatedId))]
    public class Rating
    {
        [ForeignKey("Rater")]
        public int RaterId { get; set; }
        [ForeignKey("Rated")]
        public int RatedId { get; set; }
        public float Communication {  get; set; }
        public float Reliability { get; set; }
        public float OverallExperience { get; set; }
        public float Average {  get; set; }

        public User Rater { get; set; }
        public User Rated { get; set; }
    }
}
