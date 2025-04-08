package vcc.viv.voiceai.common.model

data class Order(
    val id: Int,
    val code: String,
    val date: String,
    val note: String,
    val listProduct: List<Product> = emptyList(),
    val status: String,
    val total: String,
    val paymentMethod: String,
)
