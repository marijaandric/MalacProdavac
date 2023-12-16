using back.BLL.Dtos;
using back.BLL.Dtos.Cards;
using back.BLL.Dtos.Infos;
using back.BLL.Services;
using back.DAL.Contexts;
using back.DAL.Models;
using back.Models;
using Microsoft.EntityFrameworkCore;

namespace back.DAL.Repositories
{
    public class OrderRepository : IOrderRepository
    {
        Context _context;
        public OrderRepository(Context context)
        {
            _context = context;
        }

        int numberOfItems = 10;

        public async Task<int> GetOrdersPageCount(int userId, int? status)
        {
            if (status == null)
            {
                return (int)Math.Ceiling((double)(await _context.Orders.Where(x => x.UserId == userId).CountAsync()) / numberOfItems);
            }
            return (int)Math.Ceiling((double)(await _context.Orders.Where(x => x.UserId == userId && x.StatusId == status).CountAsync()) / numberOfItems);
        }

        public async Task<float> OrderMass(int orderId)
        {
            var orderItems = await _context.OrderItems.Where(x => x.OrderId == orderId).Join(_context.Products, oi => oi.ProductId, p => p.Id, (oi, p) => new { oi, p }).ToListAsync();
            float mass = 0;
            foreach(var item in orderItems)
            {
                if (item.p.MetricId == 2) mass += item.oi.Quantity;
                else mass += (float)item.p.Mass * item.oi.Quantity;
            }

            return mass;
        }

        public async Task<float> DeliveryPrice(int orderId)
        {
            float price = 0;
            var order = await GetOrder(orderId);
            
            if (order.DeliveryMethodId == 1) return price;

            var routeId = (await _context.DeliveryRequests.FirstOrDefaultAsync(x => x.OrderId == orderId)).RouteId;
            if (routeId == null) return price;

            price = (await _context.DeliveryRoutes.FirstOrDefaultAsync(x => x.Id == routeId)).FixedCost;

            var mass = await OrderMass(orderId);
            float coeff = 1;

            if (mass > 1 && mass <= 5) coeff = 550f / 350f;
            else if (mass > 5 && mass <= 15) coeff = 1000f / 350f;
            else if (mass > 15 && mass <= 30) coeff = 1500f / 350f;
            else if (mass > 30 && mass <= 40) coeff = 2000f / 350f;
            else if (mass > 40) coeff = 2500f / 350f;

            return (float)Math.Round((double)price * coeff, 2);
        }

        public async Task<float> RouteProfit(int routeId)
        {
            var reqs = await _context.DeliveryRequests.Where(x => x.RouteId == routeId).ToListAsync();
            float profit = reqs.Sum(x => DeliveryPrice(x.OrderId).Result);
            return profit;
        }

        public async Task<float> TotalProfit(int userId)
        {
            var pastRoutes = await _context.DeliveryRoutes.Where(x => x.DeliveryPersonId == userId && x.Finished).ToListAsync();
            float totalProfit = pastRoutes.Sum(x => RouteProfit(x.Id).Result);

            return totalProfit;
        }

        public async Task<List<OrderCard>> GetOrders(int userId, int? status, int page)
        {
            List<OrderCard> orders = new List<OrderCard>();

            if (status == null)
            {
                orders = await _context.Orders.Where(x => x.UserId == userId).Skip(page - 1 * numberOfItems).Take(numberOfItems).Select(x => new OrderCard
                {
                    Id = x.Id,
                    Quantity = _context.OrderItems.Where(i => i.OrderId == x.Id).Count(),
                    Amount = _context.OrderItems.Where(i => i.OrderId == x.Id).Sum(i => i.Price),
                    CreatedOn = x.CreatedOn.ToShortDateString(),
                    Status = _context.OrderStatuses.FirstOrDefault(s => s.Id == x.StatusId).Name
                    
                }).ToListAsync();
            }
            else
            {
                orders = await _context.Orders.Where(x => x.UserId == userId && x.StatusId == status).Skip(page - 1 * numberOfItems).Take(numberOfItems).Select(x => new OrderCard
                {
                    Id = x.Id,
                    Quantity = _context.OrderItems.Where(i => i.OrderId == x.Id).Count(),
                    Amount = _context.OrderItems.Where(i => i.OrderId == x.Id).Sum(i => i.Price),
                    CreatedOn = x.CreatedOn.ToShortDateString(),
                    Status = _context.OrderStatuses.FirstOrDefault(s => s.Id == x.StatusId).Name

                }).ToListAsync();
            }

            foreach (var order in orders) order.Amount += await DeliveryPrice(order.Id);

            return orders;
        }

        public async Task<OrderInfo> OrderDetails(int orderId)
        {
            OrderInfo order = await _context.Orders.Select(x => new OrderInfo
            {
                Id = x.Id,
                Quantity = _context.OrderItems.Where(i => i.OrderId == x.Id).Count(),
                Amount = _context.OrderItems.Where(i => i.OrderId == x.Id).Sum(i => i.Price),
                CreatedOn = x.CreatedOn.ToShortDateString(),
                Status = _context.OrderStatuses.FirstOrDefault(s => s.Id == x.StatusId).Name,
                DeliveryMethod = _context.DeliveryMethods.FirstOrDefault(dm => dm.Id == x.DeliveryMethodId).Name,
                PaymentMethod = _context.PaymentMethods.FirstOrDefault(pm => pm.Id == x.PaymentMethodId).Name,
                ShippingAddress = x.ShippingAddress

            }).FirstOrDefaultAsync(x => x.Id == orderId);

            List<OrderItemCard> items = await _context.OrderItems.Where(x => x.OrderId == orderId).Select(x => new OrderItemCard
            {
                Name = _context.Products.FirstOrDefault(p => p.Id == x.ProductId) != null ? _context.Products.FirstOrDefault(p => p.Id == x.ProductId).Name : _context.ArchivedProducts.FirstOrDefault(p => p.Id == x.ProductId).Name,
                Shop = _context.Products.FirstOrDefault(p => p.Id == x.ProductId) != null ? _context.Shop.FirstOrDefault(s => s.Id == _context.Products.FirstOrDefault(p => p.Id == x.ProductId).ShopId).Name : _context.Shop.FirstOrDefault(s => s.Id == _context.ArchivedProducts.FirstOrDefault(p => p.Id == x.ProductId).ShopId).Name,
                Price = x.Price,
                Metric = _context.Products.FirstOrDefault(p => p.Id == x.ProductId) != null ? _context.Metrics.FirstOrDefault(m => m.Id == _context.Products.FirstOrDefault(p => p.Id == x.ProductId).MetricId).Name : _context.Metrics.FirstOrDefault(m => m.Id == _context.ArchivedProducts.FirstOrDefault(p => p.Id == x.ProductId).MetricId).Name,
                Quantity = x.Quantity,
                Image = _context.ProductImages.FirstOrDefault(pi => pi.ProductId == x.ProductId).Image

            }).ToListAsync();

            double delivery = await DeliveryPrice(order.Id);
            return new OrderInfo
            {
                Id = order.Id,
                Quantity = order.Quantity,
                Amount = order.Amount,
                CreatedOn = order.CreatedOn,
                Status = order.Status,
                Items = items,
                DeliveryMethod = order.DeliveryMethod,
                PaymentMethod = order.DeliveryMethod,
                ShippingAddress = order.ShippingAddress,
                DeliveryPrice = (float)Math.Ceiling(delivery)
            };
        }

        public async Task<Order> InsertOrder(OrderDto order)
        {
            Order newOrder = new Order
            {
                UserId = order.UserId,
                CreatedOn = DateTime.Now,
                DeliveryMethodId = order.DeliveryMethod,
                PaymentMethodId = order.PaymentMethod,
                ShippingAddress = order.ShippingAddress,
                StatusId = (await _context.OrderStatuses.FirstOrDefaultAsync(x => x.Name == "Pending")).Id,
                PickupTime = order.PickupTime,
                Accepted = -1,
                ShopId = order.ShopId
            };

            (double, double) coords;
            if (order.ShippingAddress != null)
            {
                coords = await HelperService.GetCoordinates(order.ShippingAddress);
                newOrder.Latitude = (float)coords.Item1;
                newOrder.Longitude = (float)coords.Item2;
            }
            await _context.Orders.AddAsync(newOrder);
            await _context.SaveChangesAsync();

            foreach (var item in order.Products)
            {
                if (item != null)
                {
                    Product product = _context.Products.FirstOrDefault(x => x.Id == item.Id);
                    float total;

                    int count = order.Products.Where(x => x.Id == item.Id).Sum(x => x.Quantity);
                    if (product.SalePercentage != null && product.SaleMinQuantity <= order.Products.Where(x => x.Id == item.Id).Sum(x => x.Quantity)) total = product.Price * item.Quantity * (100 - product.SalePercentage) / 100;
                    else total = product.Price * item.Quantity;

                    await _context.OrderItems.AddAsync(new OrderItem
                    {
                        ProductId = item.Id,
                        Quantity = item.Quantity,
                        SizeId = item.SizeId,
                        Price = total,
                        OrderId = newOrder.Id,
                    });

                    ProductSize ps = await _context.ProductSizes.FirstOrDefaultAsync(x => x.ProductId == item.Id && x.SizeId == item.SizeId);
                    ps.Stock -= item.Quantity;
                }
            }

            await _context.SaveChangesAsync();
            return newOrder;
        }

        public async Task<bool> UpdateResponse(int orderId, int resp)
        {
            Order o = await _context.Orders.FirstOrDefaultAsync(x => x.Id == orderId);
            o.Accepted = resp;
            if (resp != 0) o.StatusId = (await _context.OrderStatuses.FirstOrDefaultAsync(x => x.Name == "Processing")).Id;

            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<Order> GetOrder(int orderId)
        {
            return await _context.Orders.FirstOrDefaultAsync(x => x.Id == orderId);
        }

        public async Task<PaymentSlipInfo> GetPaymentSlipInfo(int userId, int shopId)
        {
            User u = await _context.Users.FirstOrDefaultAsync(x => x.Id == userId);
            Shop shop = await _context.Shop.FirstOrDefaultAsync(x => x.Id == shopId);
            User rcv = await _context.Users.FirstOrDefaultAsync(x => x.Id == shop.OwnerId);
            return new PaymentSlipInfo
            {
                Name = u.Name,
                Lastname = u.Lastname,
                Address = u.Address,
                NameRcv = rcv.Name,
                LastnameRcv = rcv.Lastname,
                AddressRcv = rcv.Address,
                AccountNumberRcv = shop.AccountNumber,
            };
        }

        public async Task<bool> DeleteOrder(Order order)
        {
            _context.Orders.Remove(order);
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<List<Card>> GetCards(int userId)
        {
            return await _context.Cards.Where(x => x.OwnerId == userId).ToListAsync();
        }

        public async Task<bool> InsertCard(Card card)
        {
            _context.Cards.Add(card);
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<bool> DeleteCard(int cardId)
        {
            _context.Cards.Remove(await _context.Cards.FirstOrDefaultAsync(x => x.Id == cardId));
            return await _context.SaveChangesAsync() > 0;
        }

        public async Task<CheckedStockDto> CheckStock(StockToCheckDto toCheck)
        {
            int stock;
            var temp = (await _context.ProductSizes.Join(_context.Sizes, ps => ps.SizeId, s => s.Id, (ps, s) => new { ps, s }).FirstOrDefaultAsync(x => x.ps.ProductId == toCheck.Id && x.s.Name.Equals(toCheck.Size)));
            
            if (temp == null) stock = -1;
            else stock = temp.ps.Stock;
            
            return new CheckedStockDto
            {
                Id = toCheck.Id,
                Quantity = toCheck.Quantity,
                Size = toCheck.Size,
                Available = stock
            };
        }
    }
}
