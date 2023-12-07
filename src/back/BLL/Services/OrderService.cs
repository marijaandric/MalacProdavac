using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.Infos;
using back.DAL.Repositories;
using back.Models;

namespace back.BLL.Services
{
    public class OrderService : IOrderService
    {
        IOrderRepository _repository;
        INotificationRepository _notificationRepository;
        IShopRepository _shopRepository;
        IUserRepository _userRepository;
        IDeliveryRepository _deliveryRepository;
        public OrderService(IOrderRepository repository, INotificationRepository notificationRepository, IShopRepository shopRepository, IUserRepository userRepository, IDeliveryRepository deliveryRepository)
        {
            _repository = repository;
            _notificationRepository = notificationRepository;
            _shopRepository = shopRepository;
            _userRepository = userRepository;
            _deliveryRepository = deliveryRepository;
        }

        public async Task<int> GetOrdersPageCount(int userId, int? status)
        {
            return await _repository.GetOrdersPageCount(userId, status);
        }

        public async Task<List<OrderCard>> GetOrders(int userId, int? status, int page)
        {
            List<OrderCard> orders = await _repository.GetOrders(userId, status, page);
            if (orders.Count == 0) throw new ArgumentException("No orders!");

            return orders;
        }

        public async Task<OrderInfo> OrderDetails(int orderId)
        {
            OrderInfo info = await _repository.OrderDetails(orderId);
            if (info == null) throw new ArgumentException("No order found!");

            return info;
        }

        public async Task<bool> InsertOrders(List<OrderDto> orders)
        {
            foreach (var order in orders)
            {
                Order o = await _repository.InsertOrder(order);
                if (o == null) throw new ArgumentException("Order could not be processed!");

                if (o.DeliveryMethodId == 1 && o.PickupTime != null)
                    if (await _notificationRepository.InsertNotification((await _shopRepository.GetShop(order.ShopId)).OwnerId, 6, "Order pickup request", "User " + await _userRepository.GetUsername(order.UserId) + " has requested to pick up order #" + o.Id + "on " + ((DateTime)o.PickupTime).ToShortDateString() + ", at " + ((DateTime)o.PickupTime).Hour + ":" + ((DateTime)o.PickupTime).Minute + ".\nTap to respond.", o.Id)) Console.WriteLine("Notification sent!");

                if (o.DeliveryMethodId == 2)
                {
                    int id = await _deliveryRepository.InsertDeliveryRequest(new DeliveryRequestDto { OrderId = o.Id, ShopId = o.ShopId });
                    if (id != -1)
                    {
                        if (await _notificationRepository.InsertNotification((await _shopRepository.GetShop(order.ShopId)).OwnerId, 5, "Order delivery request", "User " + await _userRepository.GetUsername(order.UserId) + " has placed a new order. Please choose a delivery person to deliver order#" + o.Id + ".\nTap here to view the request.", id)) Console.WriteLine("Notification sent!");
                    }
                    else
                    {
                        await _repository.DeleteOrder(o);
                        return false;
                    }
                }
            }

            return true;

        }

        public async Task<bool> RespondToPickupRequest(int orderId, int resp, string? message)
        {
            if (!await _repository.UpdateResponse(orderId, resp)) throw new ArgumentException("Could not send response!");

            Order o = await _repository.GetOrder(orderId);

            if (resp == 1)
            {
                if (await _notificationRepository.InsertNotification(o.UserId, 6, "Pickup time response", "The pickup time for order #" + o.Id + " has been accepted.\nTap to view order details.", o.Id)) Console.WriteLine("Notification sent!");
            }
            else
            {
                if (message == null)
                {
                    if (await _notificationRepository.InsertNotification(o.UserId, 6, "Pickup time response", "The pickup time for order #" + o.Id + " has been declined.\nTap to view order details.", o.Id)) Console.WriteLine("Notification sent!");
                }
                else
                {
                    if (await _notificationRepository.InsertNotification(o.UserId, 6, "Pickup time response", "The pickup time for order #" + o.Id + " has been declined.\nThe owner said: " + message + " \nTap to view order details.", o.Id)) Console.WriteLine("Notification sent!");
                }
                    
            }

            return true;
        }

        public async Task<List<Card>> GetCards(int userId)
        {
            var cards = await _repository.GetCards(userId);
            if (cards.Count == 0) throw new ArgumentException("No cards found!");
            return cards;
        }
        
        public async Task<bool> InsertCard(CardDto dto)
        {
            Card newCard = new Card
            {
                CardNumber = dto.CardNumber,
                CVV = dto.CVV,
                ExpirationDate = DateTime.Parse(dto.ExpirationDate),
                Fullname = dto.FullName,
                OwnerId = dto.OwnerId
            };

            return await _repository.InsertCard(newCard);
        }

        public async Task<bool> DeleteCard(int cardId)
        {
            return await _repository.DeleteCard(cardId);
        }
    }
}
