using back.BLL.Services;
using Microsoft.AspNetCore.Mvc;

namespace back.PL.Controllers
{
    [Route("back/[controller]")]
    [ApiController]
    public class UserController : Controller
    {
        IUserService _service;
        public UserController(IUserService userService)
        {
            _service = userService;
        }

        [HttpGet("MyProfile")]
        public async Task<IActionResult> MyProfile(int userId) 
        {
            try
            {
                return Ok(new { Profile = await _service.GetMyProfile(userId) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }
    }
}
