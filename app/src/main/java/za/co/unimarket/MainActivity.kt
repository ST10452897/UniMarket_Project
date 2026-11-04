package za.co.unimarket

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import za.co.unimarket.fragments.CategoryListFragment
import za.co.unimarket.fragments.HomeFragment
import za.co.unimarket.fragments.MessagesFragment
import za.co.unimarket.fragments.SettingsFragment
import za.co.unimarket.fragments.SplashFragment
import za.co.unimarket.fragments.WishlistFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNavigation)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> navigateToFragment(HomeFragment(), false)
                R.id.nav_categories -> navigateToFragment(CategoryListFragment(), false)
                R.id.nav_wishlist -> navigateToFragment(WishlistFragment(), false)
                R.id.nav_messages -> navigateToFragment(MessagesFragment(), false)
                R.id.nav_settings -> navigateToFragment(SettingsFragment(), false)
                else -> false
            }
            true
        }

        if (savedInstanceState == null) {
            navigateToFragment(SplashFragment(), false)
        }
    }

    fun navigateToFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun navigateToHome() {
        bottomNav.selectedItemId = R.id.nav_home
        setBottomNavVisible(true)
    }

    fun setBottomNavVisible(visible: Boolean) {
        bottomNav.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
