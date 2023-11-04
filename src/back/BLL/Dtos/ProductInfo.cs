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
        public List<WorkingHours> WorkingHours { get; set; }
        public List<ProductReview> Reviews { get; set; }
        public List<(ProductQuestion, ProductAnswer)> QuestionsAndAnswers { get; set; }
        public Dictionary<string, int> Sizes { get; set; }

    }
}
