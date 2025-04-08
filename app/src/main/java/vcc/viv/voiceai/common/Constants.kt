package vcc.viv.voiceai.common

import vcc.viv.voiceai.common.model.Product

object Constants {
    object Role {
        const val USER = "User"
        const val SYSTEM = "System"
    }
    val listProduct: List<Product> = listOf(
        Product(
            id = 1,
            name = "Trà Sữa Oolong Nướng Sương Sáo",
            price = "55",
            imageUrl = "https://product.hstatic.net/1000075078/product/1737356814_ts-oolong-nuong-suong-sao_38937aa46d4f4ccb9525df18c752ef0c.png"
        ),
        Product(
            id = 2,
            name = "Trà Sữa Oolong Tứ Quý Bơ",
            price = "59",
            imageUrl = "https://product.hstatic.net/1000075078/product/1737358282_tra-sua-oolong-tu-quy-bo_e1de7569aad44e9883d38fd31a4e9a7e_large.png"
        ),
        Product(
            id = 3,
            name = "Trà Sữa Oolong Tứ Quý Sương Sáo",
            price = "55",
            imageUrl = "https://product.hstatic.net/1000075078/product/1737358275_tra-sua-oolong-tu-quy-suong-sao_d5375b9ee8e54241a479098fadc2780d_large.png"
        ),
        Product(
            id = 4,
            name = "Hồng Trà Sữa Nóng",
            price = "55",
            imageUrl = "https://product.hstatic.net/1000075078/product/1737356747_hong-tra-sua-nong_1c7338d4cb154d8698900e24ce968f90_large.png"
        ),
        Product(
            id = 5,
            name = "Trà Sữa Oolong BLao",
            price = "39",
            imageUrl = "https://product.hstatic.net/1000075078/product/1737356772_tra-sua-oolong-blao_0790aac40efb45afaef2b1c6abbb1da3_large.png"
        ),
        Product(
            id = 6,
            name = "Frosty Cà Phê Đường Đen",
            price = "59",
            imageUrl = "https://product.hstatic.net/1000075078/product/1737355736_fosty-banh-kem-dau_090ebd3012d2403da48e067e254e9a81_large.png"
        ),
        Product(
            id = 7,
            name = "Frosty Trà Xanh",
            price = "59",
            imageUrl = "https://product.hstatic.net/1000075078/product/1737355729_fosty-choco-chip_c794168209e8478286ad443d7620ca2b_large.png"
        ),
        Product(
            id = 8,
            name = "Frosty Bánh Kem Dâu",
            price = "59",
            imageUrl = "https://product.hstatic.net/1000075078/product/1737355584_frosty-tx_27304eb485b04d3bac0e16727d5d62f1_large.png"
        ),
        Product(
            id = 9,
            name = "Frosty Choco Chip",
            price = "59",
            imageUrl = "https://product.hstatic.net/1000075078/product/1737355743_fosty-duong-den_f84cd661a8944d5a9f5db656a82c9f41_large.png"
        ),
    )
}