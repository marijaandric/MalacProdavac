using back.BLL.Dtos.HelpModels;

namespace back.BLL.Dtos
{
    public class EditShopDto
    {
        public int Id { get; set; }
        public string? Name { get; set; }
        public string? Address { get; set; }
        public List<int>? Categories { get; set; }
        public List<WorkingHoursDto>? WorkingHours { get; set; }
        public int? PIB { get; set; }
        public string? AccountNumber { get; set; }
    }
}
