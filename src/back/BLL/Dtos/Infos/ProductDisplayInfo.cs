namespace back.BLL.Dtos.Infos
{
    public class ProductDisplayInfo
    {
        public int Id { get; set; }
        public int ShopId { get; set; }
        public string Address {  get; set; }
        public string EndDate { get; set; }
        public TimeSpan EndTime { get; set; }
        public string StartDate { get; set; }
        public TimeSpan StartTime { get; set; }
        public float Latitude { get; set; }
        public float Longitude { get; set; }

    }
}
