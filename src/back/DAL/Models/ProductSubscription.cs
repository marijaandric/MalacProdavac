using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Models
{
    [PrimaryKey(nameof(ProductId), nameof(UserId))]
    public class ProductSubscription
    {
        [ForeignKey(nameof(ProductId))]
        public int ProductId { get; set; }
        [ForeignKey(nameof(UserId))]
        public int UserId { get; set; }
    }
}
