package com.edu.uptc.supplierandproductmanagement.interfaces


interface IActionsFile {
    fun loadFile()
    fun dumpFile(append: Boolean = false)
}