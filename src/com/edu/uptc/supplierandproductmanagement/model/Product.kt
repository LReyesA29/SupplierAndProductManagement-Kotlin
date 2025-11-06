package com.edu.uptc.supplierandproductmanagement.model

data class Product(
    val code: String,
    var name: String,
    var category: ProductCategory,
    var price: Double,
    var stock: Int,
    var supplierCode: String
)
