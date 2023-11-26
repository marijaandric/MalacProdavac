using back.BLL.Dtos.HelpModels;

namespace back.BLL.Dtos
{
    public class EditProductDto
    {
        public int Id { get; set; }
        public string? Name { get; set; }
        public string? Description { get; set; }
        public List<StockDto>? Sizes { get; set; }
        public int? SaleMinQuantity { get; set; }
        public float? SalePercentage { get; set; }
        public string? SaleMessage { get; set; }
    }
}
