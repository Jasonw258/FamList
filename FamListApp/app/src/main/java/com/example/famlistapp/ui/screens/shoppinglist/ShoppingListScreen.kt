package com.example.famlistapp.ui.screens.shoppinglist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.famlistapp.R // Import R class
import com.example.famlistapp.data.model.ShoppingItem
import com.example.famlistapp.data.model.ShoppingItemPriority
import com.example.famlistapp.data.model.User
import com.example.famlistapp.data.model.LocalizedData
import com.example.famlistapp.ui.theme.FamListAppTheme
import com.example.famlistapp.ui.screens.shoppinglist.viewmodel.UserShoppingList
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    groupedItems: List<UserShoppingList>,
    onAddItem: (name: String, priority: String, category: String?, quantity: String?, unit: String?, location: String?) -> Unit,
    onToggleItemBought: (ShoppingItem) -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit,
    onVoiceInputClicked: () -> Unit,
    onClearBoughtItemsClicked: () -> Unit,
    onViewHistoryClicked: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf(TextFieldValue("")) }
    var newItemPriority by remember { mutableStateOf(ShoppingItemPriority.NORMAL) }
    var newItemCategory by remember { mutableStateOf<String?>(null) }
    var newItemQuantity by remember { mutableStateOf(TextFieldValue("")) }
    var newItemUnit by remember { mutableStateOf<String?>(null) }
    var newItemLocation by remember { mutableStateOf<String?>(null) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.shopping_list_title), style = MaterialTheme.typography.headlineMedium)
                Row {
                    TextButton(onClick = { /* TODO: Show recommendations */ }) {
                        Text(stringResource(R.string.seasonal_recommendations), style = MaterialTheme.typography.bodySmall)
                    }
                    var showFilterDialog by remember { mutableStateOf(false) }
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Filled.FilterList, contentDescription = stringResource(R.string.filter_items_title))
                    }
                    if (showFilterDialog) {
                        val availableUsers = groupedItems.map { it.user }
                        val selectedCategories = remember { mutableStateSetOf<String>() }
                        val selectedUserIds = remember { mutableStateSetOf<String>() }
                        val selectedPriorities = remember { mutableStateSetOf<String>() }
                        FilterDialog(
                            onDismissRequest = { showFilterDialog = false },
                            availableUsers = availableUsers,
                            availableCategories = LocalizedData.DefaultCategories.categories,
                            availablePriorities = ShoppingItemPriority.values().toList(),
                            selectedCategories = selectedCategories,
                            onCategorySelected = { category, isSelected -> if (isSelected) selectedCategories.add(category) else selectedCategories.remove(category) },
                            selectedUserIds = selectedUserIds,
                            onUserSelected = { userId, isSelected -> if (isSelected) selectedUserIds.add(userId) else selectedUserIds.remove(userId) },
                            selectedPriorities = selectedPriorities,
                            onPrioritySelected = { priority, isSelected -> if (isSelected) selectedPriorities.add(priority.displayName) else selectedPriorities.remove(priority.displayName) },
                            onApplyFilters = {
                                println("Applying filters: Cat=${selectedCategories.joinToString()}, Users=${selectedUserIds.joinToString()}, Prio=${selectedPriorities.joinToString()}")
                                showFilterDialog = false
                            }
                        )
                    }
                }
            }

            if (groupedItems.isEmpty()) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("清单是空的，或大家都没啥要买的！", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            newItemName = TextFieldValue("")
                            newItemPriority = ShoppingItemPriority.NORMAL
                            newItemCategory = null
                            newItemQuantity = TextFieldValue("")
                            newItemUnit = null
                            newItemLocation = null
                            showDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(R.string.i_want_to_buy))
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    groupedItems.forEach { userList ->
                        item { UserGroupHeader(userList) }
                        items(userList.items, key = { it.id }) { item ->
                            ShoppingListItemRow(item = item, onToggleBought = { onToggleItemBought(item) }, onDelete = { onDeleteItem(item) })
                        }
                        item { Divider(modifier = Modifier.padding(top = 8.dp)) }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                if (groupedItems.isNotEmpty()) {
                    Button(
                        onClick = {
                            newItemName = TextFieldValue("")
                            newItemPriority = ShoppingItemPriority.NORMAL
                            newItemCategory = null
                            newItemQuantity = TextFieldValue("")
                            newItemUnit = null
                            newItemLocation = null
                            showDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(R.string.i_want_to_buy))
                    }
                }
                OutlinedButton(
                    onClick = onVoiceInputClicked,
                    modifier = Modifier.weight(if (groupedItems.isEmpty()) 1f else 0.6f)
                ) {
                    Icon(Icons.Filled.Mic, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.voice_input_button))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)).padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onViewHistoryClicked) { Text(stringResource(R.string.view_history_button)) }
                TextButton(onClick = onClearBoughtItemsClicked) { Text(stringResource(R.string.clear_bought_items_button)) }
            }

            if (showDialog) {
                AddItemDialog(
                    onDismissRequest = { showDialog = false },
                    onAddItem = { name, priority, category, quantity, unit, location ->
                        onAddItem(name, priority, category, quantity, unit, location)
                        // Resetting states is now handled by the onClick of the "Add Item" button
                        showDialog = false
                    },
                    newItemName = newItemName,
                    onNewItemNameChange = { newItemName = it },
                    selectedPriority = newItemPriority,
                    onPrioritySelected = { newItemPriority = it },
                    selectedCategory = newItemCategory,
                    onCategorySelected = { newItemCategory = it },
                    quantity = newItemQuantity,
                    onQuantityChange = { newItemQuantity = it },
                    selectedUnit = newItemUnit,
                    onUnitSelected = { newItemUnit = it },
                    selectedLocation = newItemLocation,
                    onLocationSelected = { newItemLocation = it }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterDialog(
    onDismissRequest: () -> Unit,
    availableUsers: List<User>,
    availableCategories: List<String>,
    availablePriorities: List<ShoppingItemPriority>,
    selectedCategories: Set<String>,
    onCategorySelected: (String, Boolean) -> Unit,
    selectedUserIds: Set<String>,
    onUserSelected: (String, Boolean) -> Unit,
    selectedPriorities: Set<String>,
    onPrioritySelected: (ShoppingItemPriority, Boolean) -> Unit,
    onApplyFilters: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.filter_items_title)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(stringResource(R.string.filter_by_category), style = MaterialTheme.typography.titleSmall)
                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    availableCategories.forEach { category ->
                        FilterChip(selected = selectedCategories.contains(category), onClick = { onCategorySelected(category, !selectedCategories.contains(category)) }, label = { Text(category) })
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.filter_by_member), style = MaterialTheme.typography.titleSmall)
                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    availableUsers.forEach { user ->
                        FilterChip(selected = selectedUserIds.contains(user.id), onClick = { onUserSelected(user.id, !selectedUserIds.contains(user.id)) }, label = { Text(user.nickname) })
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.filter_by_priority), style = MaterialTheme.typography.titleSmall)
                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    availablePriorities.forEach { priority ->
                        FilterChip(selected = selectedPriorities.contains(priority.displayName), onClick = { onPrioritySelected(priority, !selectedPriorities.contains(priority.displayName)) }, label = { Text(priority.displayName) })
                    }
                }
            }
        },
        confirmButton = { Button(onClick = onApplyFilters) { Text(stringResource(R.string.apply_filters_button)) } },
        dismissButton = { Button(onClick = onDismissRequest) { Text(stringResource(R.string.cancel_button)) } }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddItemDialog(
    onDismissRequest: () -> Unit,
    onAddItem: (name: String, priority: String, category: String?, quantity: String?, unit: String?, location: String?) -> Unit,
    newItemName: TextFieldValue,
    onNewItemNameChange: (TextFieldValue) -> Unit,
    selectedPriority: ShoppingItemPriority,
    onPrioritySelected: (ShoppingItemPriority) -> Unit,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    quantity: TextFieldValue,
    onQuantityChange: (TextFieldValue) -> Unit,
    selectedUnit: String?,
    onUnitSelected: (String?) -> Unit,
    selectedLocation: String?,
    onLocationSelected: (String?) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(id = R.string.add_item_dialog_title)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = newItemName, onValueChange = onNewItemNameChange, label = { Text(stringResource(id = R.string.item_name_label)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(value = quantity,onValueChange = onQuantityChange,label = { Text("数量") },modifier = Modifier.weight(1f),keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),singleLine = true)
                    Spacer(modifier = Modifier.width(8.dp))
                    var unitExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(expanded = unitExpanded, onExpandedChange = { unitExpanded = !unitExpanded }, modifier = Modifier.weight(1f)) {
                        OutlinedTextField(value = selectedUnit ?: "单位", onValueChange = {}, readOnly = true, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded) }, modifier = Modifier.menuAnchor())
                        ExposedDropdownMenu(expanded = unitExpanded, onDismissRequest = { unitExpanded = false }) {
                            LocalizedData.DefaultUnits.units.forEach { unit -> DropdownMenuItem(text = { Text(unit) }, onClick = { onUnitSelected(unit); unitExpanded = false }) }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(id = R.string.priority_label), style = MaterialTheme.typography.labelMedium)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    ShoppingItemPriority.values().forEach { priority ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onPrioritySelected(priority) }) {
                            RadioButton(selected = (priority == selectedPriority), onClick = { onPrioritySelected(priority) })
                            Text(text = priority.displayName.split("").first(), modifier = Modifier.padding(start = 2.dp, end = 8.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.category_label), style = MaterialTheme.typography.labelMedium)
                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    LocalizedData.DefaultCategories.categories.forEach { category ->
                        FilterChip(selected = (category == selectedCategory), onClick = { onCategorySelected(if (category == selectedCategory) null else category) }, label = { Text(category) })
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("购买地点:", style = MaterialTheme.typography.labelMedium)
                var locationExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = locationExpanded, onExpandedChange = { locationExpanded = !locationExpanded }) {
                    OutlinedTextField(value = selectedLocation ?: "选择地点", onValueChange = {}, readOnly = true, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                    ExposedDropdownMenu(expanded = locationExpanded, onDismissRequest = { locationExpanded = false }) {
                        LocalizedData.DefaultPurchaseLocations.locations.forEach { location -> DropdownMenuItem(text = { Text(location) }, onClick = { onLocationSelected(location); locationExpanded = false }) }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (newItemName.text.isNotBlank()) {
                    onAddItem(newItemName.text, selectedPriority.displayName, selectedCategory, quantity.text.ifBlank { null }, selectedUnit, selectedLocation)
                }
            }) { Text(stringResource(id = R.string.add_button)) }
        },
        dismissButton = { Button(onClick = onDismissRequest) { Text(stringResource(id = R.string.cancel_button)) } }
    )
}

@Composable
fun UserGroupHeader(userList: UserShoppingList) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = "${userList.user.nickname} 要买的 (${userList.items.size}样)", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = formatUserStatus(userList.user), style = MaterialTheme.typography.labelMedium, color = if (userList.user.isOnline) MaterialTheme.colorScheme.primary else Color.Gray)
    }
}

@Composable
fun formatUserStatus(user: User): String {
    if (user.isOnline) return "[在线]"
    if (user.lastSeenTimestamp == 0L) return "[离线]"
    val now = System.currentTimeMillis()
    val diff = now - user.lastSeenTimestamp
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        minutes < 1 -> "[刚刚]"
        minutes < 60 -> "[${minutes}分钟前]"
        hours < 24 -> "[${hours}小时前]"
        days == 1L -> "[昨天]"
        days < 7 -> "[${days}天前]"
        else -> "[较久前]"
    }
}

@Composable
fun ShoppingListItemRow(item: ShoppingItem, onToggleBought: () -> Unit, onDelete: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Checkbox(checked = item.isBought, onCheckedChange = { onToggleBought() })
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = item.priority.split("").firstOrNull() ?: "", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(end = 8.dp))
                    Text(
                        text = item.name,
                        style = if (item.isBought) MaterialTheme.typography.bodyLarge.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough, color = Color.Gray)
                        else MaterialTheme.typography.bodyLarge,
                        maxLines = 2, // Allow wrapping
                        overflow = TextOverflow.Ellipsis
                    )
                    val quantityUnitString = buildString {
                        item.quantity?.let { append(it) }
                        item.unit?.let { if (item.quantity != null) append(" "); append(it) }
                    }
                    if (quantityUnitString.isNotBlank()) {
                        Text(text = quantityUnitString, style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
                    }
                }
                val categoryLocationInfo = mutableListOf<String>()
                item.category?.let { categoryLocationInfo.add(it) }
                item.purchaseLocation?.let { categoryLocationInfo.add(it) }
                if (categoryLocationInfo.isNotEmpty()) {
                    Text(text = categoryLocationInfo.joinToString(" @ "), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline, modifier = Modifier.padding(start = 32.dp))
                }
            }
        }
        IconButton(onClick = onDelete) { Icon(Icons.Filled.Delete, contentDescription = "删除物品", tint = MaterialTheme.colorScheme.error) }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListScreenPreview() {
    FamListAppTheme {
        val now = System.currentTimeMillis()
        val user1 = User(id = "user1", nickname = "爸爸", avatarUrl = "", isOnline = true, lastSeenTimestamp = now)
        val user2 = User(id = "user2", nickname = "妈妈", avatarUrl = "", isOnline = false, lastSeenTimestamp = now - (2 * 60 * 60 * 1000))
        val sampleGroupedItems = remember {
            mutableStateListOf(
                UserShoppingList(user = user1, items = listOf(
                    ShoppingItem(id="1", name = "牛奶", addedByUserId = "user1", addedByUserName = "爸爸", priority = ShoppingItemPriority.NORMAL.displayName, category = "蛋奶制品🥛", quantity = "2", unit = "盒", purchaseLocation = "🏪超市"),
                    ShoppingItem(id="2", name = "报纸", addedByUserId = "user1", addedByUserName = "爸爸", isBought = true, priority = ShoppingItemPriority.LOW.displayName, purchaseLocation = "楼下便利店", quantity = "1", unit = "份")
                )),
                UserShoppingList(user = user2, items = listOf(
                    ShoppingItem(id="3", name = "鸡蛋", addedByUserId = "user2", addedByUserName = "妈妈", priority = ShoppingItemPriority.URGENT.displayName, category = "蛋奶制品🥛", quantity = "1", unit = "打", purchaseLocation = "🥬菜市场"),
                    ShoppingItem(id="4", name = "超长名称的酱油用来测试省略号是否正常显示以及是否会影响布局", addedByUserId = "user2", addedByUserName = "妈妈", category = "调料🧂", purchaseLocation = "🛒网购", quantity = "1", unit = "瓶")
                ))
            )
        }
        ShoppingListScreen(
            groupedItems = sampleGroupedItems,
            onAddItem = { name, priority, category, quantity, unit, location ->
                val userList = sampleGroupedItems.find { it.user.id == "user1" }
                if (userList != null) {
                    val newItem = ShoppingItem(name = name, addedByUserId = "user1", addedByUserName = "爸爸", priority = priority, category = category, quantity = quantity, unit = unit, purchaseLocation = location, boughtTimestamp = if(name.contains("bought")) System.currentTimeMillis() else null)
                    val updatedItems = userList.items + newItem
                    sampleGroupedItems[sampleGroupedItems.indexOf(userList)] = userList.copy(items = updatedItems)
                }
            },
            onToggleItemBought = { /* ... */ },
            onDeleteItem = { /* ... */ },
            onVoiceInputClicked = { println("Voice input clicked (Preview)") },
            onClearBoughtItemsClicked = { println("Clear bought items clicked (Preview)") },
            onViewHistoryClicked = { println("View history clicked (Preview)") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListScreenEmptyPreview() {
    FamListAppTheme {
        ShoppingListScreen(
            groupedItems = emptyList(),
            onAddItem = { _, _, _, _, _, _ -> },
            onToggleItemBought = {},
            onDeleteItem = {},
            onVoiceInputClicked = {},
            onClearBoughtItemsClicked = {},
            onViewHistoryClicked = {}
        )
    }
}
// Ensure R class is imported: import com.example.famlistapp.R
// Ensure stringResource is used for all user-facing strings.
// For example, in AddItemDialog:
// label = { Text("数量") } should be label = { Text(stringResource(R.string.quantity_label)) }
// value = selectedUnit ?: "单位" should be value = selectedUnit ?: stringResource(R.string.unit_placeholder)
// Text("购买地点:") should be Text(stringResource(R.string.purchase_location_label))
// value = selectedLocation ?: "选择地点" should be value = selectedLocation ?: stringResource(R.string.select_location_placeholder)
// Similar changes for UserGroupHeader, ShoppingListItemRow, etc.
// This step focuses on structure; full string resource integration is a larger task.
