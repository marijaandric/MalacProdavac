namespace back.BLL.Dtos.Cards
{
    public class DeliveryRouteCard : DeliveryRequestCard
    {

        public TimeSpan StartTime { get; set; }
        public double Cost { get; set; }
    }
}
