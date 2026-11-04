package za.co.unimarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import za.co.unimarket.R
import za.co.unimarket.model.Product

class ProductAdapter(
    private var products: List<Product>,
    private val onWishlistClick: ((Product) -> Unit)? = null,
    private val onItemClick: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rlImageBg: RelativeLayout = itemView.findViewById(R.id.rlImageBg)
        val ivProductIcon: ImageView = itemView.findViewById(R.id.ivProductIcon)
        val ivWishlistHeart: ImageView = itemView.findViewById(R.id.ivWishlistHeart)
        val tvProductTitle: TextView = itemView.findViewById(R.id.tvProductTitle)
        val tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val tvProductLocation: TextView = itemView.findViewById(R.id.tvProductLocation)
        val tvSellerAvatar: TextView = itemView.findViewById(R.id.tvSellerAvatar)
        val tvSellerName: TextView = itemView.findViewById(R.id.tvSellerName)
        val ivVerifiedShield: ImageView = itemView.findViewById(R.id.ivVerifiedShield)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_card, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvProductTitle.text = product.title
        holder.tvProductPrice.text = product.formattedPrice
        holder.tvProductLocation.text = product.location
        holder.tvSellerAvatar.text = product.sellerInitials
        holder.tvSellerName.text = product.sellerName

        holder.rlImageBg.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, product.bgColorRes))
        holder.ivProductIcon.setImageResource(product.iconRes)

        if (product.isWishlisted) {
            holder.ivWishlistHeart.setImageResource(R.drawable.ic_heart_filled)
        } else {
            holder.ivWishlistHeart.setImageResource(R.drawable.ic_heart_outline)
        }

        holder.ivVerifiedShield.visibility = if (product.isVerified) View.VISIBLE else View.GONE

        holder.ivWishlistHeart.setOnClickListener {
            product.isWishlisted = !product.isWishlisted
            notifyItemChanged(position)
            onWishlistClick?.invoke(product)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateList(newList: List<Product>) {
        products = newList
        notifyDataSetChanged()
    }
}
