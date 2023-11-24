using back.BLL.Dtos;
using back.BLL.Services;
using back.Models;
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
        public async Task<IActionResult> GetShops(int? userId, [FromQuery] List<int>? categories, int? rating, bool? open, int? range, string? location, int sort, string? search, int page, bool? favorite, float? currLat, float? currLong)
        {
            try
            {
                return Ok(await _service.GetShops(userId, categories, rating, open, range, location, sort, search, page, favorite, currLat, currLong));
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
        public async Task<IActionResult> ShopPages(int? userId, [FromQuery] List<int>? categories, int? rating, bool? open, int? range, string? location, string? search, bool? favorite, float? currLat, float? currLong) 
        {
            try
            {
                return Ok(new { PageCount = await _service.ShopPages(userId, categories, rating, open, range, location, search, favorite, currLat, currLong) });
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

        [HttpPost("NewProductDisplay")]
        public async Task<IActionResult> NewProductDisplay(ProductDisplayDto productDisplay)
        {
            try
            {
                return Ok(new { Success = await _service.InsertProductDisplay(productDisplay) });
            }
            catch (Exception ex)
            {
                return BadRequest(new {Error =  ex.Message});
            }
        }

        [HttpPut("EditProductDisplay")]
        public async Task<IActionResult> EditProductDisplay(EditProductDisplayDto productDisplay)
        {
            try
            {
                return Ok(new { Success = await _service.EditProductDisplay(productDisplay) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpDelete("DeleteProductDisplay")]
        public async Task<IActionResult> DeleteProductDisplay(int id)
        {
            try
            {
                return Ok(new { Success = await _service.DeleteProductDisplay(id) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("GetProductDisplay")]
        public async Task<IActionResult> GetProductDisplay(int id)
        {
            try
            {
                return Ok(new { Success = await _service.GetProductDisplay(id) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }
    }
}
