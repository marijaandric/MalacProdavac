namespace back.BLL.Dtos.Cards
{
    public class ProductCard
    {
        public int Id { get; set; }
        public int ShopId { get; set; }
        public string? Name { get; set; }
        public float Price { get; set; }
        public float Rating { get; set; }
        public string Image { get; set; }
    }
}
