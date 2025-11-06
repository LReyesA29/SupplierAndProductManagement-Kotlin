package com.edu.uptc.supplierandproductmanagement.view

import com.edu.uptc.supplierandproductmanagement.model.Product
import com.edu.uptc.supplierandproductmanagement.model.ProductCategory
import com.edu.uptc.supplierandproductmanagement.model.Supplier
import com.edu.uptc.supplierandproductmanagement.model.User
import com.edu.uptc.supplierandproductmanagement.persistence.HandlingPersistence
import kotlin.system.exitProcess
class Interfaz {

    private val handlingPersistence = HandlingPersistence()
    private val reports = Reports(handlingPersistence)
    private var currentUsername: String? = null

    fun showStart() {
        handlingPersistence.loadFile()
        println("Bienvenido Al Programa de gestion de Proveedores y Productos ")
        showInfo()
    }

    fun showInfo() {

        login()
        while (true) {
            showMenu()
        }
    }

    fun showMenu() {
        println("Menu Principal ")
        println("1. Administrar usuarios")
        println("2. Administrar proveedores")
        println("3. Administrar productos")
        println("4. Reportes")
        println("5. Salir")
        val option = getInt("Seleccione una opción: ")

        when (option) {
            1 -> showAdminUsers()
            2 -> showAdminSuppliers()
            3 -> showAdminProducts()
            4 -> showReportsMenu()
            5 -> {
                println("Finalizando el Programa.....")
                println("Adioooss!!")
                exitProcess(0)
            }
            else -> println("Error intente de nuevo")
        }
    }

    fun login() {
        if (handlingPersistence.users.isEmpty()) {
            println("No hay usuarios registrados.Por favor Cree uno nuevo")
            val username = getString("Digite un Nombre de usuario: ")
            val password = getString("Digite una Contraseña: ")
            val question = getString("Digite una Pregunta de seguridad: ")
            val answer = getString("Digite la Respuesta de seguridad: ")

            val created = handlingPersistence.addUser(
                User(
                    username = username,
                    password = password,
                    securityQuestion = question,
                    securityAnswer = answer
                )
            )

            if (created) {
                println("Usuario creado exitosamente...")
            } else {
                println("Error: El Usuario ya esta Registrado")
            }
        }

        var loggedIn = false
        while (!loggedIn) {
            println()
            println("Ingresar:")
            val username = getString("Usuario: ")
            val password = getString("Contraseña: ")

            val valid = handlingPersistence.validateLogin(username, password)
            if (valid) {
                currentUsername = username
                println("Bienvenido Sr@ $username")
                loggedIn = true
            } else {
                println("Usuario y/o Contraseña Incorrectos")
            }
        }
    }

    fun showAdminUsers() {
        println()
        println("   Administrar Usuarios")
        println("1. Crear usuario")
        println("2. Mostrar usuarios")
        println("3. Actualizar contraseña")
        println("4. Eliminar usuario")
        println("5. Volver ")
        val option = getInt("Seleccione una opción: ")

        when (option) {
            1 -> createUser()
            2 -> listUsers()
            3 -> changeUserPassword()
            4 -> deleteUser()
            5 -> return
            else -> println("Opción incorrecta")
        }
    }

    private fun createUser() {
        println()
        println("   Añadir Usuario")
        val username = getString("Digite un nombre de usuario: ")
        val password = getString("Digite una Contraseña: ")
        val question = getString("Digite una Pregunta de seguridad: ")
        val answer = getString("Digite una respuesta de seguridad: ")

        val created = handlingPersistence.addUser(
            User(
                username = username,
                password = password,
                securityQuestion = question,
                securityAnswer = answer
            )
        )

        if (created) {
            println("Usuario creado exitosamente")
        } else {
            println("Error: nombre de usuario ya esta registrado")
        }
    }

    private fun listUsers() {
        println()
        println("Lista de Usuarios Registrados")
        val users = handlingPersistence.getAllUsers()
        if (users.isEmpty()) {
            println("No hay usuarios registrados")
            return
        }
        users.forEach { u ->
            println("Usuario: ${u.username}")
        }
    }

    private fun changeUserPassword() {
        println()
        println(" Actualizar contraseña de usuario")
        val username = getString("Nombre de usuario: ")

        val user = handlingPersistence.getAllUsers().find { it.username == username }
        if (user == null) {
            println("El Usuario ${username} no se encuentra Registrado")
            return
        }

        println("Pregunta de seguridad: ${user.securityQuestion}")
        val answer = getString("Respuesta: ")
        val newPassword = getString("Nueva contraseña: ")

        val updated = handlingPersistence.updateUserPassword(username, answer, newPassword)
        if (updated) {
            println("Contraseña actualizada Exitosamente...")
        } else {
            println("Respuesta de seguridad Incorrecta.Intente nuevamente")
        }
    }

    private fun deleteUser() {
        println()
        println("Borrar usuario")
        val username = getString("Nombre de usuario a eliminar: ")
        val deleted = handlingPersistence.deleteUser(username)
        if (deleted) {
            println("Usuario eliminado Exitosamente")
            showInfo()
        } else {
            println("No se encontró un usuario con ese nombre.")
        }
    }

    fun showAdminSuppliers() {
        println()
        println("Administrar Proveedores")
        println("1. Agregar proveedor")
        println("2. Buscar proveedor ")
        println("3. Actualizar proveedor")
        println("4. Eliminar proveedor")
        println("5. Mostrar Lista de proveedores")
        println("6. Volver ")
        val option = getInt("Seleccione una opción: ")

        when (option) {
            1 -> addSupplier()
            2 -> searchSupplier()
            3 -> updateSupplier()
            4 -> deleteSupplier()
            5 -> listSuppliers()
            6 -> return
            else -> println("Opción incorrecta")
        }
    }

    private fun addSupplier() {
        println()
        println("Agregar proveedor ")
        val code = getString("Código del proveedor: ")
        val name = getString("Nombre: ")
        val city = getString("Ciudad: ")
        val address = getString("Dirección: ")
        val phone = getString("Teléfono: ")
        val email = getString("Email : ")
        val deliveryTime = getInt("Días promedio de entrega: ")

        val added = handlingPersistence.addSupplier(
            Supplier(
                code = code,
                name = name,
                city = city,
                address = address,
                phone = phone,
                email = email,
                deliveryTime = deliveryTime
            )
        )

        if (added) {
            println("Proveedor agregado Exitosamente")
        } else {
            println("Error: ya existe un proveedor con ese código.")
        }
    }

    private fun searchSupplier() {
        println()
        println("Buscar proveedor ")
        val code = getString("Código del proveedor: ")
        val supplier = handlingPersistence.getSupplierByCode(code)
        if (supplier == null) {
            println("No se encontró un proveedor con ese código")
        } else {
            println("Proveedor encontrado:")
            println("Código: ${supplier.code}")
            println("Nombre: ${supplier.name}")
            println("Ciudad: ${supplier.city}")
            println("Dirección: ${supplier.address}")
            println("Teléfono: ${supplier.phone}")
            println("Correo: ${supplier.email}")
            println("Días Promedio de entrega: ${supplier.deliveryTime}")
        }
    }

    private fun updateSupplier() {
        println()
        println("Actualizar proveedor ")
        val code = getString("Código del proveedor a actualizar: ")
        val existing = handlingPersistence.getSupplierByCode(code)
        if (existing == null) {
            println("No se encontró un proveedor con ese código.")
            return
        }

        println("Para dejar el mismo deje el Campo vacio")
        val name = readOptional("Nombre (${existing.name}): ")
        val city = readOptional("Ciudad (${existing.city}): ")
        val address = readOptional("Dirección (${existing.address}): ")
        val phone = readOptional("Teléfono (${existing.phone}): ")
        val email = readOptional("Correo (${existing.email}): ")
        val deliveryTimeStr = readOptional("Días de entrega (${existing.deliveryTime}): ")

        val updated = Supplier(
            code = existing.code,
            name = if (name.isBlank()) existing.name else name,
            city = if (city.isBlank()) existing.city else city,
            address = if (address.isBlank()) existing.address else address,
            phone = if (phone.isBlank()) existing.phone else phone,
            email = if (email.isBlank()) existing.email else email,
            deliveryTime = deliveryTimeStr.toIntOrNull() ?: existing.deliveryTime
        )

        val ok = handlingPersistence.updateSupplier(updated)
        if (ok) {
            println("Proveedor se actualizo exitosamente...")
        } else {
            println("No fue Posible actualizar Proveedor")
        }
    }

    private fun deleteSupplier() {
        println()
        println("Eliminar proveedor")
        val code = getString("Código del proveedor a eliminar: ")

        val supplier = handlingPersistence.getSupplierByCode(code)
        if (supplier == null) {
            println("No Existe un proveedor con ese código")
            return
        }

        val products = handlingPersistence.getProductsBySupplier(code)

        val deletedSupplier = handlingPersistence.deleteSupplier(code)
        if (!deletedSupplier) {
            println("No fue posible eliminar el proveedor")
            return
        }

        if (products.isNotEmpty()) {
            products.forEach { product ->
                handlingPersistence.deleteProduct(product.code)
            }
            println("Proveedor eliminado correctamente.")
            println("Los productos asociados también fueron eliminados:")
            products.forEach { p ->
                println(" - Código: ${p.code} | Nombre: ${p.name}")
            }
        } else {
            println("Proveedor eliminado Exitosamente...")
            println("Este No tenía productos asociados")
        }
    }


    private fun listSuppliers() {
        println()
        println("Lista de proveedores")
        val suppliers = handlingPersistence.getAllSuppliers()
        if (suppliers.isEmpty()) {
            println("No hay proveedores registrados.")
            return
        }
        suppliers.forEach { s ->
            println("Código: ${s.code} | Nombre: ${s.name} | Ciudad: ${s.city} | Días Promedio entrega: ${s.deliveryTime}")
        }
    }

    fun showAdminProducts() {
        println()
        println("Administrar productos")
        println("1. Agregar producto")
        println("2. Buscar producto por código")
        println("3. Actualizar producto")
        println("4. Eliminar producto")
        println("5. Listar productos")
        println("6. Volver")
        val option = getInt("Seleccione una opción: ")

        when (option) {
            1 -> addProduct()
            2 -> searchProduct()
            3 -> updateProduct()
            4 -> deleteProduct()
            5 -> listProducts()
            6 -> return
            else -> println("Opción Incorrecta")
        }
    }

    private fun addProduct() {
        println()
        println("Añadir producto")
        val code = getString("Código del producto: ")
        val name = getString("Nombre del producto: ")
        val category = getProductCategory()
        val price = getDouble("Precio: ")
        val stock = getInt("Cantidad: ")
        val supplierCode = getString("Código del proveedor: ")

        val supplier = handlingPersistence.getSupplierByCode(supplierCode)
        if (supplier == null) {
            println("No existe el proveedor")
            return
        }

        val added = handlingPersistence.addProduct(
            Product(
                code = code,
                name = name,
                category = category,
                price = price,
                stock = stock,
                supplierCode = supplierCode
            )
        )

        if (added) {
            println("Producto agregado Exitosamente")
        } else {
            println("Error: ya existe un Producto con ese código")
        }
    }

    private fun searchProduct() {
        println()
        println("Buscar producto")
        val code = getString("Código del producto: ")
        val product = handlingPersistence.getProductByCode(code)
        if (product == null) {
            println("No se encontró un producto con ese código")
        } else {
            println("Producto encontrado:")
            println("Código: ${product.code}")
            println("Nombre: ${product.name}")
            println("Categoría: ${product.category}")
            println("Precio: ${product.price}")
            println("Cantidad: ${product.stock}")
            println("Código proveedor: ${product.supplierCode}")
        }
    }

    private fun updateProduct() {
        println()
        println("Actualizar producto")
        val code = getString("Código del producto a actualizar: ")
        val existing = handlingPersistence.getProductByCode(code)
        if (existing == null) {
            println("No se encontro un producto con ese código")
            return
        }

        println("Deje vacío para dejar el valor el mismo")
        val name = readOptional("Nombre (${existing.name}): ")
        val changeCategory = readOptional("Cambiar categoría? (S/N): ")
        val category = if (changeCategory.equals("S", ignoreCase = true)) {
            getProductCategory()
        } else {
            existing.category
        }
        val priceStr = readOptional("Precio (${existing.price}): ")
        val stockStr = readOptional("Cantidad (${existing.stock}): ")
        val supplierCodeInput = readOptional("Código proveedor (${existing.supplierCode}): ")

        val finalSupplierCode = if (supplierCodeInput.isBlank()) {
            existing.supplierCode
        } else {
            val supplier = handlingPersistence.getSupplierByCode(supplierCodeInput)
            if (supplier == null) {
                println("No existe un proveedor con ese código. No puedes actualizar el Proveedor")
                return
            }
            supplierCodeInput
        }

        val updated = Product(
            code = existing.code,
            name = if (name.isBlank()) existing.name else name,
            category = category,
            price = priceStr.toDoubleOrNull() ?: existing.price,
            stock = stockStr.toIntOrNull() ?: existing.stock,
            supplierCode = finalSupplierCode
        )

        val ok = handlingPersistence.updateProduct(updated)
        if (ok) {
            println("Producto actualizado Exitosamente....")
        } else {
            println("No es posible actualizar el producto")
        }
    }

    private fun deleteProduct() {
        println()
        println("Eliminar producto")
        val code = getString("Código del producto a eliminar: ")
        val deleted = handlingPersistence.deleteProduct(code)
        if (deleted) {
            println("Producto eliminado Exitosamente")
        } else {
            println("No se encontró un producto con ese código")
        }
    }

    private fun listProducts() {
        println()
        println("Lista de productos")
        val products = handlingPersistence.getAllProducts()
        if (products.isEmpty()) {
            println("No hay productos registrados")
            return
        }
        products.forEach { p ->
            println("Código: ${p.code} | Nombre: ${p.name} | Tipo: ${p.category} | Precio: ${p.price} | Cantidad: ${p.stock}")
        }
    }

    fun showReportsMenu() {
        println()
        println("Reportes")
        println("1. Gráfico de barras: Proporción de cantidad de Productos por cada Tipo")
        println("2. Gráfico de líneas: Contraste del Tiempo de entrega Promedio de cada proveedor ")
        println("3. Gráfico de torta: Proporción de cantidad de Productos por cada Tipo ")
        println("4. Volver")
        val option = getInt("Seleccione una opción: ")

        when (option) {
            1 -> reports.showBarChartStockByCategory()
            2 -> reports.showLineChartDeliveryTimeBySupplier()
            3 -> reports.showPieChartStockByCategory()
            4 -> return
            else -> println("Opción incorrecta")
        }
    }


    private fun getString(message: String): String {
        print(message)
        return readLine()?.trim().orEmpty()
    }

    private fun readOptional(message: String): String {
        print(message)
        return readLine() ?: ""
    }

    private fun getInt(message: String): Int {
        while (true) {
            print(message)
            val input = readLine()
            val value = input?.toIntOrNull()
            if (value != null) return value
            println("Entrada con Formato Incorrecto, digite un número entero")
        }
    }

    private fun getDouble(message: String): Double {
        while (true) {
            print(message)
            val input = readLine()
            val value = input?.toDoubleOrNull()
            if (value != null) return value
            println("Entrada Incorrecta, digite un número válido")
        }
    }

    private fun getProductCategory(): ProductCategory {
        println("Seleccione la tipo del producto:")
        ProductCategory.values().forEachIndexed { index, cat ->
            println("${index + 1}. ${cat.name}")
        }
        val option = getInt("Opción: ")
        val values = ProductCategory.values()
        return if (option in 1..values.size) {
            values[option - 1]
        } else {
            println("Opción inválida, se asignará ${ProductCategory.values().get(0)}")
            ProductCategory.values().get(0)
        }
    }
}
