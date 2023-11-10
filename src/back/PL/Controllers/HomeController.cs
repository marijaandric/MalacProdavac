using back.BLL.Dtos;
using back.BLL.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;

namespace back.PL.Controllers
{
    [Route("back/[controller]")]
    [ApiController]
    public class HomeController : Controller
    {
        IHomeService _service;
        public HomeController(IHomeService productService)
        {
            _service = productService;
        }

        [HttpGet("GetCategories")]
        public async Task<IActionResult> GetCategories()
        {
            try
            {
                return Ok(await _service.GetCategories());
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("SaveChosenCategories")]
        public async Task<IActionResult> SaveChosenCategories(ChosenCategoriesDto categoriesDto)
        {
            try
            {
                return Ok(await _service.SaveChosenCategories(categoriesDto));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPut("UpdateChosenCategories")]
        public async Task<IActionResult> UpdateChosenCategories(ChosenCategoriesDto categoriesDto)
        {
            try
            {
                return Ok(await _service.UpdateChosenCategories(categoriesDto));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
        [HttpGet("GetChosenCategories")]
        public async Task<IActionResult> GetChosenCategories(int id)
        {
            try
            {
                return Ok(await _service.GetChosenCategories(id));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [Authorize]
        [HttpGet("GetHomeProducts")]
        public async Task<IActionResult> GetHomeProducts(int id)
        {
            try
            {
                return Ok(await _service.GetHomeProducts(id));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("GetHomeShops")]
        public async Task<IActionResult> GetHomeShops(int id)
        {
            try
            {
                return Ok(await _service.GetHomeShops(id));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}