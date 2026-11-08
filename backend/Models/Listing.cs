namespace UniMarket.Api.Models;

public class Listing
{
    public string Id { get; set; } = "";
    public string Title { get; set; } = "";
    public string Description { get; set; } = "";
    public double Price { get; set; }
    public string Category { get; set; } = "";
    public string Campus { get; set; } = "";
    public string ImageUrl { get; set; } = "";
    public string SellerId { get; set; } = "";
}