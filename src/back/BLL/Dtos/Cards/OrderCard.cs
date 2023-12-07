namespace back.BLL.Dtos.Cards
{
    public class OrderCard
    {
        public int Id { get; set; }
        public int Quantity { get; set; }
        public float Amount { get; set; }
        public string Status { get; set; }
        public string CreatedOn { get; set; }
    }
}
