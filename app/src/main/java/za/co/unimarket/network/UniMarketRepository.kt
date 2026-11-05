package za.co.unimarket.network

import java.util.Locale
import za.co.unimarket.R
import za.co.unimarket.data.MockData
import za.co.unimarket.model.CategoryItem
import za.co.unimarket.model.MessageThread
import za.co.unimarket.model.Product

/**
 * Main Repository facilitating network API requests with graceful local mock fallbacks.
 */
class UniMarketRepository(
    private val apiService: UniMarketApiService = ApiClient.apiService
) {


    /** POST /api/auth/register */
    suspend fun register(
        studentId: String,
        email: String,
        password: String,
        fullName: String,
        campus: String? = null
    ): Result<AuthResponse> = runCatching {
        val request = RegisterRequest(
            studentId, email, password, fullName, campus
        )

        val response = apiService.register(request)

        if (!response.isSuccessful) {
            throw Exception(
                "Registration failed: HTTP ${response.code()}"
            )
        }

        val auth = response.body()
            ?: throw Exception("Empty registration response")

        if (auth.token.isBlank()) {
            throw Exception("No authentication token received")
        }

        ApiClient.setAuthToken(auth.token)
        auth
    }

    /** POST /api/auth/login */
    suspend fun login(
        email: String,
        password: String
    ): Result<AuthResponse> = runCatching {
        val request = LoginRequest(email, password)

        val response = apiService.login(request)

        if (!response.isSuccessful) {
            throw Exception(
                "Login failed: HTTP ${response.code()}"
            )
        }

        val auth = response.body()
            ?: throw Exception("Empty login response")

        if (auth.token.isBlank()) {
            throw Exception("No authentication token received")
        }

        ApiClient.setAuthToken(auth.token)
        auth
    }

    /** GET /api/categories */
    suspend fun getCategories(): Result<List<CategoryItem>> = runCatching {
        val response = apiService.getCategories()
        if (response.isSuccessful && !response.body().isNullOrEmpty()) {
            response.body()!!.map { dtoToCategoryItem(it) }
        } else {
            MockData.categories
        }
    }.recover { MockData.categories }

    /** GET /api/listings */
    suspend fun getListings(
        search: String? = null,
        campus: String? = null,
        category: String? = null
    ): Result<List<Product>> = runCatching {
        val response = apiService.getListings(search, campus, category)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.map { dtoToProduct(it) }
        } else {
            filterLocalProducts(search, category)
        }
    }.recover { filterLocalProducts(search, category) }

    /** POST /api/listings */
    suspend fun createListing(
        title: String,
        description: String,
        price: Double,
        category: String,
        campus: String,
        imageUrl: String? = null
    ): Result<Product> = runCatching {
        val request = CreateListingRequest(title, description, price, category, campus, imageUrl)
        val response = apiService.createListing(request)
        if (response.isSuccessful && response.body() != null) {
            dtoToProduct(response.body()!!)
        } else {
            createLocalProduct(title, price, category, campus)
        }
    }.recover { createLocalProduct(title, price, category, campus) }

    /** GET /api/listings/{id} */
    suspend fun getListingById(id: String): Result<Product> = runCatching {
        val response = apiService.getListingById(id)
        if (response.isSuccessful && response.body() != null) {
            dtoToProduct(response.body()!!)
        } else {
            MockData.products.find { it.id == id } ?: MockData.products.first()
        }
    }.recover { MockData.products.find { it.id == id } ?: MockData.products.first() }

    /** PUT /api/listings/{id} */
    suspend fun updateListing(
        id: String,
        title: String,
        description: String,
        price: Double,
        category: String,
        campus: String,
        status: String = "ACTIVE"
    ): Result<Product> = runCatching {
        val request = UpdateListingRequest(title, description, price, category, campus, status)
        val response = apiService.updateListing(id, request)
        if (response.isSuccessful && response.body() != null) {
            dtoToProduct(response.body()!!)
        } else {
            val existing = MockData.products.find { it.id == id }
            existing?.copy(title = title, price = price, category = category) ?: createLocalProduct(title, price, category, campus)
        }
    }.recover { createLocalProduct(title, price, category, campus) }

    /** DELETE /api/listings/{id} */
    suspend fun deleteListing(id: String): Result<Boolean> = runCatching {
        val response = apiService.deleteListing(id)
        MockData.products.removeAll { it.id == id }
        response.isSuccessful
    }.recover {
        MockData.products.removeAll { it.id == id }
        true
    }

    /** GET /api/wishlist */
    suspend fun getWishlist(): Result<List<Product>> = runCatching {
        val response = apiService.getWishlist()
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.map { dtoToProduct(it) }
        } else {
            MockData.products.filter { it.isWishlisted }
        }
    }.recover { MockData.products.filter { it.isWishlisted } }

    /** POST /api/wishlist */
    suspend fun saveWishlist(listingId: String): Result<Boolean> = runCatching {
        val response = apiService.saveWishlist(WishlistRequest(listingId))
        val target = MockData.products.find { it.id == listingId }
        target?.let { it.isWishlisted = !it.isWishlisted }
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.isWishlisted
        } else {
            target?.isWishlisted ?: true
        }
    }.recover {
        val target = MockData.products.find { it.id == listingId }
        target?.let { it.isWishlisted = !it.isWishlisted }
        target?.isWishlisted ?: true
    }

    /** GET /api/messages */
    suspend fun getMessages(): Result<List<MessageThread>> = runCatching {
        val response = apiService.getMessages()
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.map { dtoToMessageThread(it) }
        } else {
            MockData.messages
        }
    }.recover { MockData.messages }

    /** POST /api/messages */
    suspend fun sendMessage(
        receiverId: String,
        listingId: String? = null,
        content: String
    ): Result<MessageDto> = runCatching {
        val request = SendMessageRequest(receiverId, listingId, content)
        val response = apiService.sendMessage(request)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            MessageDto(
                id = "m_${System.currentTimeMillis()}",
                senderId = "me",
                senderName = "You",
                receiverId = receiverId,
                listingId = listingId,
                content = content,
                timestamp = "Just now"
            )
        }
    }.recover {
        MessageDto(
            id = "m_offline_${System.currentTimeMillis()}",
            senderId = "me",
            senderName = "You",
            receiverId = receiverId,
            listingId = listingId,
            content = content,
            timestamp = "Just now"
        )
    }

    /** POST /api/saved-searches */
    suspend fun createSavedSearch(
        keyword: String,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        category: String? = null,
        campus: String? = null
    ): Result<SavedSearchResponse> = runCatching {
        val request = SavedSearchRequest(keyword, minPrice, maxPrice, category, campus)
        val response = apiService.createSavedSearch(request)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            SavedSearchResponse(
                id = "ss_${System.currentTimeMillis()}",
                keyword = keyword,
                minPrice = minPrice,
                maxPrice = maxPrice,
                category = category,
                campus = campus,
                createdAt = "2026-09-22T20:54:00Z"
            )
        }
    }.recover {
        SavedSearchResponse(
            id = "ss_offline_${System.currentTimeMillis()}",
            keyword = keyword,
            minPrice = minPrice,
            maxPrice = maxPrice,
            category = category,
            campus = campus,
            createdAt = "2026-09-22T20:54:00Z"
        )
    }

    /** POST /api/sync */
    suspend fun syncData(
        pendingActions: List<SyncActionDto> = emptyList(),
        lastSyncedAt: Long = System.currentTimeMillis()
    ): Result<SyncResponse> = runCatching {
        val request = SyncRequest(pendingActions, lastSyncedAt)
        val response = apiService.syncData(request)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            SyncResponse(
                status = "SUCCESS",
                syncedAt = System.currentTimeMillis(),
                processedCount = pendingActions.size
            )
        }
    }.recover {
        SyncResponse(
            status = "OFFLINE_QUEUED",
            syncedAt = System.currentTimeMillis(),
            processedCount = pendingActions.size
        )
    }

    // Helper converters
    private fun dtoToProduct(dto: ListingDto): Product {
        val (icon, bg) = getCategoryIcons(dto.category)
        return Product(
            id = dto.id,
            title = dto.title,
            price = dto.price,
            category = dto.category,
            location = "${dto.category} · ${dto.campus}",
            sellerName = dto.sellerName,
            sellerInitials = dto.sellerName.take(1),
            isVerified = dto.isVerified,
            isWishlisted = dto.isWishlisted,
            iconRes = icon,
            bgColorRes = bg
        )
    }

    private fun dtoToCategoryItem(dto: CategoryDto): CategoryItem {
        val (icon, bg) = getCategoryIcons(dto.name)
        return CategoryItem(
            id = dto.id,
            name = dto.name,
            itemCount = dto.itemCount,
            iconRes = icon,
            bgColorRes = bg
        )
    }

    private fun dtoToMessageThread(dto: MessageDto): MessageThread {
        return MessageThread(
            id = dto.id,
            senderName = dto.senderName,
            senderInitials = dto.senderName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.joinToString(""),
            lastMessage = dto.content,
            timestamp = dto.timestamp,
            unread = dto.unread
        )
    }

    private fun filterLocalProducts(search: String?, category: String?): List<Product> {
        return MockData.products.filter { p ->
            val matchSearch = search.isNullOrBlank() || p.title.contains(search, ignoreCase = true)
            val matchCategory = category.isNullOrBlank() || p.category.equals(category, ignoreCase = true)
            matchSearch && matchCategory
        }
    }

    private fun createLocalProduct(title: String, price: Double, category: String, campus: String): Product {
        val (icon, bg) = getCategoryIcons(category)
        val newProduct = Product(
            id = "p_${System.currentTimeMillis()}",
            title = title,
            price = price,
            category = category,
            location = "$category · $campus",
            sellerName = "Student User",
            sellerInitials = "S",
            isVerified = true,
            isWishlisted = false,
            iconRes = icon,
            bgColorRes = bg
        )
        MockData.products.add(0, newProduct)
        return newProduct
    }

    private fun getCategoryIcons(categoryName: String): Pair<Int, Int> {
        return when (categoryName.lowercase()) {
            "electronics" -> Pair(R.drawable.ic_laptop, R.color.category_purple_bg)
            "textbooks" -> Pair(R.drawable.ic_book, R.color.category_amber_bg)
            "clothing" -> Pair(R.drawable.ic_shirt, R.color.category_green_bg)
            else -> Pair(R.drawable.ic_box, R.color.category_blue_bg)
        }
    }
}
