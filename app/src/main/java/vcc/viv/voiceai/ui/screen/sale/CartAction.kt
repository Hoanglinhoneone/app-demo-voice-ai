package vcc.viv.voiceai.ui.screen.sale

interface CartAction {

    object OnDeleteOrder : CartAction

    data class OnDeleteProduct(val productId: Int, val quantity: Int) : CartAction
    
    object OnPrintQRCode : CartAction
    data class OnChangeNote(val note: String) : CartAction
    object OnOrderNow : CartAction
}