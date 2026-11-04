package za.co.unimarket.model

import java.util.Locale

data class Product(
    val id: String,
    val title: String,
    val price: Double,
    val category: String,
    val location: String,
    val sellerName: String,
    val sellerInitials: String,
    val isVerified: Boolean = true,
    var isWishlisted: Boolean = false,
    val iconRes: Int,
    val bgColorRes: Int
) {
    val formattedPrice: String
        get() = if (price >= 1000) "R${String.format(Locale.getDefault(), "%,.0f", price)}" else "R${price.toInt()}"
}

data class CategoryItem(
    val id: String,
    val name: String,
    val itemCount: Int,
    val iconRes: Int,
    val bgColorRes: Int
)

data class MessageThread(
    val id: String,
    val senderName: String,
    val senderInitials: String,
    val lastMessage: String,
    val timestamp: String,
    val avatarBgColor: String = "#FFB74D",
    val unread: Boolean = false
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false
)
