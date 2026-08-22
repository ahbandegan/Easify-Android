package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.mutableStateListOf

/**
 * Manages an array of dynamic repeating form groups.
 */
class FormArrayState(private val templateBuilder: FormBuilder.() -> Unit) {
    val items = mutableStateListOf<FormState>()
    
    init {
        addItem() // Add at least one default sub-form
    }
    
    fun addItem() {
        val builder = FormBuilder()
        builder.templateBuilder()
        items.add(builder.build())
    }
    
    fun removeItem(index: Int) {
        if (index >= 0 && index < items.size) {
            items.removeAt(index)
        }
    }
    
    fun validateAll(): Boolean {
        return items.all { it.validateAll() }
    }
    
    fun toListMap(): List<Map<String, String>> {
        return items.map { it.toMap() }
    }
}
