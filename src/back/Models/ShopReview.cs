namespace back.Models
{
    public class ShopReview
    {
        public int ReviewerId { get; set; }
        public int ShopId { get; set; }
        public float Rating { get; set; }
        public string? Comment { get; set; }
        public DateTime? PostedOn { get; set; }
    }
}
