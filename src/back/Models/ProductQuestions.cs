namespace back.Models
{
    public class ProductQuestions
    {
        public int Id { get; set; }
        public int PosterId { get; set; }
        public int ProductId { get; set; }
        public string? QuestionText { get; set; }
        public DateTime PostedOn { get; set; }
    }
}
