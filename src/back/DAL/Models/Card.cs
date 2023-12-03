using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace back.Models
{
    [PrimaryKey(nameof(Id))]
    public class Card
    {
        public int Id { get; set; }
        [ForeignKey(nameof(OwnerId))]
        public int OwnerId { get; set; }
        public string Fullname { get; set; }
        public string CardNumber { get; set; }
        public DateTime ExpirationDate { get; set; }
        public string CVV { get; set; }

        public User Owner { get; set; }
    }
}
