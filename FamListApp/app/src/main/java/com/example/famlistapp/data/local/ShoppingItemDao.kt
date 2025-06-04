package com.example.famlistapp.data.local

import com.example.famlistapp.data.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the shopping_items table.
 * In a real app, this would be a Room DAO interface.
 * For simulation, its methods will be implemented by a fake in-memory DAO.
 */
interface ShoppingItemDao {

    /**
     * Observes list of all shopping items.
     * @return Flow emitting a list of all shopping items.
     */
    fun getAllItems(): Flow<List<ShoppingItem>>

    /**
     * Observes a single item by ID.
     * @param itemId The ID of the item.
     * @return Flow emitting the item, or null if not found.
     */
    fun getItemById(itemId: String): Flow<ShoppingItem?>

    /**
     * Insert all items, replacing any existing items on conflict.
     * @param items The list of items to be inserted.
     */
    suspend fun insertAll(items: List<ShoppingItem>)

    /**
     * Insert an item, replacing on conflict.
     * @param item The item to be inserted.
     */
    suspend fun insert(item: ShoppingItem)

    /**
     * Update an item.
     * @param item The item to be updated.
     * @return The number of rows updated. Should be 1 if the item exists.
     */
    suspend fun update(item: ShoppingItem): Int

    /**
     * Delete an item.
     * @param item The item to be deleted.
     * @return The number of rows deleted. Should be 1 if the item exists.
     */
    suspend fun delete(item: ShoppingItem): Int

    /**
     * Delete an item by its ID.
     * @param itemId The ID of the item to be deleted.
     * @return The number of rows deleted. Should be 1 if the item exists.
     */
    suspend fun deleteById(itemId: String): Int

    /**
     * Delete all items from the table.
     */
    suspend fun clearAll()
}
