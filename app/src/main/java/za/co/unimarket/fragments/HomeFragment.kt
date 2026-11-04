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
            products = MockData.products,
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

        viewLifecycleOwner.lifecycleScope.launch {
            localRepo.getProductsFlow().collect { products ->
                if (products.isNotEmpty()) {
                    productAdapter.updateList(products)
                }
            }
        }
    }
}
