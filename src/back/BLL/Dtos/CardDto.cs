namespace back.BLL.Dtos
{
    public class CardDto
    {
        public int OwnerId { get; set; }
        public string FullName { get; set; }
        public string CardNumber { get; set; }
        public string ExpirationDate { get; set; }
        public string CVV { get; set; }
    }
}
