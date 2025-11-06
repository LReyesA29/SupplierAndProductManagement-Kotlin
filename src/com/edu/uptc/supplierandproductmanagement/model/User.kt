package com.edu.uptc.supplierandproductmanagement.model

data class User(
    val username: String,
    var password: String,
    var securityQuestion: String,
    var securityAnswer: String
)
