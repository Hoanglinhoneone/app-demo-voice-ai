package vcc.viv.voiceai.data.repository.product

import vcc.viv.voiceai.common.model.Product

interface ProductRepository {
    suspend fun getProduct(): List<Product>
}