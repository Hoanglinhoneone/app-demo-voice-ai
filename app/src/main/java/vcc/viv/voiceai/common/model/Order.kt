package vcc.viv.voiceai.common.model

data class Order(
    val code: String = "",
    val date: String = "",
    val note: String = "",
    val listProduct: List<Product> = emptyList(),
    val quantity: Int = 0,
    val total: Float = 0f,
)
