using back.BLL.Dtos.HelpModels;
using back.DAL.Models;
using back.Models;

namespace back.BLL.Dtos.Infos
{
    public class ProductInfo : Product
    {
        public string ShopName { get; set; }
        public string Metric { get; set; }
        public string Category { get; set; }
        public string Subcategory { get; set; }
        public bool Liked { get; set; }
        public bool Bought { get; set; }
        public bool Rated { get; set; }
        public bool IsOwner { get; set; }
        public float Rating { get; set; }
        public List<WorkingHours> WorkingHours { get; set; }
        public List<QuestionWithAnswer> QuestionsAndAnswers { get; set; }
        public List<Stock> Sizes { get; set; }
        public List<ImageData> Images { get; set; }
        public List<ProductSubscription> Subscriptions { get; set; }
    }
}
