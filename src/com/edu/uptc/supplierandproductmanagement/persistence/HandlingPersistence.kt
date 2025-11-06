package com.edu.uptc.supplierandproductmanagement.persistence

import com.edu.uptc.supplierandproductmanagement.constants.CommonConstants
import com.edu.uptc.supplierandproductmanagement.interfaces.IActionsFile
import com.edu.uptc.supplierandproductmanagement.model.*
import java.io.File

class HandlingPersistence : IActionsFile {

    var users: MutableList<User> = mutableListOf()
        private set

    var suppliers: MutableList<Supplier> = mutableListOf()
        private set

    var products: MutableList<Product> = mutableListOf()
        private set

    override fun loadFile() {
        loadUsers()
        loadSuppliers()
        loadProducts()
    }

    override fun dumpFile(append: Boolean) {
        saveUsers()
        saveSuppliers()
        saveProducts()
    }

    private fun loadUsers() {
        users = readLines(CommonConstants.USERS_FILE_PATH)
            .mapNotNull { lineToUser(it) }
            .toMutableList()
    }

    private fun saveUsers() {
        val lines = users.map { userToLine(it) }
        writeLines(CommonConstants.USERS_FILE_PATH, lines)
    }

    private fun loadSuppliers() {
        suppliers = readLines(CommonConstants.SUPPLIERS_FILE_PATH)
            .mapNotNull { lineToSupplier(it) }
            .toMutableList()
        suppliers.sortBy { it.code }
    }

    private fun saveSuppliers() {
        suppliers.sortBy { it.code }
        val lines = suppliers.map { supplierToLine(it) }
        writeLines(CommonConstants.SUPPLIERS_FILE_PATH, lines)
    }

    private fun loadProducts() {
        products = readLines(CommonConstants.PRODUCTS_FILE_PATH)
            .mapNotNull { lineToProduct(it) }
            .toMutableList()
        products.sortBy { it.code }
    }

    private fun saveProducts() {
        products.sortBy { it.code }
        val lines = products.map { productToLine(it) }
        writeLines(CommonConstants.PRODUCTS_FILE_PATH, lines)
    }

    fun addUser(user: User): Boolean {
        if (users.any { it.username == user.username }) return false
        users.add(user)
        saveUsers()
        return true
    }

    fun validateLogin(username: String, password: String): Boolean {
        return users.any { it.username == username && it.password == password }
    }

    fun getAllUsers(): List<User> = users.toList()

    fun updateUserPassword(
        username: String,
        securityAnswer: String,
        newPassword: String
    ): Boolean {
        val user = users.find { it.username == username } ?: return false
        if (!user.securityAnswer.equals(securityAnswer, ignoreCase = true)) return false
        user.password = newPassword
        saveUsers()
        return true
    }

    fun deleteUser(username: String): Boolean {
        val removed = users.removeIf { it.username == username }
        if (removed) saveUsers()
        return removed
    }

    fun addSupplier(supplier: Supplier): Boolean {
        val index = findSupplierIndexByCode(supplier.code)
        if (index >= 0) return false
        suppliers.add(supplier)
        saveSuppliers()
        return true
    }

    fun getSupplierByCode(code: String): Supplier? {
        val index = findSupplierIndexByCode(code)
        return if (index >= 0) suppliers[index] else null
    }

    fun updateSupplier(updatedSupplier: Supplier): Boolean {
        val index = findSupplierIndexByCode(updatedSupplier.code)
        if (index < 0) return false
        suppliers[index] = updatedSupplier
        saveSuppliers()
        return true
    }

    fun deleteSupplier(code: String): Boolean {
        val index = findSupplierIndexByCode(code)
        if (index < 0) return false
        suppliers.removeAt(index)
        saveSuppliers()
        return true
    }

    fun getAllSuppliers(): List<Supplier> = suppliers.toList()

    fun addProduct(product: Product): Boolean {
        val index = findProductIndexByCode(product.code)
        if (index >= 0) return false
        products.add(product)
        saveProducts()
        return true
    }

    fun getProductByCode(code: String): Product? {
        val index = findProductIndexByCode(code)
        return if (index >= 0) products[index] else null
    }

    fun updateProduct(updatedProduct: Product): Boolean {
        val index = findProductIndexByCode(updatedProduct.code)
        if (index < 0) return false
        products[index] = updatedProduct
        saveProducts()
        return true
    }

    fun deleteProduct(code: String): Boolean {
        val index = findProductIndexByCode(code)
        if (index < 0) return false
        products.removeAt(index)
        saveProducts()
        return true
    }

    fun getAllProducts(): List<Product> = products.toList()

    fun getProductsBySupplier(supplierCode: String): List<Product> =
        products.filter { it.supplierCode == supplierCode }

    fun existsProductWithSupplier(supplierCode: String): Boolean =
        products.any { it.supplierCode == supplierCode }

    private fun findSupplierIndexByCode(code: String): Int {
        var low = 0
        var high = suppliers.size - 1
        while (low <= high) {
            val mid = (low + high) / 2
            val midCode = suppliers[mid].code
            when {
                midCode == code -> return mid
                midCode < code -> low = mid + 1
                else -> high = mid - 1
            }
        }
        return -1
    }

    private fun findProductIndexByCode(code: String): Int {
        var low = 0
        var high = products.size - 1
        while (low <= high) {
            val mid = (low + high) / 2
            val midCode = products[mid].code
            when {
                midCode == code -> return mid
                midCode < code -> low = mid + 1
                else -> high = mid - 1
            }
        }
        return -1
    }

    private fun userToLine(user: User): String =
        listOf(
            user.username,
            user.password,
            user.securityQuestion,
            user.securityAnswer
        ).joinToString(CommonConstants.FILE_SEPARATOR)

    private fun lineToUser(line: String): User? {
        val parts = line.split(CommonConstants.FILE_SEPARATOR)
        if (parts.size < 4) return null
        return User(
            username = parts[0],
            password = parts[1],
            securityQuestion = parts[2],
            securityAnswer = parts[3]
        )
    }

    private fun supplierToLine(supplier: Supplier): String =
        listOf(
            supplier.code,
            supplier.name,
            supplier.city,
            supplier.address,
            supplier.phone,
            supplier.email,
            supplier.deliveryTime.toString()
        ).joinToString(CommonConstants.FILE_SEPARATOR)

    private fun lineToSupplier(line: String): Supplier? {
        val parts = line.split(CommonConstants.FILE_SEPARATOR)
        if (parts.size < 7) return null
        return Supplier(
            code = parts[0],
            name = parts[1],
            city = parts[2],
            address = parts[3],
            phone = parts[4],
            email = parts[5],
            deliveryTime = parts[6].toIntOrNull() ?: 0
        )
    }

    private fun productToLine(product: Product): String =
        listOf(
            product.code,
            product.name,
            product.category.name,
            product.price.toString(),
            product.stock.toString(),
            product.supplierCode
        ).joinToString(CommonConstants.FILE_SEPARATOR)

    private fun lineToProduct(line: String): Product? {
        val parts = line.split(CommonConstants.FILE_SEPARATOR)
        if (parts.size < 6) return null
        val category = try {
            ProductCategory.valueOf(parts[2])
        } catch (e: IllegalArgumentException) {
            return null
        }
        return Product(
            code = parts[0],
            name = parts[1],
            category = category,
            price = parts[3].toDoubleOrNull() ?: 0.0,
            stock = parts[4].toIntOrNull() ?: 0,
            supplierCode = parts[5]
        )
    }

    private fun readLines(path: String): List<String> {
        val file = File(path)
        if (!file.exists()) return emptyList()
        return file.readLines().filter { it.isNotBlank() }
    }

    private fun writeLines(path: String, lines: List<String>) {
        val file = File(path)
        if (lines.isEmpty()) {
            file.writeText("")
        } else {
            file.writeText(lines.joinToString("\n") + "\n")
        }
    }
}
