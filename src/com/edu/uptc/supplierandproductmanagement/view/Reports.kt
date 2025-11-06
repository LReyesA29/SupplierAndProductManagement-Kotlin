package com.edu.uptc.supplierandproductmanagement.view
import com.edu.uptc.supplierandproductmanagement.model.ProductCategory
import com.edu.uptc.supplierandproductmanagement.persistence.HandlingPersistence
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.data.general.DefaultPieDataset
import javax.swing.JFrame
import javax.swing.SwingUtilities

class Reports(private val handlingPersistence: HandlingPersistence) {

    fun showBarChartStockByCategory() {
        val products = handlingPersistence.getAllProducts()
        val dataset = DefaultCategoryDataset()
        ProductCategory.values().forEach { category ->
            val totalStock = products.filter { it.category == category }.sumOf { it.stock }
            dataset.addValue(totalStock.toDouble(), "Cantidad", category.name)
        }
        val chart = ChartFactory.createBarChart(
            "Cantidad de productos de cada Tipo",
            "Tipo de Producto",
            "Cantidad Disponible",
            dataset
        )
        showChart(chart, "Gráfico de barras")
    }

    fun showLineChartDeliveryTimeBySupplier() {
        val suppliers = handlingPersistence.getAllSuppliers()
        val dataset = DefaultCategoryDataset()
        suppliers.forEach { supplier ->
            dataset.addValue(supplier.deliveryTime.toDouble(), "#Días Promedio entrega", supplier.name)
        }
        val chart = ChartFactory.createLineChart(
            "Contraste del Tiempo de entrega Promedio de cada proveedor",
            "Proveedor",
            "# Días Promedio de entrega",
            dataset
        )
        showChart(chart, "Gráfico de líneas")
    }

    fun showPieChartStockByCategory() {
        val products = handlingPersistence.getAllProducts()
        val dataset = DefaultPieDataset<String>()
        ProductCategory.values().forEach { category ->
            val totalStock = products.filter { it.category == category }.sumOf { it.stock }
            if (totalStock > 0) {
                dataset.setValue(category.name, totalStock.toDouble())
            }
        }
        val chart = ChartFactory.createPieChart(
            "Proporción de cantidad de Productos por cada Tipo",
            dataset,
            true,
            true,
            false
        )
        showChart(chart, "Gráfico de torta ")
    }

    private fun showChart(chart: JFreeChart, title: String) {
        SwingUtilities.invokeLater {
            val frame = JFrame(title)
            frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            frame.contentPane = ChartPanel(chart)
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }
}
