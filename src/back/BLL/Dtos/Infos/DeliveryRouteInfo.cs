using back.BLL.Dtos.HelpModels;

namespace back.BLL.Dtos.Infos
{
    public class DeliveryRouteInfo
    {
        public string Locations { get; set; }
        public string StartDate { get; set; }
        public TimeSpan StartTime { get; set; }
        public List<DeliveryStop> Stops { get; set; }
    }
}
