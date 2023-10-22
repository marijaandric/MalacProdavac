namespace back.Models
{
    public class Shop
    {
        public int Id { get; set; }
        public int OwnerId { get; set; }
        public string? Name { get; set; }
        public string? Address { get; set; }
        public float? Latitude { get; set; }
        public float? Longitude { get; set; }
        public string? Image {  get; set; }

        public ICollection<ArchivedProduct> ArchivedProducts { get; set; }
    }
}
