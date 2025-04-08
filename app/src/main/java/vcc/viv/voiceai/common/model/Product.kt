package vcc.viv.voiceai.common.model

data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val imageUrl: String,
    var quantity: String = "1",
) {
    fun total() = price.toFloat() * quantity.toFloat()
}