using back.BLL.Dtos.HelpModels;

namespace back.DAL.Repositories
{
    public interface IBackgroundRepository
    {
        public Task<List<PendingReview>> PendingProductReviews();
    }
}
