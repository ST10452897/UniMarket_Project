package za.co.unimarket

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LanguageManager {

    fun setLanguage(context: Context, language: String) {
        val code = when (language) {
            "Sepedi" -> "nso"
            else -> "en"
        }

        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(code)
        )
    }
}