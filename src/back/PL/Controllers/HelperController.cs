using back.BLL.Services;
using Microsoft.AspNetCore.Mvc;

namespace back.PL.Controllers
{
    [Route("back/[Controller]")]
    [ApiController]
    public class HelperController : Controller
    {
        IHelperService _service;

        public HelperController(IHelperService service)
        {
            _service = service;
        }

        [HttpPost("UploadImage")]
        public async Task<IActionResult> UploadImage (IFormFile image, int type, int id)
        {
            try
            {
                return Ok(new { Success = await _service.UploadImage(image, type, id) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("GeneratePaymentSlip")]
        public async Task<IActionResult> GeneratePaymentSlip(int userId, int shopId, float amount)
        {
            try
            {
                return Ok(new { Success = await _service.GeneratePaymentSlip(userId, shopId, amount) });
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("Route")]
        public async Task<IActionResult> Route(string start, string end, string shop, string shipping)
        {
            try
            {
                
                return Ok(new { TotalDistance = await _service.Route(start, end, shop, shipping) });
            }
            catch (Exception ex)
            {  
                return BadRequest(ex.Message); 
            }
        }

        [HttpGet("Metrics")]
        public async Task<IActionResult> Metrics()
        {
            try
            {
                return Ok(await _service.GetMetrics());
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("Sizes")]
        public async Task<IActionResult> Sizes()
        {
            try
            {
                return Ok(await _service.GetSizes());
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }
    }
}
