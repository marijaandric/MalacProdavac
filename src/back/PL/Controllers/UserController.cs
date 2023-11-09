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
                return Ok(await _service.GetMyProfile(userId));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("PublicProfile")]
        public async Task<IActionResult> PublicProfile(int userId)
        {
            try
            {
                return Ok(await _service.GetPublicProfile(userId));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("ProductReviewsOfAShop")]
        public async Task<IActionResult> ProductReviewsOfAShop(int userId, int shopId, int page)
        {
            try
            {
                return Ok(await _service.GetProductReviewsOfAShop(userId, shopId, page));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("ShopReview")]
        public async Task<IActionResult> ShopReview(int userId, int shopId)
        {
            try
            {
                return Ok(await _service.GetReviewOfAShop(userId, shopId));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("ReviewedProducts")]

        public async Task<IActionResult> GetReviewedProductsOfAShop(int userId, int shopId, int page)
        {
            try
            {
                return Ok(await _service.GetReviewedProductsOfAShop(userId, shopId, page));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

    }
}
