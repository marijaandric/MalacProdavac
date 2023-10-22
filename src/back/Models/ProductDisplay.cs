namespace back.Models
{
    public class ProductDisplay
    {
        public int Id { get; set; }
        public int ShopId { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }
        public DateTime StartTime { get; set; }
        public DateTime EndTime { get; set; }
        public string? Address { get; set; }
    }
}
