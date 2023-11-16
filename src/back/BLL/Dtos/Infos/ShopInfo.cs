using back.BLL.Dtos.HelpModels;
using back.Models;

namespace back.BLL.Dtos.Infos
{
    public class ShopInfo : Shop
    {
        public float Rating { get; set; }
        public bool Liked { get; set; }
        public bool BoughtFrom { get; set; }
        public bool Rated { get; set; }
        public bool IsOwner { get; set; }
        public List<string> Categories { get; set; }
        public List<string> Subcategories { get; set; }
        public List<WorkingHours> WorkingHours { get; set; }
        public List<ShopReviewExtended> Reviews { get; set; }
    }
}
