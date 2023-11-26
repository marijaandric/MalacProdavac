namespace back.BLL.Dtos.Cards
{
    public class DeliveryRequestCard
    {
        public int Id { get; set; }
        public string Locations { get; set; }
        public DateTime? CreatedOn { get; set; }
        public string StartAddress { get; set; }
        public string EndAddress { get; set; }
        public double RouteDivergence { get; set; }
    }
}
