package com.edu.uptc.supplierandproductmanagement.model

data class Supplier(
    val code: String,
    var name: String,
    var city: String,
    var address: String,
    var phone: String,
    var email: String,
    var deliveryTime: Int
)
