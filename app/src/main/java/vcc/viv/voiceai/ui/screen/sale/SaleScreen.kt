package vcc.viv.voiceai.ui.screen.sale

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import vcc.viv.voiceai.MainViewModel
import vcc.viv.voiceai.common.getCurrentDateTime
import vcc.viv.voiceai.common.model.Order
import vcc.viv.voiceai.common.model.Product
import vcc.viv.voiceai.ui.component.CustomTextField
import vcc.viv.voiceai.ui.component.ReceiptDialog
import vcc.viv.voiceai.R

@Preview(showBackground = true)
@Composable
fun SalePreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        SaleScreen()
    }
}

@Composable
fun SaleScreen(
    modifier: Modifier = Modifier,
    viewModel: SaleViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val messages by mainViewModel.messages.collectAsState()
    var showChatView by remember { mutableStateOf(false) }
//    LaunchedEffect(
//        messages.size
//    ) {
//        showChatView = true
//        delay(3000)
//        showChatView = false
//    }
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CardUi(viewModel, modifier = Modifier.weight(0.5f))
        Box(
            modifier = Modifier.weight(0.5f)
        ) {
            ProductScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
            IconChat(
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 8.dp, bottom = 12.dp),
                onClickChatView = {
                    showChatView = true
                }
            )
            if (showChatView) {
                ChatView(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .fillMaxHeight(0.95f)
                        .align(Alignment.BottomCenter),
                    messages = messages,
                    onClickClose = {
                        showChatView = false
                    }
                )
            }

        }
    }
}

@Composable
fun CardUi(
    viewModel: SaleViewModel,
    modifier: Modifier = Modifier
) {
    val cartUiState by viewModel.cartUiState.collectAsStateWithLifecycle()
    val productsState = rememberLazyListState()
    val listProduct = cartUiState.listProduct

    LaunchedEffect(listProduct.size) {
        listProduct.let {
            if (listProduct.isNotEmpty()) productsState.animateScrollToItem(listProduct.size - 1)
        }
    }

    var isFocusedNote by remember { mutableStateOf(false) }
    val hint = "Ghi chú"
    var text by remember { mutableStateOf("") }
    val textStyle = MaterialTheme.typography.bodySmall
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
//        item {
        Text(
            text = "Giỏ hàng",
            style = MaterialTheme.typography.bodySmall
        )
//        }
//        item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.errorContainer,
                        MaterialTheme.shapes.small
                    )
            ) {
                Text(
                    text = "Code: F1743149954",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(12.dp, 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.errorContainer,
                        MaterialTheme.shapes.small
                    )
                    .clickable {
                        viewModel.onCartAction(CartAction.OnDeleteOrder)
                    }
            ) {
                Text(
                    text = "Xóa đơn",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(12.dp, 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
//        }
//        item {
        Row(
            modifier = Modifier
                .background(Color(0xFFCECFC9))
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "No",
                modifier = Modifier.width(20.dp),
                style = textStyle
            )
            Text(
                text = "Tên sản phẩm",
                modifier = Modifier.weight(1f),
                style = textStyle
            )
            Text(
                text = "Giá (k)",
                modifier = Modifier.weight(0.2f),
                style = textStyle
            )
            Text(
                text = "Q.",
                modifier = Modifier.weight(0.1f),
                style = textStyle
            )
            Text(
                text = "Tổng k",
                modifier = Modifier.weight(0.25f),
                style = textStyle
            )
        }
        LazyColumn(
            state = productsState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f),
        ) {
            itemsIndexed(cartUiState.listProduct) { index, product ->
                ItemCard(index, product)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Tổng đơn(k):",
                Modifier.align(Alignment.Center),
                style = textStyle
            )
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                Text(
                    text = cartUiState.quantity.toString(),
                    style = textStyle,
                )
                Text(
                    text = cartUiState.total.toString(),
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(65.dp),
                    style = textStyle
                )
            }
        }
        CustomTextField(
            borderColor = MaterialTheme.colorScheme.error,
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .onFocusChanged {
                    isFocusedNote = if (it.isFocused) true else false
                },
            decorationBox = { innerTextField ->
                Box {
                    if (text.isEmpty() && !isFocusedNote) {
                        Text(
                            text = hint,
                            color = MaterialTheme.colorScheme.error,
                            style = textStyle,
                        )
                    }
                    innerTextField()
                }
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.errorContainer,
                        MaterialTheme.shapes.small
                    )
                    .clickable {
                        viewModel.onCartAction(CartAction.OnOrderNow)
                    }
            ) {
                Text(
                    text = "In hóa đơn",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(12.dp, 8.dp),
                    style = textStyle
                )
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.errorContainer,
                        MaterialTheme.shapes.small
                    )
                    .clickable {
                        viewModel.onCartAction(CartAction.OnOrderNow)
                    }
            ) {
                Text(
                    text = "Đặt hàng",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(12.dp, 8.dp),
                    style = textStyle
                )
            }
            if (cartUiState.printQR) {
                ReceiptDialog(
                    onDismiss = { viewModel.closeDialog() },
                    order = Order(
                        date = getCurrentDateTime(),
                        code = cartUiState.code,
                        listProduct = cartUiState.listProduct,
                        note = cartUiState.note,
                        quantity = cartUiState.quantity,
                        total = cartUiState.total,
                    )
                )
            }
        }
//        }
    }
}

@Composable
fun ItemCard(
    index: Int,
    product: Product
) {
    val textStyle = MaterialTheme.typography.bodySmall
    Row {
        Text(
            text = index.toString(),
            modifier = Modifier
                .width(20.dp),
            style = textStyle
        )
        Text(
            text = product.name,
            style = textStyle,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = product.price,
            modifier = Modifier.weight(0.1f),
            textAlign = TextAlign.End,
            style = textStyle
        )
        Text(
            text = product.quantity,
            modifier = Modifier.weight(0.1f),
            textAlign = TextAlign.End,
            style = textStyle
        )
        Text(
            text = product.total().toString(),
            modifier = Modifier.weight(0.25f),
            textAlign = TextAlign.End,
            style = textStyle
        )
    }
}

@Composable
fun IconChat(
    modifier: Modifier = Modifier,
    onClickChatView: () -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_chat_100),
            contentDescription = null,
            modifier = modifier.clickable {
                onClickChatView()
            }
        )
    }

}
