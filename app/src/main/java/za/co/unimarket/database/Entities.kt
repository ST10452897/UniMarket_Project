package za.co.unimarket.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import za.co.unimarket.model.CategoryItem
import za.co.unimarket.model.MessageThread
import za.co.unimarket.model.NotificationItem
import za.co.unimarket.model.Product

/** User Account Entity */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val email: String,
    val fullName: String,
    val studentId: String? = null,
    val campus: String = "UP Hatfield",
    val authToken: String? = null,
    val isLoggedIn: Boolean = true
)

/** Category Entity */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val itemCount: Int,
    val iconRes: Int,
    val bgColorRes: Int
) {
    fun toDomain(): CategoryItem = CategoryItem(
        id = id,
        name = name,
        itemCount = itemCount,
        iconRes = iconRes,
        bgColorRes = bgColorRes
    )

    companion object {
        fun fromDomain(item: CategoryItem): CategoryEntity = CategoryEntity(
            id = item.id,
            name = item.name,
            itemCount = item.itemCount,
            iconRes = item.iconRes,
            bgColorRes = item.bgColorRes
        )
    }
}

/** Marketplace Product Listing Entity */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val title: String,
    val price: Double,
    val category: String,
    val location: String,
    val sellerName: String,
    val sellerInitials: String,
    val isVerified: Boolean = true,
    val isWishlisted: Boolean = false,
    val iconRes: Int,
    val bgColorRes: Int,
    val isSynced: Boolean = true
) {
    fun toDomain(): Product = Product(
        id = id,
        title = title,
        price = price,
        category = category,
        location = location,
        sellerName = sellerName,
        sellerInitials = sellerInitials,
        isVerified = isVerified,
        isWishlisted = isWishlisted,
        iconRes = iconRes,
        bgColorRes = bgColorRes
    )

    companion object {
        fun fromDomain(product: Product, synced: Boolean = true): ProductEntity = ProductEntity(
            id = product.id,
            title = product.title,
            price = product.price,
            category = product.category,
            location = product.location,
            sellerName = product.sellerName,
            sellerInitials = product.sellerInitials,
            isVerified = product.isVerified,
            isWishlisted = product.isWishlisted,
            iconRes = product.iconRes,
            bgColorRes = product.bgColorRes,
            isSynced = synced
        )
    }
}

/** Wishlist Bookmark Entity */
@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey val listingId: String,
    val addedAt: Long = System.currentTimeMillis()
)

/** Buyer-Seller Chat Message Thread Entity */
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val senderName: String,
    val senderInitials: String,
    val lastMessage: String,
    val timestamp: String,
    val avatarBgColor: String = "#FFB74D",
    val unread: Boolean = false
) {
    fun toDomain(): MessageThread = MessageThread(
        id = id,
        senderName = senderName,
        senderInitials = senderInitials,
        lastMessage = lastMessage,
        timestamp = timestamp,
        avatarBgColor = avatarBgColor,
        unread = unread
    )

    companion object {
        fun fromDomain(thread: MessageThread): MessageEntity = MessageEntity(
            id = thread.id,
            senderName = thread.senderName,
            senderInitials = thread.senderInitials,
            lastMessage = thread.lastMessage,
            timestamp = thread.timestamp,
            avatarBgColor = thread.avatarBgColor,
            unread = thread.unread
        )
    }
}

/** Notification Entity */
@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false
) {
    fun toDomain(): NotificationItem = NotificationItem(
        id = id,
        title = title,
        message = message,
        timeAgo = timeAgo,
        isRead = isRead
    )

    companion object {
        fun fromDomain(item: NotificationItem): NotificationEntity = NotificationEntity(
            id = item.id,
            title = item.title,
            message = item.message,
            timeAgo = item.timeAgo,
            isRead = item.isRead
        )
    }
}

/** Saved Search Entity */
@Entity(tableName = "saved_searches")
data class SavedSearchEntity(
    @PrimaryKey val id: String,
    val keyword: String,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val category: String? = null,
    val campus: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

/** Offline Action Queue Sync Entity */
@Entity(tableName = "sync_actions")
data class SyncActionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val actionType: String, // CREATE, UPDATE, DELETE, WISHLIST
    val entityId: String,
    val payloadJson: String,
    val timestamp: Long = System.currentTimeMillis()
)
