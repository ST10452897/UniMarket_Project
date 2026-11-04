package za.co.unimarket.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import za.co.unimarket.R
import za.co.unimarket.model.CategoryItem

class CategoryAdapter(
    private val categories: List<CategoryItem>,
    private val onItemClick: ((CategoryItem) -> Unit)? = null
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llCategoryIconBg: LinearLayout = itemView.findViewById(R.id.llCategoryIconBg)
        val ivCategoryIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val tvCategoryCount: TextView = itemView.findViewById(R.id.tvCategoryCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_card, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.tvCategoryName.text = category.name
        holder.tvCategoryCount.text = "${category.itemCount} items"
        holder.ivCategoryIcon.setImageResource(category.iconRes)

        val bgColor = ContextCompat.getColor(holder.itemView.context, category.bgColorRes)
        holder.llCategoryIconBg.backgroundTintList = ColorStateList.valueOf(bgColor)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(category)
        }
    }

    override fun getItemCount(): Int = categories.size
}
