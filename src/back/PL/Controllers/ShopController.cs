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

        [HttpGet("ShopPages")]
        public IActionResult ShopPages() 
        {
            try
            {
                return Ok(new { PageCount = _service.ShopPages() });
            }
            catch (Exception ex)
            {
                return BadRequest(new { ErrorMessage = ex.Message });
            }
        }
    }
}
