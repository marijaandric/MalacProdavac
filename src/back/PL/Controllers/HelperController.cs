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
            if (image != null) return Ok(await _service.UploadImage(image, type, id));
            return BadRequest("Greska");
        }
    }
}
