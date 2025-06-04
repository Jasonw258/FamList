package com.example.famlistapp.data.model

object DefaultCategories {
    val categories = listOf(
        "蔬菜🥬", "水果🍎", "肉类🥩", "海鲜🦞", "蛋奶制品🥛",
        "调料🧂", "米面粮油🍚", "零食🍪", "饮品🥤", "日用品🧴", "其他🛒"
    )
}

object DefaultUnits {
    val units = listOf(
        "个", "斤", "两", "公斤", "袋", "盒", "瓶", "包", "件", "条", "把", "颗", "打", "双", "对", "卷", "听"
        // Common units: piece, 0.5kg, 50g, kg, bag, box, bottle, pack, item, strip, handful, head/piece, dozen, pair, pair(alt), roll, can
    )
}

object DefaultPurchaseLocations {
    val locations = listOf(
        "🏪超市", "🥬菜市场", "🛒网购", "🏬商场", "🥐面包店", "🥩肉铺", "🐟海鲜市场", "🍓水果摊", "🏠家里有", "其他"
        // Supermarket, Wet Market, Online, Shopping Mall, Bakery, Butcher, Seafood Market, Fruit Stall, Have at Home, Other
    )
}
