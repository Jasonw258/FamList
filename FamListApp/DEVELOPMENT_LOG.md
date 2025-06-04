# FamList - 易购清单 Development Log

This document records the development process of the FamList Android application.

## Phase 0: Project Setup

*   **Timestamp:** 2025-06-03T22:06:57Z
*   **Action:** Initialized Android project structure (Kotlin + Jetpack Compose).
*   **Details:** Created placeholder files for MainActivity and Supabase client. Created this log file.
*   **Files Created:**
    *   `app/src/main/java/com/example/famlistapp/MainActivity.kt`
    *   `app/src/main/java/com/example/famlistapp/SupabaseClient.kt`
    *   `DEVELOPMENT_LOG.md`

## Phase 1: MVP - Core Family and List Functions

### Subsection: Family Group Management - UI

*   **Timestamp:** 2025-06-03T22:09:48Z
*   **Action:** Implemented UI for Family Creation and Joining (MVP).
*   **Details:**
    *   Created `OnboardingScreen.kt` with options to navigate to Create Family or Join Family.
    *   Created `CreateFamilyScreen.kt` with input fields for family name, nickname, avatar placeholder, and simulated family code display. Added placeholder buttons for sharing.
    *   Created `JoinFamilyScreen.kt` with input fields for family code, nickname, and avatar placeholder.
    *   Updated `MainActivity.kt` conceptually to host these screens.
*   **Files Created/Modified:**
    *   `app/src/main/java/com/example/famlistapp/ui/screens/onboarding/OnboardingScreen.kt`
    *   `app/src/main/java/com/example/famlistapp/ui/screens/onboarding/CreateFamilyScreen.kt`
    *   `app/src/main/java/com/example/famlistapp/ui/screens/onboarding/JoinFamilyScreen.kt`
    *   `app/src/main/java/com/example/famlistapp/MainActivity.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/theme/Theme.kt` (created)
    *   `app/src/main/java/com/example/famlistapp/ui/theme/Color.kt` (created)
    *   `app/src/main/java/com/example/famlistapp/ui/theme/Typography.kt` (created)

### Subsection: Family Group Management - Backend Simulation

*   **Timestamp:** 2025-06-03T22:11:46Z
*   **Action:** Defined data models and simulated Supabase client logic for family management (MVP).
*   **Details:**
    *   Created `Family.kt` and `User.kt` data classes.
    *   Added placeholder functions in `SupabaseClient.kt` for `createFamily` and `joinFamily`, simulating interactions with a Supabase backend.
    *   Created basic ViewModel placeholders (`CreateFamilyViewModel.kt`, `JoinFamilyViewModel.kt`) to manage the logic for the onboarding screens.
*   **Files Created/Modified:**
    *   `app/src/main/java/com/example/famlistapp/data/model/Family.kt`
    *   `app/src/main/java/com/example/famlistapp/data/model/User.kt`
    *   `app/src/main/java/com/example/famlistapp/SupabaseClient.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/onboarding/viewmodel/CreateFamilyViewModel.kt`
    *   `app/src/main/java/com/example/famlistapp/ui/screens/onboarding/viewmodel/JoinFamilyViewModel.kt`

### Subsection: Basic Shopping List - UI and Backend Simulation

*   **Timestamp:** 2025-06-03T22:14:41Z
*   **Action:** Implemented UI and simulated backend logic for basic shopping list functionality (MVP).
*   **Details:**
    *   Created `ShoppingItem.kt` data class.
    *   Developed `ShoppingListScreen.kt` to display items, allow adding new items (simple input), mark items as bought, and delete items.
    *   Added placeholder functions in `SupabaseClient.kt` for `getShoppingList` (simulating real-time flow), `addShoppingItem`, `updateShoppingItem`, and `deleteShoppingItem`.
    *   Created `ShoppingListViewModel.kt` to manage the state and interactions for the shopping list.
    *   Updated `MainActivity.kt` to conceptually navigate to the shopping list screen.
*   **Files Created/Modified:**
    *   `app/src/main/java/com/example/famlistapp/data/model/ShoppingItem.kt`
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/ShoppingListScreen.kt`
    *   `app/src/main/java/com/example/famlistapp/SupabaseClient.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/viewmodel/ShoppingListViewModel.kt`
    *   `app/src/main/java/com/example/famlistapp/MainActivity.kt` (modified)

## Phase 2: Enhancements

### Subsection: Offline Support

*   **Timestamp:** 2025-06-03T22:17:08Z
*   **Action:** Conceptualized offline support with local caching (SQLite/Room simulation).
*   **Details:**
    *   Defined `ShoppingItemDao.kt` interface for local database operations.
    *   Created a placeholder `AppDatabase.kt`.
    *   Introduced `ShoppingRepository.kt` to manage data from local (simulated Room Dao) and remote (Supabase) sources.
    *   The repository's logic conceptually includes fetching from cache first, then network, and updating cache. It also conceptually handles queueing offline modifications.
    *   Updated `ShoppingListViewModel.kt` to use `ShoppingRepository.kt`.
*   **Files Created/Modified:**
    *   `app/src/main/java/com/example/famlistapp/data/local/ShoppingItemDao.kt`
    *   `app/src/main/java/com/example/famlistapp/data/local/AppDatabase.kt`
    *   `app/src/main/java/com/example/famlistapp/data/ShoppingRepository.kt`
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/viewmodel/ShoppingListViewModel.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/SupabaseClient.kt` (reviewed/minor modification for repository use)

### Subsection: Voice Input

*   **Timestamp:** 2025-06-03T22:18:44Z
*   **Action:** Added placeholder for voice input functionality.
*   **Details:**
    *   Added a microphone icon button to `ShoppingListScreen.kt` to trigger voice input.
    *   Created a `VoiceRecognitionService.kt` with a placeholder `startListening` function to simulate speech recognition.
    *   Updated `ShoppingListViewModel.kt` to include a method to initiate voice input via the service and add the recognized item to the list.
    *   Noted the need for microphone permissions and `SpeechRecognizer` API in a real implementation.
*   **Files Created/Modified:**
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/ShoppingListScreen.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/services/VoiceRecognitionService.kt`
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/viewmodel/ShoppingListViewModel.kt` (modified)

### Subsection: Online Status Display & List Refactor

*   **Timestamp:** 2025-06-03T22:21:08Z
*   **Action:** Implemented online status display and refactored shopping list to group items by user.
*   **Details:**
    *   Updated `User.kt` model with `isOnline` and `lastSeenTimestamp` fields.
    *   Updated `ShoppingItem.kt` to include `addedByUserName` for easier display and made `addedByUserId` non-nullable.
    *   Added `getFamilyMembersWithStatus` to `SupabaseClient.kt` to simulate fetching user presence.
    *   Refactored `ShoppingListViewModel.kt` to fetch family members and structure the shopping list as items grouped by user (using a new `UserShoppingList` data class), including their online status. It now uses `combine` to merge user data and shopping items.
    *   Modified `ShoppingListScreen.kt` to display items grouped by user, showing their name and simulated online status (e.g., "[在线]" or "1小时前在线") with a new `UserGroupHeader` composable and status formatting logic.
*   **Files Created/Modified:**
    *   `app/src/main/java/com/example/famlistapp/data/model/User.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/data/model/ShoppingItem.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/SupabaseClient.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/viewmodel/ShoppingListViewModel.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/ShoppingListScreen.kt` (modified)

### Subsection: UI Polish

*   **Timestamp:** 2025-06-03T22:24:13Z
*   **Action:** Performed a conceptual UI polish and review.
*   **Details:**
    *   Reviewed onboarding screens (`OnboardingScreen.kt`, `CreateFamilyScreen.kt`, `JoinFamilyScreen.kt`) for button prominence and clarity, adding comments for potential animations and adjusting layout/styling for better visual hierarchy and usability (e.g., `fillMaxWidth` for buttons, distinct avatar placeholder, improved spacing, keyboard handling).
    *   Reviewed `ShoppingListScreen.kt` to ensure the 'Add Item' ("我要买东西") button is prominent (relocated to bottom action bar), and list item layout is clear. Added comments for list animations and refined overall screen structure for better UX.
    *   Reviewed theme files (`Color.kt`, `Theme.kt`, `Typography.kt`) for reasonable Material 3 defaults. Added comments for integrating custom brand/semantic colors (e.g., for "important" or "completed" states) and for using custom fonts. Updated `Theme.kt` to prefer app-defined colors over dynamic system colors for more consistent branding and adjusted status bar handling. Expanded `Typography.kt` to include the full M3 type scale with comments on customization.
    *   Ensured UI elements generally align with Material Design 3 principles at a high level.
*   **Files Created/Modified:**
    *   `app/src/main/java/com/example/famlistapp/ui/screens/onboarding/OnboardingScreen.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/onboarding/CreateFamilyScreen.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/onboarding/JoinFamilyScreen.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/ShoppingListScreen.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/theme/Color.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/theme/Theme.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/theme/Typography.kt` (modified)

## Phase 3: Advanced Features

### Subsection: Push Notifications (Conceptual)

*   **Timestamp:** 2025-06-03T22:27:02Z
*   **Action:** Conceptualized push notification functionality for urgent items.
*   **Details:**
    *   Added `priority` field to `ShoppingItem.kt` (as String with backing enum `ShoppingItemPriority`).
    *   Created `PushNotificationService.kt` placeholder for handling FCM messages, with simulated `onNewToken`, `onMessageReceived`, and `displayNotification` methods.
    *   Modified `ShoppingRepository.kt`'s `addShoppingItem` to accept item priority and conceptually trigger a notification via `SupabaseClient.kt` if an item is marked "急需" (Urgent).
    *   Added `sendUrgentItemNotification` placeholder in `SupabaseClient.kt` to simulate the backend call for sending a notification.
    *   Updated `ShoppingListScreen.kt`'s `AddItemDialog` to include `RadioButton`s for selecting item priority (Urgent, Normal, Low) and to display the priority emoji in the `ShoppingListItemRow`.
    *   Updated `ShoppingListViewModel.kt`'s `addItem` function to accept the priority string and pass it to the repository. Voice input defaults to Normal priority.
*   **Files Created/Modified:**
    *   `app/src/main/java/com/example/famlistapp/services/PushNotificationService.kt`
    *   `app/src/main/java/com/example/famlistapp/data/model/ShoppingItem.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/data/ShoppingRepository.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/SupabaseClient.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/ShoppingListScreen.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/viewmodel/ShoppingListViewModel.kt` (modified)

### Subsection: Shopping History & Clearing Bought Items

*   **Timestamp:** 2025-06-03T22:30:13Z
*   **Action:** Implemented shopping history viewing and a "Clean up bought items" feature.
*   **Details:**
    *   Added `boughtTimestamp` (Long?) to `ShoppingItem.kt` to record when an item was marked as bought.
    *   Added a "[🧹 清理已买到的]" (Clean up bought items) TextButton and a "查看历史" (View History) TextButton to `ShoppingListScreen.kt` in a new bottom action bar.
    *   Created `ShoppingHistoryScreen.kt` in `app/src/main/java/com/example/famlistapp/ui/screens/history/` to display items marked as bought, showing item name, priority emoji, who bought it, and the formatted `boughtTimestamp`. Includes a TopAppBar with a back navigation button.
    *   Created `ShoppingHistoryViewModel.kt` in `app/src/main/java/com/example/famlistapp/ui/screens/history/viewmodel/` to manage data loading for the history screen using a new repository method.
    *   Updated `ShoppingRepository.kt`:
        *   Added `fun getBoughtShoppingItems(familyId: String): Flow<List<ShoppingItem>>` to fetch items where `isBought = true`, sorted by `boughtTimestamp`.
        *   Added a conceptual `suspend fun clearBoughtItemsFromActiveList(familyId: String)` which currently logs the action (actual data modification for "clearing" is simulated).
    *   Updated `ShoppingListViewModel.kt`:
        *   Modified `toggleItemBought` to set `boughtTimestamp` when an item is marked as bought and clear it if unmarked.
        *   Added `fun onClearBoughtItemsClicked()` to call the repository's `clearBoughtItemsFromActiveList`.
    *   Updated `MainActivity.kt` conceptually by adding `ShoppingHistory` to the `Screen` enum and including a navigation case for it.
*   **Files Created/Modified:**
    *   `app/src/main/java/com/example/famlistapp/data/model/ShoppingItem.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/ShoppingListScreen.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/history/ShoppingHistoryScreen.kt`
    *   `app/src/main/java/com/example/famlistapp/ui/screens/history/viewmodel/ShoppingHistoryViewModel.kt`
    *   `app/src/main/java/com/example/famlistapp/data/ShoppingRepository.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/viewmodel/ShoppingListViewModel.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/MainActivity.kt` (modified)

### Subsection: Item Categorization & Conceptual Suggestions/Ingredients

*   **Timestamp:** 2025-06-03T22:33:26Z
*   **Action:** Implemented item categorization and conceptual placeholders for smart suggestions and a common ingredient library.
*   **Details:**
    *   Added `category: String?` field to `ShoppingItem.kt`.
    *   Created `DefaultCategories.kt` in `app/src/main/java/com/example/famlistapp/data/model/` with a predefined list of Chinese shopping categories (e.g., "蔬菜🥬", "水果🍎").
    *   Updated `AddItemDialog` in `ShoppingListScreen.kt` to include `FilterChip`s for selecting a category from `DefaultCategories.categories`. The dialog content is now scrollable.
    *   `ShoppingListItemRow` in `ShoppingListScreen.kt` now displays the item's category below its name if available.
    *   `ShoppingListViewModel.kt`'s `addItem` function was updated to accept the optional `category` string and pass it to the repository. Voice input defaults to null category.
    *   `ShoppingRepository.kt`'s `addShoppingItem` function was updated to accept and store the `category`. `updateShoppingItem` was also updated to reflect category changes.
    *   Added a placeholder `TextButton` for "💡 时令推荐" (Seasonal Recommendations) on `ShoppingListScreen.kt`.
    *   Created `CommonChineseIngredients.kt` in `app/src/main/java/com/example/famlistapp/data/` with a placeholder list of common Chinese ingredients, which could be used for future "smart input" features.
*   **Files Created/Modified:**
    *   `app/src/main/java/com/example/famlistapp/data/model/ShoppingItem.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/data/model/LocalizedData.kt` (renamed from Categories.kt and modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/ShoppingListScreen.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/viewmodel/ShoppingListViewModel.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/data/ShoppingRepository.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/data/CommonIngredients.kt`

### Subsection: Advanced Filtering

*   **Timestamp:** 2025-06-03T22:35:11Z
*   **Action:** Implemented advanced filtering functionality for the shopping list.
*   **Details:**
    *   Added a filter icon button (`Icons.Filled.FilterList`) to the header area in `ShoppingListScreen.kt`.
    *   Created a `FilterDialog` Composable within `ShoppingListScreen.kt`. This dialog uses `AlertDialog` and `FilterChip`s to allow users to select multiple filters for:
        *   Categories (from `DefaultCategories.categories`).
        *   Users (from `viewModel.uiState.allUsersInFamily`).
        *   Priorities (from `ShoppingItemPriority.values()`).
    *   Updated `ShoppingListViewModel.kt`:
        *   Added `selectedCategories`, `selectedUserIds`, `selectedPriorities`, and `allUsersInFamily` to `ShoppingListUiState`.
        *   Implemented `updateCategoryFilter`, `updateUserIdFilter`, `updatePriorityFilter`, and `clearAllFilters` functions to manage the filter selections.
        *   Modified the `loadData` function (which combines shopping items and family members) to apply the selected filters to the `shoppingItems` list before grouping and updating the UI state. Filtered items are then displayed. Reloading data (and thus re-applying filters) is triggered when filter selections change.
*   **Files Created/Modified:**
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/ShoppingListScreen.kt` (modified, and new FilterDialog Composable likely within it or linked)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/viewmodel/ShoppingListViewModel.kt` (modified)

## Phase 5: Localization and Cultural Features

*   **Timestamp:** 2025-06-03T22:44:56Z
*   **Action:** Implemented localization structure, conceptual large font support, and Chinese-specific units/location tags.
*   **Details:**
    *   Set up `strings.xml` files for default (English, `values/strings.xml`), Simplified Chinese (`values-zh-rCN/strings.xml`), and Traditional Chinese (`values-zh-rTW/strings.xml`). Demonstrated usage in `OnboardingScreen.kt` and `ShoppingListScreen.kt` using `stringResource(R.string.some_key)`.
    *   Added comments in `MainActivity.kt` about how system locale changes trigger recomposition and in `Typography.kt` regarding strategies for large font support (e.g., using `LocalDensity.current.fontScale` or defining alternative typography sets). A conceptual comment was added to a `Text` composable in `ShoppingListScreen.kt` for dynamic font sizing.
    *   Added `quantity: String?`, `unit: String?`, and `purchaseLocation: String?` fields to `ShoppingItem.kt`.
    *   Renamed `Categories.kt` to `LocalizedData.kt` and added `DefaultUnits` and `DefaultPurchaseLocations` objects with common Chinese options. Updated `ShoppingListScreen.kt` to import from `LocalizedData.kt`.
    *   Updated `AddItemDialog` in `ShoppingListScreen.kt` to include a `TextField` for quantity and `ExposedDropdownMenuBox` for selecting unit and purchase location from the predefined lists.
    *   `ShoppingListItemRow` in `ShoppingListScreen.kt` was updated to display the quantity, unit, and purchase location if available.
    *   `ShoppingListViewModel.kt`'s `addItem` function was updated to accept `quantity`, `unit`, and `location` and pass them to the repository. Voice input defaults to null for these new fields.
    *   `ShoppingRepository.kt`'s `addShoppingItem` function was updated to accept and store `quantity`, `unit`, and `purchaseLocation`. The `updateShoppingItem` function was also conceptually updated to handle these fields.
*   **Files Created/Modified:**
    *   `app/src/main/res/values/strings.xml`
    *   `app/src/main/res/values-zh-rCN/strings.xml`
    *   `app/src/main/res/values-zh-rTW/strings.xml`
    *   `app/src/main/java/com/example/famlistapp/ui/theme/Typography.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/data/model/ShoppingItem.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/data/model/LocalizedData.kt` (renamed from Categories.kt and modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/ShoppingListScreen.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/onboarding/OnboardingScreen.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/ui/screens/shoppinglist/viewmodel/ShoppingListViewModel.kt` (modified)
    *   `app/src/main/java/com/example/famlistapp/data/ShoppingRepository.kt` (modified)

## Phase 6: Testing and Refinement

*   **Timestamp:** 2025-06-03T22:46:31Z
*   **Action:** Outlined testing and refinement strategies based on project requirements.

This phase focuses on ensuring the application is robust, user-friendly, and meets the specific needs of Chinese families. As this is a simulated development process, actual testing cannot be performed. Instead, this section outlines the methodologies and areas of focus for testing.

### 1. Manual Testing of Core Functionalities

Thorough manual testing would be conducted for all core features:

*   **Family Group Management:**
    *   Verify successful creation of new family groups with Chinese names.
    *   Test generation and sharing of the 6-digit family code (including via simulated WeChat/QQ).
    *   Test joining existing family groups using the code.
    *   Verify setting and display of Chinese nicknames and culturally appropriate avatars.
*   **Shopping List CRUD Operations:**
    *   Test adding new items, ensuring all fields (name, quantity, unit, priority, category, purchase location) are correctly saved and displayed.
    *   Test marking items as bought/unbought and ensure `boughtTimestamp` is handled.
    *   Test deleting items from the list.
    *   Verify that items are correctly associated with the user who added them.
*   **Real-time Synchronization (Conceptual):**
    *   Simulate two users on different devices.
    *   User A adds/edits/deletes an item. Verify User B's list updates in (simulated) real-time.
    *   Test conflict resolution (e.g., if both users edit the same item simultaneously - based on timestamp).
*   **Offline Mode (Conceptual):**
    *   Device A goes offline. User A adds/edits items.
    *   Device A comes back online. Verify changes are synced to Supabase and other family members' devices.
    *   Test handling of changes made by other users while Device A was offline.

### 2. UI/UX Testing and Refinement

Special attention would be paid to the user experience, especially for the target demographic:

*   **Target User Usability Testing:**
    *   Recruit Chinese families, including members of different age groups (especially 50+ elderly and ~10-year-old children).
    *   Observe users performing common tasks: creating a family, adding items, viewing the list, marking items as bought.
    *   Gather feedback on ease of use, clarity of Chinese labels, icon intuitiveness, font readability (especially large fonts for seniors), and overall satisfaction.
    *   Test with users who speak different Chinese dialects to assess comprehension and potential issues with voice input.
*   **Simplified/Traditional Chinese Support:**
    *   Verify that switching the device language correctly changes the app's language.
    *   Check all screens and UI elements for accurate translation and display in both Simplified and Traditional Chinese.
    *   Ensure no layout issues (e.g., text truncation) occur due to language changes.
*   **Input Method Compatibility:**
    *   Test item input using common Android Chinese input methods (e.g., Sogou Pinyin, Google Pinyin, Baidu IME, handwriting input).
*   **Voice Input Accuracy:**
    *   Test voice input for item names in Mandarin.
    *   If feasible, conduct preliminary tests for major Chinese dialects as per `instruction.md`. Assess accuracy and identify common recognition errors.
*   **Visual Design and Cultural Appropriateness:**
    *   Review color schemes and themes (including holiday themes) for cultural appropriateness and appeal.
    *   Ensure UI elements like priority markers (🔴急需) are understood as intended.

### 3. Technical Testing

*   **Cross-Device and Android Version Compatibility:**
    *   Test the app on a range of physical Android devices with varying screen sizes, resolutions, and manufacturers.
    *   Test on different Android versions (API Level 24+ as specified).
*   **Network Condition Testing:**
    *   Use network simulation tools to test app behavior under various conditions:
        *   Slow network speeds (2G, 3G).
        *   Intermittent connectivity.
        *   High latency.
    *   Verify data synchronization robustness and offline mode functionality under these conditions.

### 4. Simulated Feedback and Iteration

In a real development cycle, feedback gathered from all testing activities would be documented and prioritized. This would lead to:
*   Bug fixes and performance improvements.
*   UI/UX adjustments based on user feedback.
*   Refinement of features to better meet user needs.
This iterative process of testing and refinement is crucial for developing a high-quality application.

## Phase 7: Final Documentation and Deliverables Preparation

*   **Timestamp:** 2025-06-03T22:50:13Z
*   **Action:** Prepared final documentation elements including user manual, database design, and lexicon summary within this log. Added Chinese comments to selected source files.

This section consolidates key documentation artifacts required for the project.

### 用户手册 (User Manual - 易购清单 FamList)

欢迎使用易购清单！本应用旨在帮助您和您的家人轻松管理家庭购物需求。

**1. 创建或加入家庭组**

*   **首次使用:** 您可以选择“创建新家庭”或“加入现有家庭”。
*   **创建新家庭:**
    *   输入您的家庭名称（如“我们家”、“幸福一家”）。
    *   设置您的昵称（如“爸爸”、“妈妈”）。
    *   选择一个可爱的头像。
    *   系统会为您生成一个6位数字的【家庭码】。请将此码分享给您的家人，邀请他们加入。
*   **加入现有家庭:**
    *   向家庭创建者索取6位【家庭码】。
    *   输入家庭码和您的昵称、选择头像即可加入。

**2. 添加购物商品**

*   在家庭清单主界面，点击底部醒目的【➕ 我要买东西】按钮。
*   在“添加要买的东西”页面：
    *   **买什么:** 输入商品名称。您也可以尝试【🎤语音说话】输入。
    *   **买多少:** 输入数量（如“2”），选择单位（如“盒”、“斤”）。
    *   **急不急:** 选择优先级（🔴急需, 🟡一般, 🟢不急）。
    *   **哪里买:** 选择购买地点（如“🏪超市”, “🥬菜市场”）。
    *   **分类:** 为商品选择一个分类（如“蔬菜🥬”, “水果🍎”）。
    *   点击【✅ 确定添加】。

**3. 查看和管理清单**

*   **主界面:** 清单会按家庭成员分组显示各自要买的物品。
*   **标记已买:** 点击商品名称旁边的复选框将其标记为“已买到”。再次点击可取消。
*   **删除商品:** 点击商品条目旁边的【删除】按钮（通常是个垃圾桶图标）。
*   **在线状态:** 您可以看到家庭成员的在线状态或上次在线时间。
*   **清理已买到的:** 点击底部的【🧹 清理已买到的】按钮，可以将已购买的商品从当前主要列表中移除（它们会进入历史记录）。

**4. 使用购物历史和筛选**

*   **查看历史:** 点击主清单界面底部的【查看历史】按钮，可以浏览所有已购买的商品记录。
*   **高级筛选:** 在主清单界面，点击顶部的【筛选】图标 ( выглядит как воронка )：
    *   您可以按【分类】、【谁要买】（家庭成员）、【急不急】（优先级）来筛选当前清单中显示的商品。
    *   选择筛选条件后，清单会自动更新。

**5. 其他**
*   **语音输入:** 在添加商品页面，点击麦克风图标【🎤语音说话】即可通过语音快速输入商品名称。
*   **单位和地点:** 应用内置了常用的中文数量单位（斤、两、袋、盒等）和购买场所（超市、菜市场等），方便您快速选择。

祝您购物愉快！

### 数据库设计 (Database Design - Conceptual for Supabase)

以下为应用在Supabase后端概念上的主要数据表结构：

1.  **`families` (家庭组表)**
    *   `id`: UUID, 主键 (Primary Key)
    *   `name`: TEXT, 家庭名称 (Family Name)
    *   `family_code`: VARCHAR(6), 6位数字家庭码 (6-digit Family Code), 唯一约束 (Unique)
    *   `created_at`: TIMESTAMPTZ, 创建时间 (Creation Timestamp)

2.  **`users` (用户表)**
    *   `id`: UUID, 主键 (Primary Key), 通常与Supabase Auth用户ID关联
    *   `family_id`: UUID, 外键, 关联 `families.id` (Foreign Key to `families.id`)
    *   `nickname`: TEXT, 用户在家庭内的昵称 (Nickname within the family)
    *   `avatar_url`: TEXT, 用户选择的头像URL或标识符 (Avatar URL or identifier)
    *   `is_online`: BOOLEAN, 是否在线 (Is Online status) - 通过Presence更新
    *   `last_seen_timestamp`: TIMESTAMPTZ, 最后在线时间 (Last Seen Timestamp)
    *   `created_at`: TIMESTAMPTZ, 创建时间 (Creation Timestamp)

3.  **`shopping_items` (购物清单项目表)**
    *   `id`: UUID, 主键 (Primary Key)
    *   `family_id`: UUID, 外键, 关联 `families.id` (Foreign Key to `families.id`)
    *   `added_by_user_id`: UUID, 外键, 关联 `users.id` (Foreign Key to `users.id` - who added the item)
    *   `name`: TEXT, 商品名称 (Item Name)
    *   `quantity`: TEXT, 数量 (e.g., "1", "半") (Quantity string)
    *   `unit`: TEXT, 单位 (e.g., "斤", "盒") (Unit string)
    *   `priority`: TEXT, 优先级 (e.g., "🔴急需", "🟡一般", "🟢不急") (Priority string)
    *   `category`: TEXT, 商品分类 (e.g., "蔬菜🥬") (Category string)
    *   `purchase_location`: TEXT, 购买地点 (e.g., "超市") (Purchase Location string)
    *   `is_bought`: BOOLEAN, 是否已购买 (Is Bought status), 默认为 `false`
    *   `bought_timestamp`: TIMESTAMPTZ, NULLABLE, 标记为已购买的时间 (Timestamp when marked as bought)
    *   `notes`: TEXT, NULLABLE, 备注 (Optional notes for the item) - *新增建议字段* (new suggested field)
    *   `created_at`: TIMESTAMPTZ, 创建时间 (Creation Timestamp)
    *   `updated_at`: TIMESTAMPTZ, 最后更新时间 (Last Update Timestamp)

4.  **`pending_sync_items` (离线待同步项目表 - Conceptual for Offline Support)**
    *   `id`: UUID, 主键 (Primary Key for local cache entry)
    *   `item_data`: JSONB, 完整的 `shopping_items` 对象数据 (Full `shopping_items` object data)
    *   `operation_type`: TEXT, 操作类型 ('ADD', 'UPDATE', 'DELETE') (Operation Type)
    *   `attempt_count`: INTEGER, 同步尝试次数 (Sync Attempt Count)
    *   `created_at`: TIMESTAMPTZ, 创建时间 (Creation Timestamp)

### 商品词库与分类体系 (Item Lexicon and Category System)

为了提升中文用户体验，应用内置了初步的词库和分类体系：

*   **商品分类 (Item Categories):**
    *   定义于 `app/src/main/java/com/example/famlistapp/data/model/LocalizedData.kt` 中的 `DefaultCategories.categories`。
    *   包含如：“蔬菜🥬”、“水果🍎”、“肉类🥩”、“调料🧂”、“日用品🧴”等常见中文购物分类。
    *   用户在添加商品时可以选择这些预设分类。
*   **数量单位 (Quantity Units):**
    *   定义于 `LocalizedData.kt` 中的 `DefaultUnits.units`。
    *   包含如：“个”、“斤”、“两”、“公斤”、“袋”、“盒”、“瓶”等常用中文单位。
*   **购买地点 (Purchase Locations):**
    *   定义于 `LocalizedData.kt` 中的 `DefaultPurchaseLocations.locations`。
    *   包含如：“🏪超市”、“🥬菜市场”、“🛒网购”、“🏬商场”等常见购物地点。
*   **常用食材库 (Common Ingredients Library - Conceptual):**
    *   初步定义于 `app/src/main/java/com/example/famlistapp/data/CommonIngredients.kt`。
    *   包含如：“生抽”、“老抽”、“料酒”、“葱姜蒜”等中餐常用备料。
    *   此词库未来可用于智能输入提示、菜谱关联等高级功能。

这些预设数据旨在简化用户输入，并使应用更贴近中文家庭的购物习惯。
