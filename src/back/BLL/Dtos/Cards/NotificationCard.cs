namespace back.BLL.Dtos.Cards
{
    public class NotificationCard
    {
        public int Id { get; set; }
        public string Title { get; set; }
        public string Text { get; set; }
        public int TypeId { get; set; }
        public DateTime? CreatedOn { get; set; }
        public int? ReferenceId { get; set; }
        public bool Read { get; set; }

    }
}
