using Google.Cloud.Firestore;
using Microsoft.AspNetCore.Mvc;
using UniMarket.Api.Models;

namespace UniMarket.Api.Controllers;

[ApiController]
[Route("api/listings")]
public class ListingsController : ControllerBase
{
    private readonly CollectionReference _listings;

    public ListingsController(FirestoreDb db)
    {
        _listings = db.Collection("listings");
    }

    [HttpGet]
    public async Task<IActionResult> GetAll(
        [FromQuery] string? search,
        [FromQuery] string? campus,
        [FromQuery] string? category)
    {
        var snapshot = await _listings.GetSnapshotAsync();

        var items = snapshot.Documents
            .Select(doc =>
            {
                var item = doc.ConvertTo<ListingDocument>();
                return new Listing
                {
                    Id = doc.Id,
                    Title = item.Title,
                    Description = item.Description,
                    Price = item.Price,
                    Category = item.Category,
                    Campus = item.Campus,
                    ImageUrl = item.ImageUrl,
                    SellerId = item.SellerId
                };
            })
            .Where(x => string.IsNullOrWhiteSpace(search) ||
                x.Title.Contains(search, StringComparison.OrdinalIgnoreCase))
            .Where(x => string.IsNullOrWhiteSpace(campus) ||
                x.Campus.Equals(campus, StringComparison.OrdinalIgnoreCase))
            .Where(x => string.IsNullOrWhiteSpace(category) ||
                x.Category.Equals(category, StringComparison.OrdinalIgnoreCase))
            .ToList();

        return Ok(items);
    }

    [HttpPost]
    public async Task<IActionResult> Create(Listing listing)
    {
        if (string.IsNullOrWhiteSpace(listing.Title) ||
            listing.Price < 0)
            return BadRequest("Invalid listing.");

        var doc = _listings.Document();
        var item = new ListingDocument
        {
            Title = listing.Title,
            Description = listing.Description,
            Price = listing.Price,
            Category = listing.Category,
            Campus = listing.Campus,
            ImageUrl = listing.ImageUrl,
            SellerId = listing.SellerId
        };

        await doc.SetAsync(item);
        listing.Id = doc.Id;
        return Created($"/api/listings/{doc.Id}", listing);
    }
}

[FirestoreData]
public class ListingDocument
{
    [FirestoreProperty] public string Title { get; set; } = "";
    [FirestoreProperty] public string Description { get; set; } = "";
    [FirestoreProperty] public double Price { get; set; }
    [FirestoreProperty] public string Category { get; set; } = "";
    [FirestoreProperty] public string Campus { get; set; } = "";
    [FirestoreProperty] public string ImageUrl { get; set; } = "";
    [FirestoreProperty] public string SellerId { get; set; } = "";
}