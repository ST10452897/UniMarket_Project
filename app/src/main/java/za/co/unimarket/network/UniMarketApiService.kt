package za.co.unimarket.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit contract for UniMarket REST API endpoints.
 */
interface UniMarketApiService {

    /** POST /api/auth/register - Registers a new student using institutional credentials */
    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    /** POST /api/auth/login - Authenticates user credentials and returns an access token */
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    /** GET /api/categories - Retrieves active marketplace categories */
    @GET("api/categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    /** GET /api/listings - Fetches active listings with search, campus, and category filters */
    @GET("api/listings")
    suspend fun getListings(
        @Query("search") search: String? = null,
        @Query("campus") campus: String? = null,
        @Query("category") category: String? = null
    ): Response<List<ListingDto>>

    /** POST /api/listings - Publishes a new item listing */
    @POST("api/listings")
    suspend fun createListing(
        @Body request: CreateListingRequest
    ): Response<ListingDto>

    /** GET /api/listings/{id} - Retrieves an individual listing */
    @GET("api/listings/{id}")
    suspend fun getListingById(
        @Path("id") id: String
    ): Response<ListingDto>

    /** PUT /api/listings/{id} - Updates an individual listing */
    @PUT("api/listings/{id}")
    suspend fun updateListing(
        @Path("id") id: String,
        @Body request: UpdateListingRequest
    ): Response<ListingDto>

    /** DELETE /api/listings/{id} - Removes an individual listing */
    @DELETE("api/listings/{id}")
    suspend fun deleteListing(
        @Path("id") id: String
    ): Response<Unit>

    /** GET /api/wishlist - Views bookmarked listings for quick access */
    @GET("api/wishlist")
    suspend fun getWishlist(): Response<List<ListingDto>>

    /** POST /api/wishlist - Saves or toggles bookmarked listings for quick access */
    @POST("api/wishlist")
    suspend fun saveWishlist(
        @Body request: WishlistRequest
    ): Response<WishlistResponse>

    /** GET /api/messages - Manages direct buyer-to-seller in-app communication */
    @GET("api/messages")
    suspend fun getMessages(): Response<List<MessageDto>>

    /** POST /api/messages - Sends a direct buyer-to-seller message */
    @POST("api/messages")
    suspend fun sendMessage(
        @Body request: SendMessageRequest
    ): Response<MessageDto>

    /** POST /api/saved-searches - Registers keyword and price criteria to trigger listing match alerts */
    @POST("api/saved-searches")
    suspend fun createSavedSearch(
        @Body request: SavedSearchRequest
    ): Response<SavedSearchResponse>

    /** POST /api/sync - Synchronizes on-device offline modifications with the backend */
    @POST("api/sync")
    suspend fun syncData(
        @Body request: SyncRequest
    ): Response<SyncResponse>
}
