﻿namespace back.BLL.Dtos
{
    public class OrderCard
    {
        public int Id { get; set; }
        public int Quantity { get; set; }
        public float Amount { get; set; }
        public string Status { get; set; }
        public DateTime CreatedOn { get; set; }
    }
}
