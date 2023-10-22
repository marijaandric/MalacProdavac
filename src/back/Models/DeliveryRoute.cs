namespace back.Models
{
    public class DeliveryRoute
    {
        public int Id { get; set; }
        public int DeliveryPersonId { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime StartTime { get; set; }
    }
}
