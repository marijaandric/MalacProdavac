using back.DAL.Models;
using back.Models;

namespace back.BLL.Dtos
{
    public class ProductInfo : Product
    {
        public string ShopName { get; set; }
        public string Metric {  get; set; }
        public string Category { get; set; }
        public string Subcategory { get; set; }
        public bool Liked { get; set; }
        public bool Bought { get; set; }
        public bool Rated { get; set; }
        public bool IsOwner { get; set; }
        public float Rating { get; set; }
        public List<WorkingHours> WorkingHours { get; set; }
        public List<ProductReview> Reviews { get; set; }
        public List<(ProductQuestion, ProductAnswer)> QuestionsAndAnswers { get; set; }
        public Dictionary<string, int> Sizes { get; set; }
        public List<string> Images { get; set; }

    }
}
