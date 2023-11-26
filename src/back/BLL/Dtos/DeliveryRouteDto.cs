namespace back.BLL.Dtos
{
    public class DeliveryRouteDto
    {
        public int UserId { get; set; }
        public DateTime StartDate { get; set; }
        public string StartTime {  get; set; }
        public string StartLocation { get; set; }
        public string EndLocation { get; set; }
    }
}
