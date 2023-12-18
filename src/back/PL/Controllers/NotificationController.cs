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
        public async Task<IActionResult> GetNotifications(int userId, [FromQuery]List<int>? types, int page)
        {
            try
            {
                return Ok(await _service.GetNotifications(userId, types, page));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPut("MarkAsRead")]
        public async Task<IActionResult> MarkAsRead(int notificationId)
        {
            try
            {
                return Ok(new { Success = await _service.MarkAsRead(notificationId)});
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpDelete("DeleteNotification")]
        public async Task<IActionResult> DeleteNotification(int notificationId)
        {
            try
            {
                return Ok(new { Success = await _service.DeleteNotification(notificationId) });
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpDelete("DeleteAllNotifications")]
        public async Task<IActionResult> DeleteAllNotifications(int userId)
        {
            try
            {
                return Ok(new { Success = await _service.DeleteAllNotifications(userId) });
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}
