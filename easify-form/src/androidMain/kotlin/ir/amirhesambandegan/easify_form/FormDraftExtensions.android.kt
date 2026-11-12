package ir.amirhesambandegan.easify_form

import android.content.Context
import androidx.core.content.edit

private const val DRAFT_PREFS_NAME = "easify_form_drafts"

/**
 * Auto-saves the current form values to [SharedPreferences] as a JSON draft.
 */
fun FormState.saveDraft(context: Context, formId: String) {
    val prefs = context.applicationContext.getSharedPreferences(DRAFT_PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit { putString(formId, exportDraftJson()) }
}

/**
 * Restores previously saved form values from [SharedPreferences].
 */
fun FormState.restoreDraft(context: Context, formId: String) {
    val prefs = context.applicationContext.getSharedPreferences(DRAFT_PREFS_NAME, Context.MODE_PRIVATE)
    val jsonStr = prefs.getString(formId, null) ?: return
    importDraftJson(jsonStr)
}

/**
 * Clears a saved JSON draft from [SharedPreferences].
 */
fun FormState.clearDraft(context: Context, formId: String) {
    val prefs = context.applicationContext.getSharedPreferences(DRAFT_PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit { remove(formId) }
}
