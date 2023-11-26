using back.BLL.Dtos.HelpModels;

namespace back.BLL.Dtos
{
    public class ProductDto
    {
        public string Name { get; set; }
        public string Description { get; set; }
        public int MetrticId { get; set; }
        public float Price { get; set; }
        public float SalePercentage { get; set; }
        public int SaleMinQuantity { get; set; }
        public string? SaleMessage { get; set; }
        public int CategoryId { get; set; }
        public int ShopId { get; set; }
        public double? Mass { get; set; }
        public List<StockDto> Sizes { get; set; }
    }
}
