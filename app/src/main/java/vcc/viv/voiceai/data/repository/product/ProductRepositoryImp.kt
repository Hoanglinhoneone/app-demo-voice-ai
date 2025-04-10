package vcc.viv.voiceai.data.repository.product

import vcc.viv.voiceai.common.model.Product
import vcc.viv.voiceai.data.datasource.database.JsonDataSource
import javax.inject.Inject

class ProductRepositoryImp @Inject constructor(
    private val jsonDataSource: JsonDataSource
) : ProductRepository {
    override suspend fun getProduct(): List<Product> {
        return jsonDataSource.getProduct()
    }
}