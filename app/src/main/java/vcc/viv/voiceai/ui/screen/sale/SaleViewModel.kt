package vcc.viv.voiceai.ui.screen.sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import vcc.viv.voiceai.common.model.Product
import vcc.viv.voiceai.common.model.TypeFilter
import vcc.viv.voiceai.common.normalizeString
import vcc.viv.voiceai.data.repository.product.ProductRepository
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    /* **********************************************************************
     * Variable
     ***********************************************************************/
    private val _menuUiState = MutableStateFlow(MenuUiState())
    val menuUiState: StateFlow<MenuUiState> = _menuUiState.asStateFlow()

    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val listProduct = productRepository.getProduct()
            _menuUiState.update {
                it.copy(
                    listProduct = listProduct,
                    listProductBackup = listProduct
                )
            }
        }
    }

    /* **********************************************************************
     * Function
     ***********************************************************************/
    fun onMenuAction(menuAction: MenuAction) {
        when (menuAction) {
            is MenuAction.OnInputSearchChange -> {
                val searchInput = menuAction.text
                _menuUiState.value = _menuUiState.value.copy(textSearch = searchInput)
                searchProductByText()
                if (searchInput.isEmpty()) _menuUiState.update { it.copy(listProduct = it.listProductBackup) }
            }

            is MenuAction.OnTypeFilterChange -> {
                _menuUiState.value = _menuUiState.value.copy(itemDropdown = menuAction.type.value)
                when (menuAction.type) {
                    TypeFilter.ALL -> {
                        _menuUiState.update { it.copy(listProduct = it.listProductBackup) }
                    }

                    TypeFilter.SORT_BY_A_Z -> {
                        _menuUiState.update {
                            it.copy(listProduct = it.listProduct.sortedBy { it.name })
                        }
                    }

                    TypeFilter.PRICE_HIGH_TO_LOW -> {
                        _menuUiState.update {
                            it.copy(listProduct = it.listProduct.sortedByDescending { it.price })
                        }
                    }

                    TypeFilter.PRICE_LOW_TO_HIGH -> {
                        _menuUiState.update {
                            it.copy(listProduct = it.listProduct.sortedBy { it.price })
                        }
                    }
                }
            }

            is MenuAction.OnQuantityChange -> {
                updateListProduct(menuAction.id, menuAction.quantity)
            }

            is MenuAction.OnClickAddCart -> {
                updateCart(menuAction.product)
            }
        }
    }

    private fun searchProductByText() {
        _menuUiState.update { state ->
            state.copy(listProduct = state.listProductBackup.filter { product ->
                product.name.normalizeString()
                    .contains(menuUiState.value.textSearch.normalizeString(), true)
            })
        }
    }

    fun onCartAction(cartAction: CartAction) {
        when (cartAction) {
            is CartAction.OnDeleteOrder -> {
                deleteOrder()
            }

            is CartAction.OnPrintQRCode -> {
                printQRCode()
            }

            is CartAction.OnChangeNote -> {
                _cartUiState.update { it.copy(note = cartAction.note) }
            }

            is CartAction.OnOrderNow -> {
                printQRCode()
            }
        }
    }

    private fun deleteOrder() {
        _cartUiState.update {
            it.copy(
                code = "F1743149954",
                listProduct = emptyList(),
                note = "",
                quantity = 0,
                total = 0f,
            )
        }
    }

    private fun printQRCode() {
        _cartUiState.update { it.copy(printQR = true) }
    }

    fun closeDialog() {
        _cartUiState.update { it.copy(printQR = false) }
    }

    private fun updateCart(product: Product) {
        Timber.i("update product in cart...")
        val productsInCart = _cartUiState.value.listProduct.toMutableList()
        val existingProduct = productsInCart.find { it.id == product.id }
        if (existingProduct != null) {
            existingProduct.quantity =
                (existingProduct.quantity.toInt() + product.quantity.toInt()).toString()
        } else {
            productsInCart.add(product)
        }
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
    var listProduct: List<Product> = emptyList(),
    val listProductBackup: List<Product> = emptyList()
)

data class CartUiState(
    val code: String = "F1743149954",
    val listProduct: List<Product> = emptyList(),
    val note: String = "",
    val quantity: Int = 0,
    val total: Float = 0f,
    val printQR: Boolean = false,
)