using back.Models;

namespace back.BLL.Dtos.HelpModels
{
    public class ShopReviewExtended : ShopReview
    {
        public string Username { get; set; }
        public string Image { get; set; }
    }
}
