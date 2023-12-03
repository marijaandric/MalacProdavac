using back.BLL.Dtos;
using back.BLL.Services;
using back.Models;
using FirebaseAdmin.Messaging;
using iText.Layout.Borders;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;

namespace back.PL.Controllers
{
    [Route("back/[controller]")]
    [ApiController]
    public class OrderController : Controller
    {
        IOrderService _service;
        public OrderController(IOrderService service)
        {
            _service = service;
        }

        [HttpGet("GetOrders")]
        public async Task<IActionResult> GetOrders(int userId, int? status, int page)
        {
            try
            {
                return Ok(await _service.GetOrders(userId, status, page));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("OrderDetails")]
        public async Task<IActionResult> OrderDetails(int orderId)
        {
            try
            {
                return Ok(await _service.OrderDetails(orderId));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("InsertOrders")]
        public async Task<IActionResult> InsertOrders(List<OrderDto> orders)
        {
            try
            {
                return Ok(new { Success = await _service.InsertOrders(orders) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPut("RespondToPickupRequest")]
        public async Task<IActionResult> RespondToPickupRequest(int orderId, int resp, string? message)
        {
            try
            {
                return Ok(new { Success = await _service.RespondToPickupRequest(orderId, resp, message) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpGet("GetCards")]
        public async Task<IActionResult> GetCards(int userId)
        {
            try
            {
                return Ok(await _service.GetCards(userId));
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPost("AddCard")]
        public async Task<IActionResult> InsertCard(CardDto dto)
        {
            try
            {
                return Ok(new { Success = await _service.InsertCard(dto) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpDelete("DeleteCard")]
        public async Task<IActionResult> DeleteCard(int cardId)
        {
            try
            {
                return Ok(new { Success = await _service.DeleteCard(cardId) });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }
    }
}
