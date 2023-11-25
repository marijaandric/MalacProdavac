using back.BLL.Dtos;
using back.BLL.Services;
using Microsoft.AspNetCore.Mvc;

namespace back.PL.Controllers
{
    [Route("back/[controller]")]
    [ApiController]
    public class DeliveryController : Controller
    {
        IDeliveryService _service;

        public DeliveryController(IDeliveryService service)
        {
            _service = service;
        }

        [HttpPost("InsertDeliveryRoute")]
        public async Task<IActionResult> InsertDeliveryRoute(DeliveryRouteDto dto)
        {
            try
            {
                return Ok(new { Success = await _service.InsertDeliveryRoute(dto) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }
    }
}
