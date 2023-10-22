namespace back.Models
{
    public class ProductAnswers
    {
        public int Id { get; set; }
        public int PosterId { get; set; }
        public int QuestionId { get; set; }
        public string? AnswerText { get; set; }
        public DateTime PostedOn { get; set; }
    }
}
