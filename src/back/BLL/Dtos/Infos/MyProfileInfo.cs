using back.Models;

namespace back.BLL.Dtos.Infos
{
    public class MyProfileInfo
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Image { get; set; }
        public string Role { get; set; }
        public int RoleId { get; set; }
        public string Username { get; set; }
        public string Address { get; set; }
        public string Email { get; set; }
        public Rating Rating { get; set; }
        public DateTime? CreatedOn { get; set; }
        public float MoneySpent { get; set; }
        public float MoneyEarned { get; set; }
    }
}
