package za.co.unimarket.data

import za.co.unimarket.R
import za.co.unimarket.model.CategoryItem
import za.co.unimarket.model.MessageThread
import za.co.unimarket.model.NotificationItem
import za.co.unimarket.model.Product

object MockData {
    val categories = listOf(
        CategoryItem("1", "Electronics", 128, R.drawable.ic_laptop, R.color.category_purple_bg),
        CategoryItem("2", "Textbooks", 94, R.drawable.ic_book, R.color.category_amber_bg),
        CategoryItem("3", "Clothing", 76, R.drawable.ic_shirt, R.color.category_green_bg),
        CategoryItem("4", "Other", 52, R.drawable.ic_box, R.color.category_blue_bg)
    )

    val products = mutableListOf(
        Product(
            id = "p1",
            title = "MacBook Air M1",
            price = 8500.0,
            category = "Electronics",
            location = "Electronics · UP Hatfield",
            sellerName = "Thabo M.",
            sellerInitials = "T",
            isVerified = true,
            isWishlisted = false,
            iconRes = R.drawable.ic_laptop,
            bgColorRes = R.color.category_purple_bg
        ),
        Product(
            id = "p2",
            title = "Calculus Textbook",
            price = 350.0,
            category = "Textbooks",
            location = "Textbooks · Tuks Village",
            sellerName = "Naledi K.",
            sellerInitials = "N",
            isVerified = true,
            isWishlisted = true,
            iconRes = R.drawable.ic_book,
            bgColorRes = R.color.category_amber_bg
        ),
        Product(
            id = "p3",
            title = "Vintage Denim Jacket",
            price = 280.0,
            category = "Clothing",
            location = "Clothing · Hatfield Square",
            sellerName = "Mpho S.",
            sellerInitials = "M",
            isVerified = true,
            isWishlisted = true,
            iconRes = R.drawable.ic_shirt,
            bgColorRes = R.color.category_green_bg
        ),
        Product(
            id = "p4",
            title = "Scientific Calculator FX-991",
            price = 180.0,
            category = "Electronics",
            location = "Electronics · Auckland Park",
            sellerName = "Amogelang M.",
            sellerInitials = "A",
            isVerified = true,
            isWishlisted = false,
            iconRes = R.drawable.ic_laptop,
            bgColorRes = R.color.category_purple_bg
        ),
        Product(
            id = "p5",
            title = "Desk Lamp LED",
            price = 120.0,
            category = "Other",
            location = "Other · Braamfontein",
            sellerName = "Jordan L.",
            sellerInitials = "J",
            isVerified = true,
            isWishlisted = false,
            iconRes = R.drawable.ic_box,
            bgColorRes = R.color.category_blue_bg
        ),
        Product(
            id = "p6",
            title = "Mini Portable Speaker",
            price = 450.0,
            category = "Electronics",
            location = "Electronics · UP Hatfield",
            sellerName = "Maya C.",
            sellerInitials = "M",
            isVerified = true,
            isWishlisted = false,
            iconRes = R.drawable.ic_laptop,
            bgColorRes = R.color.category_purple_bg
        )
    )

    val messages = listOf(
        MessageThread(
            id = "m1",
            senderName = "Jordan Lee",
            senderInitials = "JL",
            lastMessage = "Is this still available?",
            timestamp = "2m",
            unread = true
        ),
        MessageThread(
            id = "m2",
            senderName = "Maya Chen",
            senderInitials = "MC",
            lastMessage = "Thanks, that works!",
            timestamp = "Yesterday",
            unread = false
        ),
        MessageThread(
            id = "m3",
            senderName = "Noah Williams",
            senderInitials = "NW",
            lastMessage = "Thanks, that works!",
            timestamp = "Yesterday",
            unread = false
        )
    )

    val notifications = listOf(
        NotificationItem(
            id = "n1",
            title = "Wishlist Match",
            message = "Calculus Textbook price dropped to R350!",
            timeAgo = "10m ago",
            isRead = false
        ),
        NotificationItem(
            id = "n2",
            title = "New Message",
            message = "Jordan Lee sent a message about Scientific Calculator",
            timeAgo = "1h ago",
            isRead = false
        ),
        NotificationItem(
            id = "n3",
            title = "Safety Tip",
            message = "Always complete exchanges at official campus safe exchange zones.",
            timeAgo = "2d ago",
            isRead = true
        )
    )
}
