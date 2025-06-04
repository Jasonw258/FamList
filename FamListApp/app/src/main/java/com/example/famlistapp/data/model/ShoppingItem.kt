package com.example.famlistapp.data.model

import java.util.UUID

// 购物清单项目数据类
data class ShoppingItem(
    val id: String = UUID.randomUUID().toString(), // 唯一ID，模拟中客户端生成
    val name: String, // 商品名称
    val isBought: Boolean = false, // 是否已购买，默认为false
    val priority: String = ShoppingItemPriority.NORMAL.displayName, // 优先级，默认为“一般”
    val addedByUserId: String, // 添加此项目的用户ID
    val addedByUserName: String = "Unknown User", // 添加此项目的用户名（为方便显示而进行的非规范化处理）
    val timestamp: Long = System.currentTimeMillis(), // 项目添加/创建时的时间戳
    val boughtTimestamp: Long? = null, // 标记为已购买时的时间戳，可为空
    val category: String? = null, // 商品分类，可为空
    val quantity: String? = null, // 数量 (例如: "1", "2", "半")，可为空
    val unit: String? = null, // 单位 (例如: "斤", "盒", "袋")，可为空
    val purchaseLocation: String? = null // 计划购买地点 (例如: "超市", "菜市场")，可为空
)

// 购物项优先级枚举
enum class ShoppingItemPriority(val displayName: String) {
    URGENT("🔴急需"),   // 紧急
    NORMAL("🟡一般"),   // 普通
    LOW("🟢不急")     // 不紧急
}

// 从显示名称获取优先级枚举值的辅助函数，用于UI映射
fun getPriorityFromString(displayName: String): ShoppingItemPriority {
    return ShoppingItemPriority.values().find { it.displayName == displayName } ?: ShoppingItemPriority.NORMAL
}
