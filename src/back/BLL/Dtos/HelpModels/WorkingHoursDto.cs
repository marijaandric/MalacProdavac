namespace back.BLL.Dtos.HelpModels
{
    public class WorkingHoursDto
    {
        public DayOfWeek Day { get; set; }
        public string OpeningHours { get; set; }
        public string ClosingHours { get; set; }
    }
}
