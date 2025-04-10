package vcc.viv.voiceai.ui.screen.home

interface CartAction {
    object OnDeleteOrder : CartAction
    object OnPrintQRCode : CartAction
    data class OnChangeNote(val note: String) : CartAction
    object OnOrderNow : CartAction
}