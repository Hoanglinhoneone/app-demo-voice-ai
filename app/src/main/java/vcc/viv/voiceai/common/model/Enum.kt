package vcc.viv.voiceai.common.model

enum class TypeFilter(val value: String) {
    ALL("Tất cả"),
    PRICE_LOW_TO_HIGH("Giá từ thấp đến cao"),
    PRICE_HIGH_TO_LOW("Giá từ cao đến thấp"),
    SORT_BY_A_Z("Sắp xếp theo A-Z")
}