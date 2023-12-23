using back.BLL.Dtos;
using back.BLL.Services;
using Microsoft.AspNetCore.Mvc;

namespace back.PL.Controllers
{
    [Route("back/[controller]")]
    [ApiController]
    public class AuthController : Controller
    {
        IAuthService _authService;

        public AuthController(IAuthService authService)
        {
            _authService = authService;
        }

        [HttpPost("Register")]
        public async Task<IActionResult> Register(UserDto user)
        {
            try
            {
                string token = await _authService.Register(user);
                if (token != "") return Ok(new { Token = token });
                else return BadRequest("Database error.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("Login")]
        public async Task<IActionResult> Login(LoginDto loginDto)
        {
            try
            {
                //treba token da vraca
                string token = await _authService.Login(loginDto);
                return Ok(new { Token = token });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPut("Edit")]
        public async Task<IActionResult> Edit(EditUserDto editDto)
        {
            try
            {
                return Ok(new { Token = await _authService.EditUser(editDto) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPut("ChangePassword")]
        public async Task<IActionResult> ChangePassword(ChangePasswordDto editDto)
        {
            try
            {
                return Ok(new { Token = await _authService.ChangePassword(editDto) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPut("DefaultProfilePhoto")]
        public async Task<IActionResult> DefaultProfilePhoto(int userId)
        {
            try
            {
                return Ok(new { Success = await _authService.DeleteProfilePhoto(userId) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPut("FCMTokenSave")]
        public async Task<IActionResult> SaveFCMToken(int userId, string token)
        {
            try
            {
                return Ok(new { Success = await _authService.SaveFcmToken(userId,token) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPut("LatestCoordinates")]
        public async Task<IActionResult> SaveLatestCoordinates(int userId, float lat, float lon)
        {
            try
            {
                return Ok(new { Success = await _authService.SaveLatestCoordinates(userId, lat, lon) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

    }
}
