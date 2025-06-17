package vcc.viv.voiceai.common

import vcc.viv.voiceai.common.model.Product

fun String.normalizeString(): String {
    val normalized = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
    return normalized.replace("\\p{M}".toRegex(), "")
}

fun List<Product>.sortAZ() : List<Product> {
    val list = this.toMutableList()

    return list
}