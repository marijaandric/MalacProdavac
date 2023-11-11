using back.Models;

namespace back.BLL.Dtos
{
    public class ShopCard
    {
        //ime, adresa, da li je postavljena kao fav prodavnica i radno vreme
        public int Id { get; set; }
        public string Name { get; set; }
        public string Address { get; set; }
        public float Latitude { get; set; }
        public float Longitude { get; set; }
        public string Image { get; set; }
        public List<WorkingHours> WorkingHours { get; set; }
        public bool Liked { get; set; }
        public float Rating { get; set; }
    }
}
