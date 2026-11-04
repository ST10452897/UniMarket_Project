package za.co.unimarket.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import za.co.unimarket.MainActivity
import za.co.unimarket.R
import za.co.unimarket.UniMarketApi

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setBottomNavVisible(false)

        val etEmail = view.findViewById<EditText>(R.id.etLoginEmail)
        val etPassword = view.findViewById<EditText>(R.id.etLoginPassword)

        etEmail?.setTextColor(Color.WHITE)
        etEmail?.setHintTextColor(Color.parseColor("#B0D5CE"))
        etPassword?.setTextColor(Color.WHITE)
        etPassword?.setHintTextColor(Color.parseColor("#B0D5CE"))

        view.findViewById<TextView>(R.id.btnBack)?.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(SplashFragment())
        }

        view.findViewById<LinearLayout>(R.id.btnGoogleLogin)?.setOnClickListener {
            Toast.makeText(context, "Signing in with Google SSO...", Toast.LENGTH_SHORT).show()
            (activity as? MainActivity)?.navigateToHome()
        }

        view.findViewById<Button>(R.id.btnLoginSubmit)?.setOnClickListener {
            val email = etEmail?.text?.toString()?.trim() ?: ""
            val password = etPassword?.text?.toString()?.trim() ?: ""

            if (email.isEmpty()) {
                etEmail?.error = "Email is required"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                etPassword?.error = "Password is required"
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val result = UniMarketApi.repository.login(email, password)
                result.onSuccess { auth ->
                    Toast.makeText(context, "Welcome back, ${auth.fullName}!", Toast.LENGTH_SHORT).show()
                    (activity as? MainActivity)?.navigateToHome()
                }.onFailure { err ->
                    Toast.makeText(context, "Login failed: ${err.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.findViewById<TextView>(R.id.tvToRegister)?.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(RegisterFragment())
        }
    }
}
