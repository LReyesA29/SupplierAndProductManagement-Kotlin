package com.edu.uptc.supplierandproductmanagement.model

data class Supplier(
    val code: String,      // unique code, e.g.: "SUP001"
    var name: String,      // supplier name
    var city: String,      // city
    var address: String,   // address
    var phone: String,     // phone number
    var email: String,     // email
    var deliveryTime: Int  // average delivery time in days
)
