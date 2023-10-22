namespace back.Models
{
    public class WorkingHours
    {
        public int ShopId { get; set; }
        public DateTime Day {  get; set; }
        public DateTime OpeningHours { get; set; }
        public DateTime ClosingHours { get; set;}
    }
}
