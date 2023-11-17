using back.BLL.Services;
using back.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;

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

        [HttpGet("OrderDetails")]
        public async Task<IActionResult> OrderDetails(int orderId)
        {
            try
            {
                return Ok(await _service.OrderDetails(orderId));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
