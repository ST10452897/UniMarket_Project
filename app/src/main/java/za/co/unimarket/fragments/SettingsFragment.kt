package za.co.unimarket.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import za.co.unimarket.LanguageManager
import za.co.unimarket.MainActivity
import za.co.unimarket.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setBottomNavVisible(true)

        val spinnerLanguage = view.findViewById<Spinner>(R.id.spinnerLanguage)
        val switchMultiLang = view.findViewById<Switch>(R.id.switchMultiLang)
        val switchNotifications = view.findViewById<Switch>(R.id.switchNotifications)
        val switchPrivacy = view.findViewById<Switch>(R.id.switchPrivacy)

        val prefs = requireContext().getSharedPreferences(
            "unimarket_settings", Context.MODE_PRIVATE
        )

        // ----- Language spinner -----
        val languages = listOf("English", "Sepedi")
        val spinnerAdapter = ArrayAdapter(
            requireContext(), R.layout.spinner_language_item, languages
        )
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_language_dropdown)
        spinnerLanguage?.adapter = spinnerAdapter

        // Show the language the app is currently using
        val currentTag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        val currentIndex = if (currentTag.startsWith("nso")) 1 else 0
        spinnerLanguage?.setSelection(currentIndex, false)

        spinnerLanguage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, v: View?, position: Int, id: Long
            ) {
                // Only change language if the user picked a different one
                if (position != currentIndex) {
                    LanguageManager.setLanguage(requireContext(), languages[position])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // ----- Multi-language switch (saved) -----
        switchMultiLang?.setOnCheckedChangeListener(null)
        switchMultiLang?.isChecked = prefs.getBoolean("multi_language_enabled", true)
        spinnerLanguage?.isEnabled = switchMultiLang?.isChecked ?: true
        switchMultiLang?.setOnCheckedChangeListener { _, isChecked ->
            spinnerLanguage?.isEnabled = isChecked
            prefs.edit().putBoolean("multi_language_enabled", isChecked).apply()
        }

        // ----- Notifications switch (saved) -----
        switchNotifications?.setOnCheckedChangeListener(null)
        switchNotifications?.isChecked = prefs.getBoolean("notifications_enabled", true)
        switchNotifications?.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply()
            Toast.makeText(
                context,
                if (isChecked) "Notifications ON" else "Notifications OFF",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ----- Privacy switch (saved) -----
        switchPrivacy?.setOnCheckedChangeListener(null)
        switchPrivacy?.isChecked = prefs.getBoolean("privacy_enabled", false)
        switchPrivacy?.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("privacy_enabled", isChecked).apply()
            Toast.makeText(
                context,
                if (isChecked) "Privacy active" else "Privacy standard",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}