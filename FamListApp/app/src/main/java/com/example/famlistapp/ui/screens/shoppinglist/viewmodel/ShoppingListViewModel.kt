package com.example.famlistapp.ui.screens.shoppinglist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// import com.example.famlistapp.SupabaseClient // No longer directly used by ViewModel
import com.example.famlistapp.data.ShoppingRepository // Import Repository
import com.example.famlistapp.data.model.ShoppingItem
import com.example.famlistapp.data.model.User // Import User model
import com.example.famlistapp.services.VoiceRecognitionService // Import VoiceRecognitionService
import com.example.famlistapp.SupabaseClient // Needed for getFamilyMembersWithStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// 用户购物清单数据类，包含用户信息和该用户的购物项列表
data class UserShoppingList(
    val user: User, // 用户对象
    val items: List<ShoppingItem> // 该用户的购物项列表
)

data class ShoppingListUiState(
    val groupedItems: List<UserShoppingList> = emptyList(), // 按用户分组的购物项列表
    val isLoading: Boolean = false, // 是否正在加载数据
    val error: String? = null, // 错误信息
    val currentFamilyId: String? = "dummyFamilyId", // 当前家庭组ID，应从实际逻辑获取
    // Filter states / 筛选状态
    val selectedCategories: Set<String> = emptySet(), // 已选分类
    val selectedUserIds: Set<String> = emptySet(), // 已选用户ID
    val selectedPriorities: Set<String> = emptySet(), // 已选优先级
    val allUsersInFamily: List<User> = emptyList() // 当前家庭的所有用户列表（用于筛选器选项）
)

// ViewModel通常通过Hilt等依赖注入框架获取实例
class ShoppingListViewModel(
    private val repository: ShoppingRepository, // 购物仓库，用于数据操作
    private val voiceRecognitionService: VoiceRecognitionService // 语音识别服务
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState()) // UI状态流
    val uiState: StateFlow<ShoppingListUiState> = _uiState

    // 当前家庭ID，应从安全存储或导航参数获取
    private val currentFamilyId: String?
        get() = _uiState.value.currentFamilyId


    init {
        // ViewModel初始化时加载数据
        // 注意: currentFamilyId此时可能为null。实际应用中，应在familyId确定后再调用loadData。
        // setFamilyIdAndReload是更稳妥的初次加载触发方式。
        currentFamilyId?.let {
            if (it != "dummyFamilyId" && it != "fallbackFamilyId") { // 避免使用占位ID加载
                 loadData()
            }
        }
    }

    // 加载购物清单和家庭成员数据，并进行分组和筛选
    private fun loadData() {
        val famId = currentFamilyId
        if (famId == null || famId.isBlank() || famId == "fallbackFamilyId" || famId == "dummyFamilyId") {
            _uiState.value = _uiState.value.copy(error = "家庭ID未设置，无法加载清单。", isLoading = false, groupedItems = emptyList())
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, error = null) // 开始加载，清除旧错误

        viewModelScope.launch { // 启动协程处理数据加载
            // 使用combine操作符合并来自仓库的购物清单流和来自SupabaseClient的家庭成员状态流
            combine(
                repository.getShoppingList(famId), // 获取购物清单
                SupabaseClient.getFamilyMembersWithStatus(famId) // 获取家庭成员及其在线状态
            ) { shoppingItems, familyMembers ->
                // 更新UI状态中的家庭成员列表（用于筛选器选项）
                _uiState.value = _uiState.value.copy(allUsersInFamily = familyMembers)

                // 应用当前激活的筛选条件
                val filteredShoppingItems = shoppingItems.filter { item ->
                    val categoryMatch = _uiState.value.selectedCategories.isEmpty() ||
                                        item.category in _uiState.value.selectedCategories
                    val userMatch = _uiState.value.selectedUserIds.isEmpty() ||
                                    item.addedByUserId in _uiState.value.selectedUserIds
                    val priorityMatch = _uiState.value.selectedPriorities.isEmpty() ||
                                        item.priority in _uiState.value.selectedPriorities
                    categoryMatch && userMatch && priorityMatch // 所有条件都必须匹配
                }

                // 更新筛选后购物项中的 'addedByUserName' 字段，用于UI显示
                val updatedShoppingItems = filteredShoppingItems.map { item ->
                    val userName = familyMembers.find { it.id == item.addedByUserId }?.nickname ?: item.addedByUserName
                    item.copy(addedByUserName = userName)
                }

                // 将处理后的购物项按用户分组
                val finalGrouped = familyMembers.map { user ->
                    val userItems = updatedShoppingItems.filter { it.addedByUserId == user.id }
                    UserShoppingList(user, userItems)
                }.filter { userList -> // 决定是否在UI上显示该用户组
                    if (_uiState.value.selectedUserIds.isNotEmpty()) { // 如果有用户筛选
                        userList.user.id in _uiState.value.selectedUserIds && userList.items.isNotEmpty() // 用户被选中且有物品
                    } else { // 没有用户筛选
                        userList.items.isNotEmpty() || userList.user.isOnline // 用户有物品或在线时显示
                    }
                }
                // 注意: 如果特定用户被选中进行筛选，即使他们筛选后没有物品，也可能需要显示该用户组。
                // 当前逻辑是：如果用户被筛选，但其物品被其他条件滤除，则该用户组不显示。可能需要调整。

                // 更新UI状态，显示最终分组和筛选后的购物清单
                _uiState.value = _uiState.value.copy(groupedItems = finalGrouped, isLoading = false)

            }.catch { e -> // 统一处理流中的异常
                _uiState.value = _uiState.value.copy(error = "加载数据时出错: ${e.message}", isLoading = false)
            }.collect() // 开始收集流数据
        }
    }

    // 添加购物项
    fun addItem(
        itemName: String,
        priority: String,
        category: String?,
        quantity: String?,
        unit: String?,
        location: String?
    ) {
        val famId = currentFamilyId
        if (itemName.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "物品名称不能为空。")
            return
        }
        if (famId == null || famId.isBlank() || famId == "fallbackFamilyId" || famId == "dummyFamilyId") {
            _uiState.value = _uiState.value.copy(error = "无法添加物品：家庭ID未设置。")
            return
        }
        // 模拟当前用户ID，实际应从用户认证状态获取
        val userId = _uiState.value.allUsersInFamily.firstOrNull()?.id ?: "user_unknown"

        viewModelScope.launch {
            try {
                repository.addShoppingItem(famId, itemName, userId, priority, category, quantity, unit, location)
                _uiState.value = _uiState.value.copy(error = null) // 操作成功，清除错误信息
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "添加物品失败: ${e.message}")
            }
        }
    }

    // 切换物品的“已购买”状态
    fun toggleItemBought(item: ShoppingItem) {
        val famId = currentFamilyId
         if (famId == null || famId.isBlank() || famId == "fallbackFamilyId" || famId == "dummyFamilyId") {
            _uiState.value = _uiState.value.copy(error = "无法更新物品状态：家庭ID未设置。")
            return
        }
        viewModelScope.launch {
            val newBoughtStatus = !item.isBought
            val updatedItem = item.copy(
                isBought = newBoughtStatus,
                boughtTimestamp = if (newBoughtStatus) System.currentTimeMillis() else null
            )
            try {
                repository.updateShoppingItem(famId, updatedItem)
                _uiState.value = _uiState.value.copy(error = null)
            } catch (e: Exception) {
                 _uiState.value = _uiState.value.copy(error = "更新物品状态失败: ${e.message}")
            }
        }
    }

    // 删除购物项
    fun deleteItem(itemId: String) {
        val famId = currentFamilyId
        if (famId == null || famId.isBlank() || famId == "fallbackFamilyId" || famId == "dummyFamilyId") {
            _uiState.value = _uiState.value.copy(error = "无法删除物品：家庭ID未设置。")
            return
        }
        viewModelScope.launch {
            try {
                repository.deleteShoppingItem(famId, itemId)
                _uiState.value = _uiState.value.copy(error = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "删除物品失败: ${e.message}")
            }
        }
    }

    // 当家庭组ID变化时调用此方法重新加载数据
    fun setFamilyIdAndReload(newFamilyId: String) {
        _uiState.value = _uiState.value.copy(currentFamilyId = newFamilyId, error = null, isLoading = true)
        loadData()
    }

    // 清理已购买的物品（概念性操作）
    fun onClearBoughtItemsClicked() {
        val famId = currentFamilyId
        if (famId == null || famId.isBlank() || famId == "fallbackFamilyId" || famId == "dummyFamilyId") {
            _uiState.value = _uiState.value.copy(error = "无法清理物品：家庭ID未设置。")
            return
        }
        viewModelScope.launch {
            try {
                repository.clearBoughtItemsFromActiveList(famId)
                _uiState.value = _uiState.value.copy(error = null)
                // 注意: 实际效果取决于clearBoughtItemsFromActiveList的实现方式
                // 可能需要手动调用loadData()来刷新UI，如果仓库操作不直接触发UI更新的话
                println("ShoppingListViewModel: 清理已购买物品操作已触发。(仓库中的具体实现决定UI是否自动更新)")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "清理已购买物品失败: ${e.message}")
            }
        }
    }

    // 开始语音输入
    fun startVoiceInput() {
        val famId = currentFamilyId
        if (famId == null || famId.isBlank() || famId == "fallbackFamilyId" || famId == "dummyFamilyId") {
            _uiState.value = _uiState.value.copy(error = "无法使用语音输入：家庭ID未设置。")
            return
        }

        voiceRecognitionService.startListening { recognizedText ->
            if (recognizedText.isNotBlank()) {
                // 语音输入通常不包含详细信息，使用默认值添加
                addItem(
                    recognizedText,
                    com.example.famlistapp.data.model.ShoppingItemPriority.NORMAL.displayName,
                    null, null, null, null
                )
            } else {
                // _uiState.value = _uiState.value.copy(error = "语音识别失败或无输入。") // 可选：提示用户
            }
        }
    }

    // --- Filter Update Functions / 筛选条件更新方法 ---
    fun updateCategoryFilter(category: String, isSelected: Boolean) {
        val currentFilters = _uiState.value.selectedCategories.toMutableSet()
        if (isSelected) currentFilters.add(category) else currentFilters.remove(category)
        _uiState.value = _uiState.value.copy(selectedCategories = currentFilters)
        loadData() // 重新加载数据以应用筛选
    }

    fun updateUserIdFilter(userId: String, isSelected: Boolean) {
        val currentFilters = _uiState.value.selectedUserIds.toMutableSet()
        if (isSelected) currentFilters.add(userId) else currentFilters.remove(userId)
        _uiState.value = _uiState.value.copy(selectedUserIds = currentFilters)
        loadData()
    }

    fun updatePriorityFilter(priorityDisplayName: String, isSelected: Boolean) {
        val currentFilters = _uiState.value.selectedPriorities.toMutableSet()
        if (isSelected) currentFilters.add(priorityDisplayName) else currentFilters.remove(priorityDisplayName)
        _uiState.value = _uiState.value.copy(selectedPriorities = currentFilters)
        loadData()
    }

    fun clearAllFilters() {
        _uiState.value = _uiState.value.copy(
            selectedCategories = emptySet(),
            selectedUserIds = emptySet(),
            selectedPriorities = emptySet()
        )
        loadData()
    }
}
