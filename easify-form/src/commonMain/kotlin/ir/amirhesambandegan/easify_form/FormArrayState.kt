package ir.amirhesambandegan.easify_form

import androidx.compose.runtime.mutableStateListOf

/**
 * Manages an array of dynamic repeating form groups.
 */
class FormArrayState(private val templateBuilder: FormBuilder.() -> Unit) {

    val items = mutableStateListOf<FormState>()

    val isDirty: Boolean
        get() = items.any { it.isDirty }

    init {
        addItem()
    }

    fun addItem() {
        val builder = FormBuilder()
        builder.templateBuilder()
        items.add(builder.build())
    }

    fun removeItem(index: Int) {
        if (index in items.indices) {
            items.removeAt(index)
        }
    }

    /**
     * Validates all form items without short-circuiting so every invalid group displays its errors.
     */
    fun validateAll(): Boolean {
        var allValid = true
        for (item in items) {
            if (!item.validateAll()) {
                allValid = false
            }
        }
        return allValid
    }

    fun resetAll() {
        items.forEach { it.resetAll() }
    }

    fun toListMap(): List<Map<String, String>> = items.map { it.toMap() }
}
