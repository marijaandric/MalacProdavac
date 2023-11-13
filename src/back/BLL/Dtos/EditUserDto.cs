namespace back.BLL.Dtos
{
    public class EditUserDto
    {
        public int Id { get; set; }
        public string? Username { get; set; }
        public string? Address { get; set; }
        public int? RoleId { get; set; }
        public string? Image {  get; set; }
        public string? Name { get; set; }
    }
}
