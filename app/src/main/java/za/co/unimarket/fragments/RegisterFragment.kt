package za.co.unimarket.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import za.co.unimarket.MainActivity
import za.co.unimarket.R
import za.co.unimarket.UniMarketApi

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setBottomNavVisible(false)

        val etName = view.findViewById<EditText>(R.id.etRegName)
        val etEmail = view.findViewById<EditText>(R.id.etRegEmail)
        val etPassword = view.findViewById<EditText>(R.id.etRegPassword)
        val cbGuidelines = view.findViewById<CheckBox>(R.id.cbGuidelines)

        val hintColor = Color.parseColor("#B0D5CE")
        etName?.setTextColor(Color.WHITE)
        etName?.setHintTextColor(hintColor)
        etEmail?.setTextColor(Color.WHITE)
        etEmail?.setHintTextColor(hintColor)
        etPassword?.setTextColor(Color.WHITE)
        etPassword?.setHintTextColor(hintColor)

        view.findViewById<TextView>(R.id.btnRegBack)?.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(LoginFragment())
        }

        view.findViewById<Button>(R.id.btnCreateAccount)?.setOnClickListener {
            val name = etName?.text?.toString()?.trim() ?: ""
            val email = etEmail?.text?.toString()?.trim() ?: ""
            val password = etPassword?.text?.toString()?.trim() ?: ""

            if (name.isEmpty()) {
                etName?.error = "Full name required"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                etEmail?.error = "Student email required"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                etPassword?.error = "Password required"
                return@setOnClickListener
            }
            if (cbGuidelines?.isChecked != true) {
                Toast.makeText(context, "Please agree to community guidelines", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val studentId = email.substringBefore("@")
                val result = UniMarketApi.repository.register(studentId, email, password, name, "UP Hatfield")
                result.onSuccess { auth ->
                    Toast.makeText(context, "Account created successfully for ${auth.fullName}!", Toast.LENGTH_SHORT).show()
                    (activity as? MainActivity)?.navigateToHome()
                }.onFailure { err ->
                    Toast.makeText(context, "Registration failed: ${err.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.findViewById<TextView>(R.id.tvToLogin)?.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(LoginFragment())
        }
    }
}
