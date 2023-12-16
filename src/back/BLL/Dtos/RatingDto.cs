namespace back.BLL.Dtos
{
    public class RatingDto
    {
        public int RaterId { get; set; }
        public int RatedId { get; set; }
        public float Communication {  get; set; }
        public float Reliability { get; set; }
        public float OverallExperience { get; set; }
    }
}
