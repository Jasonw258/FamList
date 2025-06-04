package com.example.famlistapp

import com.example.famlistapp.data.model.Family
import com.example.famlistapp.data.model.User
import java.util.UUID
import com.example.famlistapp.data.model.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// SupabaseClient模拟对象，用于与Supabase后端交互的占位符。
// TODO: 在实际应用中，这里会初始化真实的Supabase客户端 (例如: io.github.jan-tennert.supabase-kt).
object SupabaseClient {

    /**
     * 模拟在Supabase中创建新的家庭和用户。
     * 实际操作通常包括:
     * 1. 生成唯一的6位家庭码 (需检查冲突)。
     * 2. Creating a User object.
     * 3. Inserting the User into a "users" table (e.g., supabase.table("users").insert(user)).
     * 4. Creating a Family object with the new user as the first member.
     * 5. Inserting the Family into a "families" table (e.g., supabase.table("families").insert(family)).
     *
     * @param familyName The desired name for the family.
     * @param userNickname The nickname for the user creating the family.
     * @param userAvatar 用户头像信息占位符。
     * @return 返回包含创建的Family对象及其家庭码的Pair，如果失败则返回null和错误信息。
     */
    suspend fun createFamily(familyName: String, userNickname: String, userAvatar: String): Pair<Family?, String?> {
        // 模拟网络延迟
        // kotlinx.coroutines.delay(1000)

        // 模拟用户创建
        val newUser = User(id = UUID.randomUUID().toString(), nickname = userNickname, avatarUrl = userAvatar)

        // 模拟家庭创建
        val familyCode = (100000..999999).random().toString() // 生成模拟的6位家庭码
        val newFamily = Family(
            id = UUID.randomUUID().toString(),
            name = familyName,
            familyCode = familyCode,
            members = listOf(newUser) // 将创建者作为第一个成员
        )

        // 在真实场景中，需要处理Supabase操作可能发生的错误。
        // 此处为模拟，假设操作总是成功。
        println("模拟操作: 为用户 '$userNickname' 创建家庭 '$familyName'，家庭码为 '$familyCode'")
        return Pair(newFamily, familyCode)
    }

    /**
     * 模拟加入一个已存在的家庭。
     * 实际操作通常包括:
     * 1. 根据家庭码查询 "families" 表 (例如: supabase.table("families").select { filter { eq("family_code", familyCode) } }.singleOrNull())。
     * 2. If family exists:
     *    a. Creating a new User object.
     *    b. Inserting the User into the "users" table.
     *    c. Adding the new User to the family's member list.
     *    d. Updating the Family in the "families" table (e.g., supabase.table("families").update { filter { eq("id", family.id) } set { value("members", updatedMembersList) } }).
     *
     * @param familyCode The 6-digit code of the family to join.
     * @param userNickname The nickname for the user joining the family.
     * @param userAvatar 用户头像信息占位符。
     * @return 如果成功，返回更新后的Family对象；如果家庭码无效或发生错误，则返回null。
     */
    suspend fun joinFamily(familyCode: String, userNickname: String, userAvatar: String): Family? {
        // 模拟网络延迟
        // kotlinx.coroutines.delay(1000)

        // 模拟根据家庭码查找家庭
        if (familyCode == "123456" || familyCode.length == 6) { // 简化的模拟检查逻辑
            val joiningUser = User(id = UUID.randomUUID().toString(), nickname = userNickname, avatarUrl = userAvatar)
            // 模拟返回一个已存在的家庭对象
            val existingFamily = Family(
                id = UUID.randomUUID().toString(),
                name = "模拟的已存在家庭",
                familyCode = familyCode,
                members = listOf(
                    User(id = UUID.randomUUID().toString(), "原始成员", ""),
                    joiningUser // 添加新成员
                )
            )
            println("模拟操作: 用户 '$userNickname' 加入家庭码为 '$familyCode' 的家庭")
            return existingFamily
        } else {
            println("模拟操作: 未找到家庭码为 '$familyCode' 的家庭。")
            return null
        }
    }

    /**
     * 模拟根据ID获取家庭信息。
     * 实际操作通常包括:
     * 1. 根据familyId查询 "families" 表 (例如: supabase.table("families").select { filter { eq("id", familyId) } }.singleOrNull())。
     *
     * @param familyId The unique ID of the family.
     * @return The Family object if found, or null.
     */
    suspend fun getFamilyById(familyId: String): Family? {
        // Simulate network delay
        // kotlinx.coroutines.delay(500)

        // For simulation, return a dummy family if a UUID-like ID is provided
        if (runCatching { UUID.fromString(familyId) }.isSuccess) {
            return Family(
                id = familyId,
                name = "Simulated Family by ID",
                familyCode = "654321",
                members = listOf(User(id = UUID.randomUUID().toString(), "Member 1", ""))
            )
        }
        println("Simulated: Family with ID '$familyId' not found.")
        return null
    }

    // --- Shopping List Simulation ---

    private val _simulatedShoppingList = mutableListOf<ShoppingItem>(
        ShoppingItem(name = "牛奶 (simulated)", isBought = true, addedByUserId = "user1"),
        ShoppingItem(name = "鸡蛋 (simulated)", addedByUserId = "user2"),
        ShoppingItem(name = "面包 (simulated)", addedByUserId = "user1")
    )
    private val simulatedShoppingListFlow = MutableStateFlow(_simulatedShoppingList.toList())


    /**
     * Simulates getting a real-time flow of shopping list items for a family.
     * Normally, this would use Supabase Realtime to subscribe to changes in the "shopping_items" table
     * filtered by family_id. E.g., supabase.table("shopping_items").select { filter { eq("family_id", familyId) } }.asFlow().map { it.decodeList<ShoppingItem>() }
     *
     * @param familyId The ID of the family whose shopping list is to be fetched.
     * @return A Flow emitting the list of shopping items.
     */
    suspend fun getShoppingList(familyId: String): Flow<List<ShoppingItem>> {
        // Simulate network delay or initial fetch
        // kotlinx.coroutines.delay(500)
        println("Simulated: Getting shopping list for family '$familyId'")
        // For simulation, return a flow that emits the current list and can be updated by other functions.
        return simulatedShoppingListFlow
    }

    /**
     * Simulates adding a new item to the shopping list.
     * Normally, this would insert a new record into the "shopping_items" table.
     * E.g., supabase.table("shopping_items").insert(newItem).decodeAs<ShoppingItem>()
     *
     * @param familyId The ID of the family to add the item to.
     * @param itemName The name of the item.
     * @param userId The ID of the user adding the item.
     * @return The created ShoppingItem, or null if failed.
     */
    suspend fun addShoppingItem(familyId: String, itemName: String, userId: String): ShoppingItem? {
        // Simulate network delay
        // kotlinx.coroutines.delay(300)
        val newItem = ShoppingItem(
            name = itemName,
            addedByUserId = userId,
            // familyId = familyId // This would be part of the ShoppingItem model if stored in DB table
        )
        _simulatedShoppingList.add(0, newItem) // Add to top
        simulatedShoppingListFlow.value = _simulatedShoppingList.toList()
        println("Simulated: Added item '$itemName' to family '$familyId' by user '$userId'")
        return newItem
    }

    /**
     * Simulates updating an existing shopping item (e.g., toggling its 'isBought' status).
     * Normally, this would update a record in the "shopping_items" table.
     * E.g., supabase.table("shopping_items").update({ filter { eq("id", item.id) } }, item)
     *
     * @param familyId The ID of the family (for context, might not be needed if item.id is globally unique).
     * @param item The ShoppingItem with updated values.
     * @return True if successful, false otherwise.
     */
    suspend fun updateShoppingItem(familyId: String, item: ShoppingItem): Boolean {
        // Simulate network delay
        // kotlinx.coroutines.delay(200)
        val index = _simulatedShoppingList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            _simulatedShoppingList[index] = item
            simulatedShoppingListFlow.value = _simulatedShoppingList.toList()
            println("Simulated: Updated item '${item.name}' (isBought: ${item.isBought}) in family '$familyId'")
            return true
        }
        println("Simulated: Failed to update item '${item.name}', not found.")
        return false
    }

    /**
     * Simulates deleting a shopping item.
     * Normally, this would delete a record from the "shopping_items" table.
     * E.g., supabase.table("shopping_items").delete { filter { eq("id", itemId) } }
     *
     * @param familyId The ID of the family (for context).
     * @param itemId The ID of the item to delete.
     * @return True if successful, false otherwise.
     */
    suspend fun deleteShoppingItem(familyId: String, itemId: String): Boolean {
        // Simulate network delay
        // kotlinx.coroutines.delay(300)
        val removed = _simulatedShoppingList.removeIf { it.id == itemId }
        if (removed) {
            simulatedShoppingListFlow.value = _simulatedShoppingList.toList()
            println("Simulated: Deleted item with ID '$itemId' from family '$familyId'")
        } else {
            println("Simulated: Failed to delete item with ID '$itemId', not found.")
        }
        return removed
    }

    // --- User Presence Simulation ---

    /**
     * Simulates fetching family members with their online status.
     * Normally, this would involve:
     * 1. Fetching all users belonging to the familyId.
     * 2. Subscribing to Supabase Realtime "presence" channels for online status
     *    or periodically fetching last_seen timestamps.
     *
     * @param familyId The ID of the family.
     * @return A Flow emitting a list of User objects with updated presence info.
     */
    fun getFamilyMembersWithStatus(familyId: String): Flow<List<User>> {
        // Simulate network delay
        // kotlinx.coroutines.delay(700)
        println("Simulated: Getting family members with status for family '$familyId'")

        // Static list for simulation
        val now = System.currentTimeMillis()
        val simulatedUsers = listOf(
            User(id = "user1_dad", nickname = "爸爸", avatarUrl = "", isOnline = true, lastSeenTimestamp = now),
            User(id = "user2_mom", nickname = "妈妈", avatarUrl = "", isOnline = false, lastSeenTimestamp = now - 60 * 60 * 1000), // 1 hour ago
            User(id = "user3_kid", nickname = "小明", avatarUrl = "", isOnline = false, lastSeenTimestamp = now - 24 * 60 * 60 * 1000 * 2) // 2 days ago
        )
        // In a real app, this flow would be powered by Supabase Realtime or periodic polling.
        return MutableStateFlow(simulatedUsers) // Emit once for simulation
    }

    /**
     * Simulates sending a push notification for an urgent item.
     * In a real application, this would typically involve:
     * 1. Calling a Supabase Edge Function.
     * 2. The Edge Function would then use the Supabase Admin SDK to:
     *    a. Query for FCM tokens of users in the specified family (excluding the sender, if needed).
     *    b. Send messages to those tokens via Firebase Cloud Messaging (FCM) Admin SDK.
     *
     * @param familyId The ID of the family to notify.
     * @param itemName The name of the urgent item.
     * @param userName The name of the user who added the item.
     */
    suspend fun sendUrgentItemNotification(familyId: String, itemName: String, userName: String) {
        // Simulate network delay for the call to the edge function
        // kotlinx.coroutines.delay(500)
        println("SupabaseClient (Simulated): Attempting to send push notification for URGENT item.")
        println("  Family ID: $familyId")
        println("  Item Name: $itemName")
        println("  Added By: $userName")
        println("  Action: In a real app, would call a Supabase Edge Function here to trigger FCM send.")
        // Example of what the Edge Function might do (logged here for simulation):
        // 1. Fetch FCM tokens for members of 'familyId'.
        // 2. Construct notification payload: { title: "紧急物品!", body: "$userName 添加了急需物品: $itemName" }
        // 3. Send to FCM.
    }
}
