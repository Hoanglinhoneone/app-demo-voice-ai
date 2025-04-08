package vcc.viv.voiceai.ui.screen.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import vcc.viv.voiceai.common.Constants
import vcc.viv.voiceai.common.model.Product
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    /* **********************************************************************
     * Variable
     ***********************************************************************/
    private val _menuUiState = MutableStateFlow(MenuUiState())
    val menuUiState: StateFlow<MenuUiState> = _menuUiState.asStateFlow()

    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState.asStateFlow()

    /* **********************************************************************
     * Function
     ***********************************************************************/
    fun onMenuAction(menuAction: MenuAction) {
        when (menuAction) {
            is MenuAction.OnInputSearchChange -> {
                _menuUiState.value = _menuUiState.value.copy(textSearch = menuAction.text)
            }

            is MenuAction.OnTypeFilterChange -> {
                _menuUiState.value = _menuUiState.value.copy(itemDropdown = menuAction.type)
            }

            is MenuAction.OnQuantityChange -> {
                updateListProduct(menuAction.id, menuAction.quantity)
            }

            is MenuAction.OnClickAddCart -> {
                updateCart(menuAction.product)
            }

        }
    }

    fun onCartAction(cartAction: CartAction) {
        when (cartAction) {
            is CartAction.OnDeleteOrder -> {
                deleteOrder(cartAction.id)
            }

            is CartAction.OnPrintQRCode -> {
                printQRCode(cartAction.id)
            }

            is CartAction.OnChangeNote -> {
                _cartUiState.update { it.copy(note = cartAction.note) }
            }
        }
    }

    private fun deleteOrder(id: Int) {

    }

    private fun printQRCode(id: Int) {

    }

    private fun updateCart(product: Product) {
        val productsInCart = _cartUiState.value.listProduct.toMutableList()
        productsInCart.add(product)
        _cartUiState.update {
            it.copy(
                listProduct = productsInCart,
                quantity = (it.quantity + product.quantity.toInt()),
                total = it.total + product.total()
            )
        }
        updateListProduct(product.id, "1")
    }

    private fun updateListProduct(id: Int, quantity: String) {
        _menuUiState.update { it ->
            it.copy(listProduct = it.listProduct.map { product ->
                Timber.d("updateListProduct: $product")
                if (product.id == id) {
                    product.copy(quantity = quantity)
                } else {
                    product
                }
            })
        }
    }
}

data class MenuUiState(
    val textSearch: String = "",
    val itemDropdown: String = "Tất cả",
    var listProduct: List<Product> = Constants.listProduct,
)

data class CartUiState(

    val listProduct: List<Product> = emptyList(),
    val note: String = "",
    val quantity: Int = 0,
    val total: Float = 0f,
)