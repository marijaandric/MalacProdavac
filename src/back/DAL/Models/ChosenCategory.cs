using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace back.Models
{
    [PrimaryKey(nameof(UserId), nameof(CategoryId))]
    public class ChosenCategory
    {
        public int UserId { get; set; }
        public int CategoryId { get; set; }

    }
}
