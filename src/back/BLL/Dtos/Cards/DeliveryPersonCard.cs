namespace back.BLL.Dtos.Cards
{
    public class DeliveryPersonCard
    {
        public string DeliveryPerson { get; set; }
        public int DeliveryPersonId { get; set; }
        public double Price { get; set; }
        public DateTime Date { get; set; }
        public double ClosestRouteDivergence { get; set; }
    }
}
