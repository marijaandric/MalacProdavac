using back.Models;

namespace back.BLL.Dtos
{
    public class PublicProfileInfo
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Image { get; set; }
        public string Role { get; set; }
        public int RoleId { get; set; }
        public string Username { get; set; }
        public Rating Rating { get; set; }
    }
}
