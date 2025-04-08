package vcc.viv.voiceai.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import vcc.viv.voiceai.common.model.Product
import vcc.viv.voiceai.ui.component.CustomTextField

@Composable
fun ProductScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val menuUiState by viewModel.menuUiState.collectAsStateWithLifecycle()
    var isFocusSelectedSearch by remember { mutableStateOf(false) }
    val textStyle = MaterialTheme.typography.bodySmall
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Thực đơn",
            style = textStyle
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                CustomTextField(
                    value = menuUiState.textSearch,
                    onValueChange = {
                        viewModel.onMenuAction(MenuAction.OnInputSearchChange(it))
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .onFocusChanged {
                            isFocusSelectedSearch = if (it.isFocused) true else false
                        },
                    decorationBox = { innerTextField ->
                        Box(
                        ) {
                            if (menuUiState.textSearch.isEmpty() && !isFocusSelectedSearch) {
                                Text(
                                    text = "Tìm kiếm",
                                    color = MaterialTheme.colorScheme.error,
                                    style = textStyle,
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
            ItemDropdown(
                modifier = Modifier.weight(1f),
                defaultSearch = menuUiState.itemDropdown,
                onTimeChange = { it ->
                    viewModel.onMenuAction(MenuAction.OnTypeFilterChange(it))
                }
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val listFastSearch =
                listOf(
                    FastSearch(1, "tt"),
                    FastSearch(2, "muối"),
                    FastSearch(3, "trà")
                )
            listFastSearch.forEach { fastSearch ->
                ItemFastSearch(fastSearch = fastSearch)
            }
        }
        ProductView(
            products = menuUiState.listProduct,
            onQuantityChange = { quantity, id ->
                viewModel.onMenuAction(MenuAction.OnQuantityChange(quantity, id))
            },
            onClickAddCart = {
                viewModel.onMenuAction(MenuAction.OnClickAddCart(it))
            }
        )
    }
}

@Composable
fun ProductView(
    products: List<Product> = emptyList(),
    onQuantityChange: (String, Int) -> Unit,
    onClickAddCart: (Product) -> Unit
) {
    val list = products
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(list) { item ->
            ItemProduct(product = item,
                onQuantityChange = { quantity, id ->
                    onQuantityChange(quantity, id)
                },
                onClickAddCart = {
                    onClickAddCart(item)
                }
            )
        }
    }
}

@Composable
fun ItemProduct(
    product: Product,
    modifier: Modifier = Modifier,
    onQuantityChange: (String, Int) -> Unit = { String, Int -> },
    onClickAddCart: (Product) -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier.size(width = 80.dp, height = 190.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(
                horizontal = 4.dp,
                vertical = 8.dp
            )

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(width = 30.dp, height = 50.dp),
                contentScale = ContentScale.Crop,
            )
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(24.dp)
            )
            CustomTextField(
                value = product.quantity.toString(),
                onValueChange = { onQuantityChange(it, product.id) },
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp)
                    .width(36.dp),
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                singleLine = true
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(4.dp)
                ).clickable {
                    onClickAddCart(product)
                }
            ) {
                Text(
                    text = "(ID:${product.id}) Gọi món",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ItemDropdown(
    modifier: Modifier = Modifier,
    listItem: List<String> = listOf(
        "Tất cả",
        "Giá từ thấp đến cao",
        "Giá từ cao đến thấp",
        "Sắp xếp theo A-Z"
    ),
    defaultSearch: String = "Tất cả",
    onTimeChange: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.border(
            1.dp,
            MaterialTheme.colorScheme.outline,
            RoundedCornerShape(4.dp)
        ).clickable {
            expanded = !expanded
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = defaultSearch,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodySmall
            )
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "More options")
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listItem.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onTimeChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        ProductScreen(

        )
    }
}

@Composable
fun ItemFastSearch(
    modifier: Modifier = Modifier,
    fastSearch: FastSearch
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .clickable {}
            .background(
                MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(4.dp)
            )
            .size(width = 60.dp, height = 30.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${fastSearch.id}: ${fastSearch.name}",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class FastSearch(
    val id: Int,
    val name: String,
)
