namespace back.BLL.Dtos
{
    public class ChangePasswordDto
    {
        public int Id { get; set; }
        public string Password { get; set; }
        public string OldPassword { get; set; }
    }
}
