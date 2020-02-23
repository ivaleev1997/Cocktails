package com.education.cocktails

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.education.cocktails.model.Cocktail
import com.education.cocktails.network.TheCocktailsApi
import com.education.cocktails.network.TheCocktailsApiService
import com.education.cocktails.util.jsonDeserializer
import com.google.gson.GsonBuilder
import junit.framework.Assert.assertFalse
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

    private lateinit var cocktailsApi: TheCocktailsApi

    @Before
    fun init() {
        cocktailsApi = TheCocktailsApiService.API_SERVICE
    }

    @Test
    fun testNetworking() {
        var randomCocktails = listOf<Cocktail>()

        runBlocking {
            val response = cocktailsApi.getRandomCocktailAsync()
            if (response.isSuccessful)
                randomCocktails = response.body()?.drinks ?: listOf()
        }

        assertTrue(randomCocktails.isNotEmpty())
    }

    @Test
    fun customDeserializer_Test() {
        val expectedId = 11007L
        val gson = GsonBuilder().registerTypeAdapter(Cocktail::class.java, jsonDeserializer).create()
        val cocktail = gson.fromJson(cocktailJson, Cocktail::class.java)

        assertEquals(expectedId, cocktail.idDrink)
        assertFalse(cocktail.ingredientWithMeasure.isNullOrEmpty())
    }

    @Test
    fun customDeserializerLiteCocktailJson_Test() {
        val expectedId = 14029L
        val gson = GsonBuilder().registerTypeAdapter(Cocktail::class.java, jsonDeserializer).create()
        val cocktail = gson.fromJson(cocktailLiteJson, Cocktail::class.java)

        assertEquals(expectedId, cocktail.idDrink)
        assertTrue(!cocktail.image.isNullOrEmpty())
        assertTrue(cocktail.drink.isNotEmpty())
        assertTrue(cocktail.ingredientWithMeasure.isNullOrEmpty())
    }

/*    @Test
    fun saveMapStringsInDB_Test() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val appDb = Room
            .inMemoryDatabaseBuilder(appContext, CocktailsDb::class.java)
            .build()
        val cocktailsDao = appDb.getCocktailDao()

        val gson = GsonBuilder().registerTypeAdapter(Cocktail::class.java, jsonDeserializer).create()
        val cocktail = gson.fromJson(cocktailJson, Cocktail::class.java)

        cocktailsDao.insertCocktails(listOf(cocktail))
        val actualCocktail = getValue(cocktailsDao.getCocktailById(cocktail.idDrink)).first()

        assertTrue(cocktail.ingredientWithMeasure["Tequila"] == actualCocktail.ingredientWithMeasure["Tequila"])

    }*/

}
