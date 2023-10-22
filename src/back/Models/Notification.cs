namespace back.Models
{
    public class Notification
    {
        public int Id { get; set; }
        public string? Title { get; set; }
        public string? Text { get; set; }
        public int TypeId { get; set; }
        public DateTime? CreatedOn { get; set; }
    }
}
