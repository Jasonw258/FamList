package com.example.famlistapp.data

import com.example.famlistapp.SupabaseClient
import com.example.famlistapp.data.local.ShoppingItemDao
import com.example.famlistapp.data.model.ShoppingItem
import com.example.famlistapp.data.model.ShoppingItemPriority // Import priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope

/**
 * Repository for managing ShoppingList data.
 * It acts as a single source of truth for shopping data, handling
 * local caching (via DAO) and remote data fetching (via SupabaseClient).
 *
 * For simulation, conflict resolution and offline queueing are conceptual.
 */
class ShoppingRepository(
    private val shoppingItemDao: ShoppingItemDao, // Simulated DAO
    private val supabaseClient: SupabaseClient     // Simulated Supabase client
) {

    /**
     * Gets the shopping list.
     * 1. Emits cached data from DAO.
     * 2. Fetches fresh data from Supabase in the background.
     * 3. Updates DAO with fresh data. The DAO's Flow will then emit the new list.
     *
     * @param familyId The ID of the family whose list is being fetched.
     * @return A Flow of the shopping list.
     */
    fun getShoppingList(familyId: String): Flow<List<ShoppingItem>> {
        return shoppingItemDao.getAllItems()
            .onStart {
                // This block is executed when the Flow is first collected.
                // Launch a separate coroutine to fetch from network and update cache.
                // This allows the cached data to be emitted immediately while network fetch happens.
                coroutineScope { // Use coroutineScope to manage lifecycle of this child coroutine
                    launch {
                        try {
                            println("Repository: Fetching remote list for family $familyId")
                            // Fetch from remote (Supabase)
                            // In a real scenario, SupabaseClient.getShoppingList might return a Flow itself
                            // or a simple List. Here it's a Flow, so we take the first emission for snapshot.
                            val remoteItems = supabaseClient.getShoppingList(familyId).firstOrNull()
                            if (remoteItems != null) {
                                println("Repository: Remote list fetched, ${remoteItems.size} items. Updating cache.")
                                // For simplicity, clear local cache and insert all.
                                // A more sophisticated approach would involve merging/diffing.
                                shoppingItemDao.clearAll() // Or selective updates based on timestamps/IDs
                                shoppingItemDao.insertAll(remoteItems)
                            } else {
                                println("Repository: No remote items fetched or error occurred.")
                            }
                        } catch (e: Exception) {
                            // Handle network errors, e.g., log them.
                            // The DAO will continue to provide cached data if available.
                            println("Repository: Error fetching remote list: ${e.message}")
                            // Optionally, emit an error state or use a more robust error handling mechanism.
                        }
                    }
                }
            }
            .catch { e ->
                // This catches errors from the DAO flow itself, though less likely with Room.
                println("Repository: Error in DAO flow: ${e.message}")
                emit(emptyList()) // Emit empty list or handle error as appropriate
            }
    }

    /**
     * Adds an item to the shopping list.
     * 1. Adds to local DAO immediately (optimistic update).
     * 2. Tries to add to Supabase.
     * 3. If Supabase add fails, conceptual: roll back local change or mark for sync.
     *
     * @param familyId The family ID.
     * @param itemName The name of the item.
     * @param userId The user ID of the adder.
     * @param priority The priority of the item.
     * @param category The category of the item.
     * @param quantity The quantity of the item.
     * @param unit The unit for the quantity.
     * @param purchaseLocation The intended purchase location.
     */
    suspend fun addShoppingItem(
        familyId: String,
        itemName: String,
        userId: String,
        priority: String = ShoppingItemPriority.NORMAL.displayName,
        category: String? = null,
        quantity: String? = null, // New field
        unit: String? = null,     // New field
        purchaseLocation: String? = null // New field
    ) {
        val tempId = "temp_${System.currentTimeMillis()}" // Temporary ID for local insertion
        // Use the provided priority, or default if not matching enum (though ViewModel should ensure valid priority)
        val itemPriority = ShoppingItemPriority.values().find { it.displayName == priority } ?: ShoppingItemPriority.NORMAL
        val newItem = ShoppingItem(
            id = tempId,
            name = itemName,
            addedByUserId = userId,
            isBought = false,
            priority = itemPriority.displayName,
            category = category,
            quantity = quantity,
            unit = unit,
            purchaseLocation = purchaseLocation,
            // addedByUserName will be filled by ViewModel/UI or another mechanism if needed immediately
            // For now, repository focuses on core data.
        )

        // 1. Optimistic local update
        shoppingItemDao.insert(newItem)
        println("Repository: Optimistically added '$itemName' (Prio: ${newItem.priority}, Cat: ${newItem.category}, Qty: ${newItem.quantity} ${newItem.unit}, Loc: ${newItem.purchaseLocation}) to local cache with temp ID $tempId.")

        try {
            // 2. Attempt to sync with remote
            // SupabaseClient.addShoppingItem is still simulated to take only name and userId.
            // In a real app, it would take the full ShoppingItem object or all relevant fields.
            val remoteItem = supabaseClient.addShoppingItem(familyId, newItem.name, newItem.addedByUserId)

            if (remoteItem != null) {
                // Sync successful: update local item with actual ID from remote
                // Ensure all locally set fields (priority, category, quantity, unit, location) are preserved.
                val finalItem = remoteItem.copy(
                    priority = newItem.priority,
                    category = newItem.category,
                    quantity = newItem.quantity,
                    unit = newItem.unit,
                    purchaseLocation = newItem.purchaseLocation,
                    addedByUserName = newItem.addedByUserName // Keep this if it was set, though ideally server sets it
                )
                shoppingItemDao.delete(newItem) // Delete item with temp ID
                shoppingItemDao.insert(finalItem) // Insert item with actual remote ID
                println("Repository: Added '$itemName' to remote. Local cache updated with remote ID ${finalItem.id} and all local fields.")

                // 3. Trigger notification if urgent
                if (itemPriority == ShoppingItemPriority.URGENT) {
                    // Ideally, get the user's name from a reliable source (e.g. user profile)
                    // For now, we might not have the user's name readily available here.
                    // The SupabaseClient function will just log it.
                    // A better approach: ViewModel provides userName or repository fetches it.
                    // Let's assume a placeholder name or that SupabaseClient handles it.
                    val userName = newItem.addedByUserName // This might be "Unknown User" if not set prior
                    supabaseClient.sendUrgentItemNotification(familyId, finalItem.name, userName)
                }
            } else {
                // Remote add failed.
                println("Repository: Failed to add '$itemName' to remote. Local item with temp ID $tempId remains (needs sync).")
                // Conceptual: Queue for later sync. If urgent, notification might be queued too or sent upon successful sync.
            }
        } catch (e: Exception) {
            println("Repository: Network error adding '$itemName': ${e.message}. Local item with temp ID $tempId needs sync.")
            // Conceptual: Queue for later sync.
        }
    }

    /**
     * Updates an item (e.g., toggles 'isBought').
     * 1. Updates local DAO immediately.
     * 2. Tries to update in Supabase.
     * 3. If Supabase update fails, conceptual: roll back or mark for sync.
     *
     * @param familyId The family ID.
     * @param item The item to update.
     */
    suspend fun updateShoppingItem(familyId: String, item: ShoppingItem) {
        val oldItemVersion = shoppingItemDao.getItemById(item.id).firstOrNull()
        // Ensure all relevant fields are part of the 'item' passed to update.
        // The 'item' object from ViewModel should contain new priority, category, quantity, unit, location, isBought, boughtTimestamp etc.
        shoppingItemDao.update(item) // Optimistic local update
        println("Repository: Optimistically updated '${item.name}' (isBought: ${item.isBought}, Prio: ${item.priority}, Cat: ${item.category}, Qty: ${item.quantity} ${item.unit}, Loc: ${item.purchaseLocation}) in local cache.")

        try {
            // supabaseClient.updateShoppingItem should ideally take the full 'item' object
            // or have parameters for all updatable fields.
            val success = supabaseClient.updateShoppingItem(familyId, item) // Assuming client takes the whole item
            if (!success) {
                println("Repository: Failed to update '${item.name}' on remote. Reverting local change or queueing.")
                if (oldItemVersion != null) { // Revert if possible
                    shoppingItemDao.update(oldItemVersion)
                    println("Repository: Reverted local update for '${item.name}'.")
                }
                // Conceptual: Queue for later sync.
            } else {
                println("Repository: Successfully updated '${item.name}' on remote.")
            }
        } catch (e: Exception) {
            println("Repository: Network error updating '${item.name}': ${e.message}. Local change needs sync.")
            if (oldItemVersion != null) { // Revert on network error too for more robust UX
                 shoppingItemDao.update(oldItemVersion)
                 println("Repository: Reverted local update for '${item.name}' due to network error.")
            }
            // Conceptual: Queue for later sync.
        }
    }

    /**
     * Deletes an item.
     * 1. Deletes from local DAO immediately.
     * 2. Tries to delete from Supabase.
     * 3. If Supabase delete fails, conceptual: re-insert or mark for sync.
     *
     * @param familyId The family ID.
     * @param itemId The ID of the item to delete.
     */
    suspend fun deleteShoppingItem(familyId: String, itemId: String) {
        val itemToDelete = shoppingItemDao.getItemById(itemId).firstOrNull()
        if (itemToDelete == null) {
            println("Repository: Item with ID $itemId not found locally for deletion.")
            // Optionally, still try to delete from remote if ID is known
            // supabaseClient.deleteShoppingItem(familyId, itemId)
            return
        }
        shoppingItemDao.deleteById(itemId) // Optimistic local delete
        println("Repository: Optimistically deleted item ID $itemId from local cache.")

        try {
            val success = supabaseClient.deleteShoppingItem(familyId, itemId)
            if (!success) {
                println("Repository: Failed to delete item ID $itemId from remote. Re-inserting locally or queueing.")
                // Conceptual: Re-insert local item or queue for later sync.
                // Re-inserting: shoppingItemDao.insert(itemToDelete)
                // For now, log and leave the optimistic delete.
            } else {
                println("Repository: Successfully deleted item ID $itemId from remote.")
            }
        } catch (e: Exception) {
            println("Repository: Network error deleting item ID $itemId: ${e.message}. Local delete needs sync.")
            // Conceptual: Queue for later sync (e.g., add to a "pending_sync_deletes" table with the itemId).
        }
    }

    // Conceptual: Function to trigger synchronization of pending changes
    // suspend fun syncPendingChanges() { /* ... */ }

    /**
     * Gets all shopping items that are marked as bought.
     * For simulation, this filters the local DAO's items. In a real app,
     * this might fetch from a separate "history" table or query based on 'isBought' and possibly 'isArchived'.
     *
     * @param familyId The ID of the family.
     * @return A Flow of the list of bought shopping items.
     */
    fun getBoughtShoppingItems(familyId: String): Flow<List<ShoppingItem>> {
        // Simulate fetching for a specific family by simply filtering all items from DAO.
        // In a real app with proper familyId per item, DAO would have:
        // shoppingItemDao.getBoughtItemsByFamily(familyId)
        println("Repository: Getting bought shopping items for family $familyId (simulated by filtering all local bought items)")
        return shoppingItemDao.getAllItems().map { list ->
            list.filter { it.isBought }
                .sortedByDescending { it.boughtTimestamp ?: it.timestamp } // Show most recently bought first
        }.catch { e ->
            println("Repository: Error getting bought items: ${e.message}")
            emit(emptyList())
        }
    }

    /**
     * Conceptually "clears" or "archives" bought items from the active shopping list.
     *
     * Simulation: For now, this function doesn't change any data state.
     * In a real app, this might:
     * - Set an `isArchived = true` flag on bought items in the local DB and remote.
     * - Or, if `getShoppingList` filters out `isBought = true` items after a certain period,
     *   this function might just trigger a refresh or ensure that filter is applied.
     * - Or, move them to a different table.
     *
     * For this simulation, the main `getShoppingList` will need to be adjusted if we want
     * to see items disappear from it after "clearing". Currently, it shows all items.
     *
     * A simple simulation approach would be to modify `getShoppingList` to filter out
     * items that are `isBought = true` IF a certain condition is met (e.g. a flag set by this function).
     * However, without more state management, this is tricky.
     *
     * Let's assume for now this function logs the action and in a real scenario would
     * modify items (e.g., set `isArchived=true`) which `getShoppingList` would then exclude.
     *
     * @param familyId The ID of the family.
     */
    suspend fun clearBoughtItemsFromActiveList(familyId: String) {
        println("Repository: Clearing bought items from active list for family $familyId (Conceptual)")
        // Conceptual:
        // 1. Get all items where isBought = true and isArchived = false for the family.
        // val itemsToClear = shoppingItemDao.getAllItems().firstOrNull()
        //     ?.filter { it.isBought && (it.isArchived == false) /* if isArchived field exists */ }
        //
        // itemsToClear?.forEach { item ->
        //     // Update local DAO: item.copy(isArchived = true)
        //     // shoppingItemDao.update(item.copy(isArchived = true))
        //
        //     // Update remote (Supabase):
        //     // supabaseClient.updateShoppingItem(familyId, item.copy(isArchived = true))
        // }
        //
        // After this, the main getShoppingList() flow would naturally update if it filters by isArchived=false.
        // For this simulation, no actual data change will occur here.
        // The UI for ShoppingListScreen would need to re-fetch or have its flow update.
        // To make an immediate effect for simulation in ShoppingListScreen, one might have to
        // manually update the SupabaseClient's internal list to remove bought items.
        // SupabaseClient.removeBoughtItemsFromSimulatedList(familyId) // This function would need to be added to SupabaseClient
    }
}
