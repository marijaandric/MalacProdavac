using back.BLL.Dtos;
using back.BLL.Services;
using Microsoft.AspNetCore.Mvc;

namespace back.PL.Controllers
{
    [Route("back/[controller]")]
    [ApiController]
    public class ProductController : Controller
    {
        IProductService _service;
        public ProductController(IProductService productService)
        {
            _service = productService;
        }

        [HttpGet("GetProducts")]
        public async Task<IActionResult> GetProducts(int userId, [FromQuery] List<int> categories, int rating, bool open, int range, string location, int sort, string search)
        {
            try
            {
                return Ok(await _service.GetProducts(userId, categories, rating, open, range, location, sort, search));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("SortingOptions")]
        public IActionResult SortingOptions()
        {
            return Ok(new Dictionary<int, string>
            {
                { 1 , "Price (lowest first)"},
                { 2 , "Price (highest first)" },
                { 3 , "Alphabetically (ascending)" },
                { 4 , "Alphabetically (descending)" },
            });
        }
    }
}
