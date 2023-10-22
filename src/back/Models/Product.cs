namespace back.Models
{
    public class Product
    {
        public int Id { get; set; }
        public int ShopId { get; set; }
        public string? Name { get; set; }
        public string? Description { get; set; }
        public float Price { get; set; }
        public int Stock {  get; set; }
        public int MetricId { get; set; }
        public int CategoryId { get; set; }
        public int SubcategoryId { get; set; }
        public float SalePercentage { get; set; }
        public int SaleMinQuantity { get; set; }
        public string? SaleMessage { get; set; }
    }
}
