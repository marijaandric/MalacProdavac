using System.ComponentModel.DataAnnotations;

namespace back.DAL.Models
{
    public class Size
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
    }
}
