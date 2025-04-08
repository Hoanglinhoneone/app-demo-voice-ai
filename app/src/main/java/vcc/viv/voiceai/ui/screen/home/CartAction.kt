package vcc.viv.voiceai.ui.screen.home

interface CartAction {
    data class OnDeleteOrder(val id: Int) : CartAction
    data class OnPrintQRCode(val id: Int) : CartAction
    data class OnChangeNote(val note: String) : CartAction
    data class OnOrderNow(val id: Int) : CartAction
}