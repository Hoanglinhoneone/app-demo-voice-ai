package vcc.viv.voiceai.data.datasource.database

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import vcc.viv.voiceai.common.model.Product
import java.io.InputStreamReader
import javax.inject.Inject

class JsonDataSource @Inject constructor(val context: Context) {

    /* **********************************************************************
     * Variable
     ***********************************************************************/
    private val FILE_PRODUCT = "product.json"

    /* **********************************************************************
     * Function
     ***********************************************************************/
    fun getProduct(): List<Product> {
        Timber.i("get products")
        val json = readJsonFromAssets(FILE_PRODUCT)
        return parseJsonToProductList(json)
    }

    private fun parseJsonToProductList(json: String): List<Product> {
        Timber.i(" parsing json")
        val productListType = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(json, productListType)
    }

    private fun readJsonFromAssets(fileName: String): String {
        Timber.i(" reading file json")
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val reader = InputStreamReader(inputStream)
        return reader.readText()
    }
}