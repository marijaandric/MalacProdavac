using back.BLL.Services;
using Microsoft.AspNetCore.Mvc;

namespace back.PL.Controllers
{
    [Route("back/[controller]")]
    [ApiController]
    public class DeliveryController : Controller
    {
        IDeliveryService _service;

        public DeliveryController(IDeliveryService service)
        {
            _service = service;
        }

    }
}
