using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace back.Models
{
    [PrimaryKey(nameof(ReviewerId), nameof(ShopId))]
    public class ShopReview
    {
        [ForeignKey("Reviewer")]
        public int ReviewerId { get; set; }
        [ForeignKey("Shop")]
        public int ShopId { get; set; }
        public float Rating { get; set; }
        public string? Comment { get; set; }
        public DateTime? PostedOn { get; set; }

        public User Reviewer { get; set; }
        public Shop Shop { get; set; }
    }
}
