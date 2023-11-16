using back.BLL.Services;
using Microsoft.AspNetCore.Mvc;

namespace back.PL.Controllers
{
    [Route("back/[controller]")]
    [ApiController]
    public class OrderController : Controller
    {
        IOrderService _service;
        public OrderController(IOrderService service)
        {
            _service = service;
        }

        [HttpGet("GetOrders")]
        public async Task<IActionResult> GetOrders(int userId, int status, int page)
        {
            try
            {
                return Ok(await _service.GetOrders(userId, status, page));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
