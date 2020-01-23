package com.education.cocktails

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.Api
import com.education.cocktails.network.TheCocktailsDBApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.education.cocktails", appContext.packageName)
    }

    private lateinit var cocktailsDBApi: TheCocktailsDBApi

    @Before
    fun init() {
        cocktailsDBApi = Api.apiService
    }

    @Test
    fun testNetworking() {
        var randomCocktails = listOf<Cocktail>()

        runBlocking {
            val response = cocktailsDBApi.getRandomAsync().await()
            if (response.isSuccessful)
                randomCocktails = response.body()?.drinks ?: listOf()
        }

        assertTrue(randomCocktails.isNotEmpty())
    }
}
