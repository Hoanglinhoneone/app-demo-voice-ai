package vcc.viv.voiceai.ui.screen.sale

import vcc.viv.voiceai.common.model.Product
import vcc.viv.voiceai.common.model.TypeFilter

interface MenuAction {

    data class OnSearchProduct(val textInput: String, val filter: String) : MenuAction
    data class OnInputSearchChange(val text: String) : MenuAction
    data class OnTypeFilterChange(val type: TypeFilter) : MenuAction
    data class OnQuantityChange(val quantity: String, val id: Int) : MenuAction
    data class OnClickAddCart(val product: Product) : MenuAction
}

