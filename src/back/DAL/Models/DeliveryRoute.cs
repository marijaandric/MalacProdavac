using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace back.Models
{
    public class DeliveryRoute
    {
        [Key]
        public int Id { get; set; }
        [ForeignKey("DeliveryPerson")]
        public int DeliveryPersonId { get; set; }
        public DateTime StartDate { get; set; }
        public TimeSpan StartTime { get; set; }
        public string StartLocation { get; set; }
        public string EndLocation { get; set; }
        public double StartLatitude { get; set; }
        public double StartLongitude { get; set; }
        public double EndLatitude { get; set; }
        public double EndLongitude { get; set; }
        public float FixedCost { get; set; }
        public bool Finished { get; set; }

        public User DeliveryPerson { get; set; }
    }
}
