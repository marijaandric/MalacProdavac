using back.BLL.Dtos;
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

        [HttpGet("GetShops")]
        public async Task<IActionResult> GetShops(int userId, [FromQuery] List<int> categories, int rating, bool open, int range, string location, int sort, string search, int page, bool favorite)
        {
            try
            {
                return Ok(await _service.GetShops(userId, categories, rating, open, range, location, sort, search, page, favorite));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("SortingOptions")]
        public IActionResult SortingOptions()
        {
            return Ok(new Dictionary<int, string>
            {
                { 0 , "Default" },
                { 1 , "Rating (lowest first)" },
                { 2 , "Rating (highest first)" },
                { 3 , "Alphabetically (ascending)" },
                { 4 , "Alphabetically (descending)" }
            });
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
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("ShopDetails")]
        public async Task<IActionResult> ShopDetails(int shopId, int userId)
        {
            try
            {
                return Ok(await _service.ShopDetails(shopId, userId));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("ShopReviews")]
        public async Task<IActionResult> GetShopReviews(int shopId, int page)
        {
            try
            {
                return Ok(await _service.GetShopReviews(shopId, page));
            }
            catch (Exception ex)
            {
                return BadRequest(new { ErrorMessage = ex.Message });
            }
        }

        [HttpPost("ToggleLike")]
        public async Task<IActionResult> ToggleLike(int shopId, int userId)
        {
            try
            {
                return Ok(new { success = await _service.ToggleLike(shopId, userId) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPost("Review")]
        public async Task<IActionResult> LeaveReview(ReviewDto review)
        {
            try
            {
                return Ok(new { success = await _service.LeaveReview(review) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPost("NewShop")]
        public async Task<IActionResult> NewShop(ShopDto shop)
        {
            try
            {
                return Ok(new { Success = await _service.InsertShop(shop) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPut("EditShop")]
        public async Task<IActionResult> EditShop(EditShopDto shop)
        {
            try
            {
                return Ok(new { Success = await _service.EditShop(shop) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpDelete("DeleteShop")]
        public async Task<IActionResult> DeleteShop(int shopId)
        {
            try
            {
                return Ok(new { Success = await _service.DeleteShop(shopId) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }
    }
}
