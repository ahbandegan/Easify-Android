package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.mutableStateListOf

/**
 * Manages an array of dynamic repeating form groups.
 * 
 * This is useful for forms where the user can dynamically add or remove sections,
 * such as a list of work experiences or a list of addresses.
 *
 * @param templateBuilder A lambda function acting as a builder for each form item.
 */
class FormArrayState(private val templateBuilder: FormBuilder.() -> Unit) {
    
    /**
     * The observable list of [FormState] items currently in this array.
     */
    val items = mutableStateListOf<FormState>()
    
    init {
        addItem() // Add at least one default sub-form
    }
    
    /**
     * Appends a new form item to the end of the array based on the [templateBuilder].
     */
    fun addItem() {
        val builder = FormBuilder()
        builder.templateBuilder()
        items.add(builder.build())
    }
    
    /**
     * Removes the form item at the specified index.
     *
     * @param index The index of the item to remove.
     */
    fun removeItem(index: Int) {
        if (index >= 0 && index < items.size) {
            items.removeAt(index)
        }
    }
    
    /**
     * Validates all form items in the array.
     *
     * @return `true` if all items are valid, `false` otherwise.
     */
    fun validateAll(): Boolean {
        return items.all { it.validateAll() }
    }
    
    /**
     * Converts the array of forms into a list of maps, where each map
     * represents a single form's field keys and their string values.
     *
     * @return A list containing the map representation of each form item.
     */
    fun toListMap(): List<Map<String, String>> {
        return items.map { it.toMap() }
    }
}
