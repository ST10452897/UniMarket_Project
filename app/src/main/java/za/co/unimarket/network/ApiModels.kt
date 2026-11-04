package za.co.unimarket.network

import com.google.gson.annotations.SerializedName

/** Request for student registration via institutional credentials */
data class RegisterRequest(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("campus") val campus: String? = null
)

/** Request for authentication */
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

/** Response from auth endpoints */
data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("tokenType") val tokenType: String = "Bearer",
    @SerializedName("userId") val userId: String,
    @SerializedName("email") val email: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("campus") val campus: String? = null
)

/** Marketplace category DTO */
data class CategoryDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("itemCount") val itemCount: Int = 0,
    @SerializedName("iconName") val iconName: String? = null,
    @SerializedName("active") val active: Boolean = true
)

/** Marketplace listing DTO */
data class ListingDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("price") val price: Double,
    @SerializedName("category") val category: String,
    @SerializedName("campus") val campus: String,
    @SerializedName("sellerId") val sellerId: String,
    @SerializedName("sellerName") val sellerName: String,
    @SerializedName("isVerified") val isVerified: Boolean = true,
    @SerializedName("isWishlisted") val isWishlisted: Boolean = false,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("status") val status: String = "ACTIVE",
    @SerializedName("createdAt") val createdAt: String? = null
)

/** Request to create a new marketplace listing */
data class CreateListingRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("category") val category: String,
    @SerializedName("campus") val campus: String,
    @SerializedName("imageUrl") val imageUrl: String? = null
)

/** Request to update an existing listing */
data class UpdateListingRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("category") val category: String,
    @SerializedName("campus") val campus: String,
    @SerializedName("status") val status: String = "ACTIVE",
    @SerializedName("imageUrl") val imageUrl: String? = null
)

/** Wishlist request payload */
data class WishlistRequest(
    @SerializedName("listingId") val listingId: String
)

/** Wishlist action response */
data class WishlistResponse(
    @SerializedName("message") val message: String,
    @SerializedName("isWishlisted") val isWishlisted: Boolean,
    @SerializedName("items") val items: List<ListingDto>? = null
)

/** Buyer-to-seller message DTO */
data class MessageDto(
    @SerializedName("id") val id: String,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("senderName") val senderName: String,
    @SerializedName("receiverId") val receiverId: String,
    @SerializedName("listingId") val listingId: String? = null,
    @SerializedName("content") val content: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("unread") val unread: Boolean = false
)

/** Request payload to send a message */
data class SendMessageRequest(
    @SerializedName("receiverId") val receiverId: String,
    @SerializedName("listingId") val listingId: String? = null,
    @SerializedName("content") val content: String
)

/** Saved search request payload */
data class SavedSearchRequest(
    @SerializedName("keyword") val keyword: String,
    @SerializedName("minPrice") val minPrice: Double? = null,
    @SerializedName("maxPrice") val maxPrice: Double? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("campus") val campus: String? = null
)

/** Saved search response payload */
data class SavedSearchResponse(
    @SerializedName("id") val id: String,
    @SerializedName("keyword") val keyword: String,
    @SerializedName("minPrice") val minPrice: Double? = null,
    @SerializedName("maxPrice") val maxPrice: Double? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("campus") val campus: String? = null,
    @SerializedName("createdAt") val createdAt: String
)

/** Offline sync item descriptor */
data class SyncActionDto(
    @SerializedName("actionType") val actionType: String, // CREATE, UPDATE, DELETE, WISHLIST
    @SerializedName("entityId") val entityId: String,
    @SerializedName("payloadJson") val payloadJson: String,
    @SerializedName("timestamp") val timestamp: Long
)

/** Sync request payload */
data class SyncRequest(
    @SerializedName("pendingActions") val pendingActions: List<SyncActionDto>,
    @SerializedName("lastSyncedAt") val lastSyncedAt: Long
)

/** Sync response payload */
data class SyncResponse(
    @SerializedName("status") val status: String,
    @SerializedName("syncedAt") val syncedAt: Long,
    @SerializedName("processedCount") val processedCount: Int,
    @SerializedName("updatedListings") val updatedListings: List<ListingDto>? = null
)

/** Generic API Response Wrapper */
data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T? = null
)
