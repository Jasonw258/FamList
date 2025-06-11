package com.example.famlistapp.data

object LocalizedData {
    val categories = mapOf(
        "dairy" to "乳制品",
        "meat" to "肉类",
        "vegetables" to "蔬菜",
        "fruits" to "水果",
        "grains" to "谷物",
        "snacks" to "零食"
    )
    val priorities = mapOf(
        "high" to "高",
        "medium" to "中",
        "low" to "低"
    )
    val units = mapOf(
        "kg" to "公斤",
        "g" to "克",
        "l" to "升",
        "ml" to "毫升",
        "pieces" to "个"
    )
    object DefaultPurchaseLocations {
        val locations = listOf("超市", "便利店", "菜市场", "电商平台")
    }
} 