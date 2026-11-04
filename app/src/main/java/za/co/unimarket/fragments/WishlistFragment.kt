package za.co.unimarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import za.co.unimarket.MainActivity
import za.co.unimarket.R
import za.co.unimarket.UniMarketApi
import za.co.unimarket.adapters.ProductAdapter
import za.co.unimarket.data.MockData

class WishlistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setBottomNavVisible(true)

        val wishlistedProducts = MockData.products.filter { it.isWishlisted }

        val rvWishlistGrid = view.findViewById<RecyclerView>(R.id.rvWishlistGrid)
        rvWishlistGrid?.layoutManager = GridLayoutManager(context, 2)
        val adapter = ProductAdapter(
            products = wishlistedProducts,
            onWishlistClick = { product ->
                viewLifecycleOwner.lifecycleScope.launch {
                    val localRepo = UniMarketApi.getLocalRepository(requireContext())
                    localRepo.toggleWishlist(product)
                    Toast.makeText(context, "Wishlist updated", Toast.LENGTH_SHORT).show()
                }
            },
            onItemClick = { product ->
                Toast.makeText(context, "Selected: ${product.title}", Toast.LENGTH_SHORT).show()
            }
        )
        rvWishlistGrid?.adapter = adapter

        val localRepo = UniMarketApi.getLocalRepository(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            localRepo.getWishlistedProductsFlow().collect { items ->
                adapter.updateList(items)
            }
        }
    }
}
