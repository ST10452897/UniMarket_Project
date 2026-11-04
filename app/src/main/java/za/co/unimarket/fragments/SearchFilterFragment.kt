package za.co.unimarket.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.unimarket.MainActivity
import za.co.unimarket.R
import za.co.unimarket.UniMarketApi
import za.co.unimarket.adapters.ProductAdapter
import za.co.unimarket.data.MockData
import za.co.unimarket.model.Product

class SearchFilterFragment : Fragment() {

    private lateinit var adapter: ProductAdapter
    private var currentBudgetMax: Double? = null
    private var currentQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setBottomNavVisible(true)

        val etSearchQuery = view.findViewById<EditText>(R.id.etSearchQuery)
        val btnClearSearch = view.findViewById<ImageView>(R.id.btnClearSearch)

        val chipUnder500 = view.findViewById<TextView>(R.id.chipUnder500)
        val chip500to2000 = view.findViewById<TextView>(R.id.chip500to2000)
        val chip2000plus = view.findViewById<TextView>(R.id.chip2000plus)
        val chipClearBudget = view.findViewById<TextView>(R.id.chipClearBudget)

        val chips = listOf(chipUnder500, chip500to2000, chip2000plus)

        fun filterProducts() {
            var result = MockData.products.asSequence()
            if (currentQuery.isNotBlank()) {
                result = result.filter { it.title.contains(currentQuery, ignoreCase = true) || it.category.contains(currentQuery, ignoreCase = true) }
            }
            if (currentBudgetMax != null) {
                result = when (currentBudgetMax) {
                    500.0 -> result.filter { it.price < 500.0 }
                    2000.0 -> result.filter { it.price in 500.0..2000.0 }
                    2001.0 -> result.filter { it.price > 2000.0 }
                    else -> result
                }
            }
            adapter.updateList(result.toList())
        }

        adapter = ProductAdapter(
            products = MockData.products,
            onWishlistClick = { product ->
                viewLifecycleOwner.lifecycleScope.launch {
                    UniMarketApi.repository.saveWishlist(product.id)
                    Toast.makeText(context, if (product.isWishlisted) "Saved to wishlist" else "Removed", Toast.LENGTH_SHORT).show()
                }
            },
            onItemClick = { product ->
                Toast.makeText(context, "Selected: ${product.title}", Toast.LENGTH_SHORT).show()
            }
        )

        val rvSearchResults = view.findViewById<RecyclerView>(R.id.rvSearchResults)
        rvSearchResults?.layoutManager = GridLayoutManager(context, 2)
        rvSearchResults?.adapter = adapter

        etSearchQuery?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentQuery = s?.toString()?.trim() ?: ""
                filterProducts()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnClearSearch?.setOnClickListener {
            etSearchQuery?.setText("")
            currentQuery = ""
            filterProducts()
        }

        fun selectChip(selectedChip: TextView?, maxBudget: Double?) {
            chips.forEach { chip ->
                chip.isSelected = (chip == selectedChip)
                chip.setTextColor(
                    if (chip == selectedChip) requireContext().getColor(R.color.text_white)
                    else requireContext().getColor(R.color.text_primary)
                )
            }
            currentBudgetMax = maxBudget
            filterProducts()
        }

        chipUnder500?.setOnClickListener { selectChip(chipUnder500, 500.0) }
        chip500to2000?.setOnClickListener { selectChip(chip500to2000, 2000.0) }
        chip2000plus?.setOnClickListener { selectChip(chip2000plus, 2001.0) }
        chipClearBudget?.setOnClickListener { selectChip(null, null) }
    }
}
