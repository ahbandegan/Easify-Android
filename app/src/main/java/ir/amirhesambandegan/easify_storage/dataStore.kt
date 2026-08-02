package ir.amirhesambandegan.easify_storage

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/**
 * Extension property to provide a single instance of DataStore across the application.
 */
val Context.dataStore by preferencesDataStore("pref")
