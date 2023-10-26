using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace back.Models
{
    [PrimaryKey(nameof(ShopId), nameof(UserId))]
    public class LikedShops
    {
        [ForeignKey("Shop")]
        public int ShopId { get; set; }
        [ForeignKey("User")]
        public int UserId { get; set; }

        public Shop Shop { get; set; }
        public User User { get; set; }
    }
}
