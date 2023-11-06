using back.Models;

namespace back.BLL.Dtos
{
    public class ProductReviewExtended : ProductReview
    {
        public string Username { get; set; }
        public string Image {  get; set; }
    }
}
