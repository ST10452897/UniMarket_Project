package za.co.unimarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
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

        val tvSettingsTitle = view.findViewById<TextView>(R.id.tvSettingsTitle)
        val tvSettingsInfo = view.findViewById<TextView>(R.id.tvSettingsInfo)
        val tvEnableMultiLang = view.findViewById<TextView>(R.id.tvEnableMultiLang)
        val spinnerLanguage = view.findViewById<Spinner>(R.id.spinnerLanguage)
        val switchMultiLang = view.findViewById<Switch>(R.id.switchMultiLang)
        val switchNotifications = view.findViewById<Switch>(R.id.switchNotifications)
        val switchPrivacy = view.findViewById<Switch>(R.id.switchPrivacy)

        val languages = listOf("English", "Sepedi")
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_language_item, languages)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_language_dropdown)
        spinnerLanguage?.adapter = spinnerAdapter

        spinnerLanguage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 1) { // Sepedi
                    tvSettingsTitle?.text = "Seti / Settings (Sepedi)"
                    tvSettingsInfo?.text = "UniMarket ke setšididi sa khampase se netefaditšweng. Imeile ya gago ya sekolo e boloka mmaraka o na le go kgauswi le go botega."
                    tvEnableMultiLang?.text = "Šomiša polelo tse dingwe / Enable multi-language"
                    Toast.makeText(context, "Language changed to Sepedi", Toast.LENGTH_SHORT).show()
                } else { // English
                    tvSettingsTitle?.text = "Settings (English)"
                    tvSettingsInfo?.text = getString(R.string.settings_info)
                    tvEnableMultiLang?.text = getString(R.string.settings_enable_multi_lang)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        switchMultiLang?.setOnCheckedChangeListener { _, isChecked ->
            spinnerLanguage?.isEnabled = isChecked
            Toast.makeText(context, if (isChecked) "Multi-language enabled" else "Multi-language disabled", Toast.LENGTH_SHORT).show()
        }

        switchNotifications?.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(context, if (isChecked) "Notifications ON" else "Notifications OFF", Toast.LENGTH_SHORT).show()
        }

        switchPrivacy?.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(context, if (isChecked) "Privacy active" else "Privacy standard", Toast.LENGTH_SHORT).show()
        }
    }
}
