using back.BLL.Dtos;
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

        [HttpPost("InsertDeliveryRoute")]
        public async Task<IActionResult> InsertDeliveryRoute(DeliveryRouteDto dto)
        {
            try
            {
                return Ok(new { Success = await _service.InsertDeliveryRoute(dto) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPut("AddToRoute")]
        public async Task<IActionResult> AddToRoute(int reqId, int routeId)
        {
            try
            {
                return Ok(new { Success = await _service.AddToRoute(reqId, routeId) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPut("DeclineRequest")]
        public async Task<IActionResult> DeclineRequest(int reqId)
        {
            try
            {
                return Ok(new { Success = await _service.DeclineRequest(reqId) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("GetRequestsForDeliveryPerson")]
        public async Task<IActionResult> GetRequestsForDeliveryPerson(int deliveryPerson)
        {
            try
            {
                return Ok(await _service.GetRequestsForDeliveryPerson(deliveryPerson));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("GetRequestsForShop")]
        public async Task<IActionResult> GetRequestsForShop(int userId)
        {
            try
            {
                return Ok(await _service.GetRequestsForShop(userId));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("GetRoutesForDeliveryPerson")]
        public async Task<IActionResult> GetRoutesForDeliveryPerson(int userId)
        {
            try
            {
                return Ok(await _service.GetRoutesForDeliveryPerson(userId));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("GetRoutesForRequest")]
        public async Task<IActionResult> GetRoutesForRequest(int userId, int requestId)
        {
            try
            {
                return Ok(await _service.GetRoutesForRequest(userId, requestId));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("GetDeliveryPeopleForRequest")]
        public async Task<IActionResult> GetDeliveryPeopleForRequest(int requestId)
        {
            try
            {
                return Ok(await _service.GetDeliveryPeopleForRequest(requestId));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPut("ChooseDeliveryPerson")]
        public async Task<IActionResult> ChooseDeliveryPerson(int requestId, int chosenPersonId)
        {
            try
            {
                return Ok(new { Success = await _service.ChooseDeliveryPerson(requestId, chosenPersonId) });

            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("GetRouteDetails")]
        public async Task<IActionResult> GetRouteDetails(int routeId)
        {
            try
            {
                return Ok(await _service.GetRouteDetails(routeId));

            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("GetRequestDetails")]
        public async Task<IActionResult> GetRequestDetails(int reqId)
        {
            try
            {
                return Ok(await _service.GetRequestDetails(reqId));

            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpDelete("DeleteRoute")]
        public async Task<IActionResult> DeleteRoute(int routeId)
        {
            try
            {
                return Ok(new { Success = await _service.DeleteRoute(routeId) });

            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPut("EditRoute")]
        public async Task<IActionResult> EditRoute(EditDeliveryRouteDto dto)
        {
            try
            {
                return Ok(new { Success = await _service.EditRoute(dto) });

            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }
    }
}
