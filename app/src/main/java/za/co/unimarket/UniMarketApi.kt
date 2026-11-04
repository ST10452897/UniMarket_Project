package za.co.unimarket

import android.content.Context
import za.co.unimarket.database.AppDatabase
import za.co.unimarket.database.UniMarketLocalRepository
import za.co.unimarket.network.ApiClient
import za.co.unimarket.network.UniMarketRepository

/** Endpoint contract and repository access for UniMarket REST Services & Room DB. */
object UniMarketApi {
    const val BASE_URL = "https://unimarket-api.azurewebsites.net/"
    const val REGISTER = "api/auth/register"
    const val LOGIN = "api/auth/login"
    const val CATEGORIES = "api/categories"
    const val LISTINGS = "api/listings"
    const val WISHLIST = "api/wishlist"
    const val MESSAGES = "api/messages"
    const val SAVED_SEARCHES = "api/saved-searches"
    const val SYNC = "api/sync"

    val client: ApiClient
        get() = ApiClient

    val repository: UniMarketRepository by lazy {
        UniMarketRepository()
    }

    private var localRepositoryInstance: UniMarketLocalRepository? = null

    fun getLocalRepository(context: Context): UniMarketLocalRepository {
        return localRepositoryInstance ?: synchronized(this) {
            val repo = UniMarketLocalRepository(context.applicationContext)
            localRepositoryInstance = repo
            repo
        }
    }

    fun getDatabase(context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
}
