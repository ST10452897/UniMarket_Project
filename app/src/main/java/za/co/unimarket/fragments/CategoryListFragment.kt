
package za.co.unimarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import za.co.unimarket.MainActivity
import za.co.unimarket.R
import za.co.unimarket.adapters.ProductAdapter
import za.co.unimarket.model.Product
import za.co.unimarket.network.ApiClient

class CategoryListFragment : Fragment() {

    private lateinit var productAdapter: ProductAdapter

    private var allProducts: List<Product> = emptyList()
    private var selectedCategory = "All"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_category_list,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.setBottomNavVisible(true)

        val pillAll = view.findViewById<TextView>(R.id.pillAll)
        val pillElectronics = view.findViewById<TextView>(R.id.pillElectronics)
        val pillTextbooks = view.findViewById<TextView>(R.id.pillTextbooks)
        val pillClothing = view.findViewById<TextView>(R.id.pillClothing)

        val pills = listOf(
            pillAll,
            pillElectronics,
            pillTextbooks,
            pillClothing
        )

        fun filterProducts(categoryName: String) {
            selectedCategory = categoryName

            val filtered = if (categoryName == "All") {
                allProducts
            } else {
                allProducts.filter {
                    it.category.contains(categoryName, ignoreCase = true) ||
                            (categoryName == "Textbooks" &&
                                    it.category.equals("Books", ignoreCase = true))
                }
            }

            productAdapter.updateList(filtered)
        }

        fun selectPill(selected: TextView, categoryName: String) {
            pills.forEach { pill ->
                pill.isSelected = pill == selected
                pill.setTextColor(
                    if (pill == selected)
                        requireContext().getColor(R.color.text_white)
                    else
                        requireContext().getColor(R.color.text_primary)
                )
            }

            filterProducts(categoryName)
        }

        productAdapter = ProductAdapter(
            products = emptyList(),
            onWishlistClick = { product ->
                Toast.makeText(
                    context,
                    if (product.isWishlisted)
                        "Added to wishlist"
                    else
                        "Removed",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onItemClick = { product ->
                Toast.makeText(
                    context,
                    "Selected: ${product.title}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        val rvListingsGrid =
            view.findViewById<RecyclerView>(R.id.rvListingsGrid)

        rvListingsGrid.layoutManager = GridLayoutManager(context, 2)
        rvListingsGrid.adapter = productAdapter

        pillAll.setOnClickListener {
            selectPill(pillAll, "All")
        }

        pillElectronics.setOnClickListener {
            selectPill(pillElectronics, "Electronics")
        }

        pillTextbooks.setOnClickListener {
            selectPill(pillTextbooks, "Textbooks")
        }

        pillClothing.setOnClickListener {
            selectPill(pillClothing, "Clothing")
        }

        // Load real listings from the ASP.NET Core API
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getListings()

                if (response.isSuccessful) {
                    val listings = response.body().orEmpty()

                    allProducts = listings.map { listing ->
                        val category =
                            listing.category.ifBlank { "Textbooks" }

                        val (icon, background) =
                            when (category.lowercase()) {
                                "electronics" ->
                                    R.drawable.ic_laptop to
                                            R.color.category_purple_bg

                                "textbooks", "books" ->
                                    R.drawable.ic_book to
                                            R.color.category_amber_bg

                                "clothing" ->
                                    R.drawable.ic_shirt to
                                            R.color.category_green_bg

                                else ->
                                    R.drawable.ic_box to
                                            R.color.category_blue_bg
                            }

                        val seller = listing.sellerName?.takeIf { it.isNotBlank() } ?: "Campus Seller"

                        Product(
                            id = listing.id,
                            title = listing.title,
                            price = listing.price,
                            category = category,
                            location = "$category · ${listing.campus}",
                            sellerName = seller,
                            sellerInitials = seller.take(1).uppercase(),
                            isVerified = false,
                            isWishlisted = listing.isWishlisted,
                            iconRes = icon,
                            bgColorRes = background
                        )
                    }

                    filterProducts(selectedCategory)

                    Toast.makeText(
                        requireContext(),
                        "Loaded ${allProducts.size} real listings",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Could not load listings: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                if (isAdded) {
                    Toast.makeText(
                        requireContext(),
                        "Connection error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
