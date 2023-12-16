using back.BLL.Dtos.HelpModels;

namespace back.DAL.Repositories
{
    public interface IBackgroundRepository
    {
        public Task<List<PendingReview>> PendingProductReviews();
        public Task<List<PendingReview>> PendingShopReviews();
        public Task<List<PendingReview>> PendingDeliveryPersonReviews();
        public Task DeletePastProductDisplays();
        public Task ChangeDeliveryStatus();
    }
}
