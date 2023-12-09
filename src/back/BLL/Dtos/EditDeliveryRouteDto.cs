namespace back.BLL.Dtos
{
    public class EditDeliveryRouteDto
    {
        public int Id { get; set; }
        public string? StartDate { get; set; }
        public string? StartTime { get; set; }
        public string? StartLocation { get; set; }
        public string? EndLocation { get; set; }
        public float? FixedCost { get; set; }
    }
}
