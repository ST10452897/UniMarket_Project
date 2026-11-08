package za.co.unimarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.unimarket.MainActivity
import za.co.unimarket.R
import za.co.unimarket.UniMarketApi
import za.co.unimarket.adapters.CategoryAdapter
import za.co.unimarket.adapters.ProductAdapter
import za.co.unimarket.data.MockData
import za.co.unimarket.network.ApiClient
import za.co.unimarket.model.Product

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setBottomNavVisible(true)

        view.findViewById<FrameLayout>(R.id.btnNotificationBell)?.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(NotificationsFragment())
        }

        view.findViewById<LinearLayout>(R.id.btnSearchBar)?.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(SearchFilterFragment())
        }

        view.findViewById<TextView>(R.id.tvViewAllCategories)?.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(CategoryListFragment())
        }

        view.findViewById<TextView>(R.id.tvSeeAllListings)?.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(CategoryListFragment())
        }

        // Setup Categories Grid
        val rvCategories = view.findViewById<RecyclerView>(R.id.rvCategories)
        rvCategories?.layoutManager = GridLayoutManager(context, 2)
        val categoryAdapter = CategoryAdapter(MockData.categories) { category ->
            (activity as? MainActivity)?.navigateToFragment(CategoryListFragment())
        }
        rvCategories?.adapter = categoryAdapter

        // Setup Fresh Listings Grid
        val rvFreshListings = view.findViewById<RecyclerView>(R.id.rvFreshListings)
        rvFreshListings?.layoutManager = GridLayoutManager(context, 2)
        val productAdapter = ProductAdapter(
            products = mutableListOf(),
            onWishlistClick = { product ->
                viewLifecycleOwner.lifecycleScope.launch {
                    val localRepo = UniMarketApi.getLocalRepository(requireContext())
                    localRepo.toggleWishlist(product)
                    Toast.makeText(context, if (product.isWishlisted) "Added to wishlist" else "Removed from wishlist", Toast.LENGTH_SHORT).show()
                }
            },
            onItemClick = { product ->
                Toast.makeText(context, "Clicked: ${product.title} (${product.formattedPrice})", Toast.LENGTH_SHORT).show()
            }
        )
        rvFreshListings?.adapter = productAdapter

        // Fetch real marketplace listings from Firestore via the API
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getListings()

                if (response.isSuccessful) {
                    val listings = response.body().orEmpty()

                    val realProducts = listings.map { listing ->
                        val category = listing.category.ifBlank { "Textbooks" }

                        val (icon, background) = when (category.lowercase()) {
                            "electronics" -> R.drawable.ic_laptop to R.color.category_purple_bg
                            "textbooks", "books" -> R.drawable.ic_book to R.color.category_amber_bg
                            "clothing" -> R.drawable.ic_shirt to R.color.category_green_bg
                            else -> R.drawable.ic_box to R.color.category_blue_bg
                        }

                        val seller = listing.sellerName.ifBlank { "Campus Seller" }

                        Product(
                            id = listing.id,
                            title = listing.title,
                            price = listing.price,
                            category = category,
                            location = "${category} · ${listing.campus}",
                            sellerName = seller,
                            sellerInitials = seller.take(1).uppercase(),
                            isVerified = false,
                            isWishlisted = listing.isWishlisted,
                            iconRes = icon,
                            bgColorRes = background
                        )
                    }

                    productAdapter.updateList(realProducts)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Could not load listings: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Connection error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        // Observe Room DB Flows & Sync Network Data
        val localRepo = UniMarketApi.getLocalRepository(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            localRepo.getCategoriesFlow().collect { categories ->
                if (categories.isNotEmpty()) {
                    rvCategories?.adapter = CategoryAdapter(categories) {
                        (activity as? MainActivity)?.navigateToFragment(CategoryListFragment())
                    }
                }
            }
        }

       // viewLifecycleOwner.lifecycleScope.launch {
           // localRepo.getProductsFlow().collect { products ->
               // if (products.isNotEmpty()) {
                  //  productAdapter.updateList(products)
               // }
            //}
        }
    }
//}
