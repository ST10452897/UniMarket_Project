package za.co.unimarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.unimarket.MainActivity
import za.co.unimarket.R
import za.co.unimarket.adapters.ProductAdapter
import za.co.unimarket.data.MockData

class CategoryListFragment : Fragment() {

    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setBottomNavVisible(true)

        val pillAll = view.findViewById<TextView>(R.id.pillAll)
        val pillElectronics = view.findViewById<TextView>(R.id.pillElectronics)
        val pillTextbooks = view.findViewById<TextView>(R.id.pillTextbooks)
        val pillClothing = view.findViewById<TextView>(R.id.pillClothing)

        val pills = listOf(pillAll, pillElectronics, pillTextbooks, pillClothing)

        fun selectPill(selected: TextView, categoryName: String) {
            pills.forEach { pill ->
                pill.isSelected = (pill == selected)
                pill.setTextColor(
                    if (pill == selected) requireContext().getColor(R.color.text_white)
                    else requireContext().getColor(R.color.text_primary)
                )
            }

            val filtered = if (categoryName == "All") {
                MockData.products
            } else {
                MockData.products.filter { it.category.contains(categoryName, ignoreCase = true) }
            }
            productAdapter.updateList(filtered)
        }

        productAdapter = ProductAdapter(
            products = MockData.products,
            onWishlistClick = { product ->
                Toast.makeText(context, if (product.isWishlisted) "Added to wishlist" else "Removed", Toast.LENGTH_SHORT).show()
            },
            onItemClick = { product ->
                Toast.makeText(context, "Selected: ${product.title}", Toast.LENGTH_SHORT).show()
            }
        )

        val rvListingsGrid = view.findViewById<RecyclerView>(R.id.rvListingsGrid)
        rvListingsGrid?.layoutManager = GridLayoutManager(context, 2)
        rvListingsGrid?.adapter = productAdapter

        pillAll.setOnClickListener { selectPill(pillAll, "All") }
        pillElectronics.setOnClickListener { selectPill(pillElectronics, "Electronics") }
        pillTextbooks.setOnClickListener { selectPill(pillTextbooks, "Textbooks") }
        pillClothing.setOnClickListener { selectPill(pillClothing, "Clothing") }
    }
}
