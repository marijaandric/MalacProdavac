using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace back.Models
{
    [PrimaryKey(nameof(ReviewerId), nameof(ProductId))]
    public class ProductReview
    {
        [ForeignKey("Reviewer")]
        public int ReviewerId { get; set; }
        [ForeignKey("Product")]
        public int ProductId { get; set; }
        public float Rating { get; set; }
        public string? Comment { get; set; }
        public DateTime? PostedOn { get; set; }

        public User Reviewer { get; set; }
        public Product Product { get; set; }
    }
}
