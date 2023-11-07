namespace back.BLL.Dtos.HelpModels
{
    public class QuestionWithAnswer
    {
        public int QuestionId { get; set; }
        public int AnswerId { get; set; }
        public string QuestionText { get; set; }
        public string AnswerText { get; set; }
    }
}
