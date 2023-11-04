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
        public async Task<IActionResult> GetProducts(int userId, [FromQuery] List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page)
        {
            try
            {
                return Ok(await _service.GetProducts(userId, categories, rating, open, range, location, sort, search, page));
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
                { 0 , "Default" },
                { 1 , "Price (lowest first)"},
                { 2 , "Price (highest first)" },
                { 3 , "Alphabetically (ascending)" },
                { 4 , "Alphabetically (descending)" },
                { 5 , "Rating (lowest first)" },
                { 6 , "Rating (highest first)" }
            });
        }

        [HttpGet("ProductPages")]
        public IActionResult ProductPages()
        {
            try
            {
                return Ok( new { PageCount = _service.ProductPages() });
            }
            catch (Exception ex)
            {
                return BadRequest(new { ErrorMessage = ex.Message });
            }
        }

        [HttpGet("ProductDetails")]
        public async Task<IActionResult> ProductDetails(int productId, int userId)
        {
            try
            {
                return Ok(new { Details = await _service.ProductDetails(productId, userId) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { ErrorMessage = ex.Message });
            }
        }
    }
}
