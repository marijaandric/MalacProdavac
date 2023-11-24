using back.BLL.Services;
using Microsoft.AspNetCore.Mvc;

namespace back.PL.Controllers
{
    [Route("back/[controller]")]
    [ApiController]
    public class NotificationController : Controller
    {
        INotificationService _service;

        public NotificationController(INotificationService service)
        {
            _service = service;
        }

        [HttpGet("GetNotifications")]
        public async Task<IActionResult> GetNotifications(int userId, int? type, int page)
        {
            try
            {
                return Ok(await _service.GetNotifications(userId, type, page));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
