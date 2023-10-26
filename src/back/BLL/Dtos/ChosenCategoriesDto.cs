namespace back.BLL.Dtos
{
    public class ChosenCategoriesDto
    {
        public int UserId { get; set; }
        public List<int> CategoryIds { get; set; }
    }
}
