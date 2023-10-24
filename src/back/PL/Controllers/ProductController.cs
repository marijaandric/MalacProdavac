using back.BLL.Dtos;
using back.BLL.Services;
using Microsoft.AspNetCore.Mvc;

namespace back.PL.Controllers
{
    [Route("back/[controller]")]
    [ApiController]
    public class ProductController : Controller
    {
        IProductService _service;
        public ProductController(IProductService productService)
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
    }
}