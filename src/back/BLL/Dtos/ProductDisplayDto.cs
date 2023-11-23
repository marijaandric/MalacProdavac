namespace back.BLL.Dtos
{
    public class ProductDisplayDto
    {
        public int ShopId { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }
        public string StartTime { get; set; }
        public string EndTime { get; set; }
        public string? Address { get; set; }
    }
}
