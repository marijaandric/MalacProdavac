using back.BLL.Services;
using Microsoft.AspNetCore.Mvc;

namespace back.PL.Controllers
{
    [Route("back/[controller]")]
    [ApiController]
    public class ShopController : Controller
    {
        IShopService _service;
        public ShopController(IShopService service)
        {
            _service = service;
        }

        [HttpGet("GetShops")]
        public async Task<IActionResult> GetShops(int userId, [FromQuery] List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page)
        {
            try
            {
                return Ok(await _service.GetShops(userId, categories, rating, open, range, location, sort, search, page));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("ShopPages")]
        public IActionResult ShopPages() 
        {
            try
            {
                return Ok(new { PageCount = _service.ShopPages() });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }
    }
}
