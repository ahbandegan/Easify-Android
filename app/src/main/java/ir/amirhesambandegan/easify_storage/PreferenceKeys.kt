package ir.amirhesambandegan.easify_storage

import androidx.datastore.preferences.core.*

/**
 * Helper object for creating DataStore Preference Keys without boilerplate.
 */
object PreferenceKeys {
    /** Creates a String preference key. */
    fun stringKey(name: String) = stringPreferencesKey(name)
    
    /** Creates an Int preference key. */
    fun intKey(name: String) = intPreferencesKey(name)
    
    /** Creates a Boolean preference key. */
    fun booleanKey(name: String) = booleanPreferencesKey(name)
    
    /** Creates a Float preference key. */
    fun floatKey(name: String) = floatPreferencesKey(name)
    
    /** Creates a Long preference key. */
    fun longKey(name: String) = longPreferencesKey(name)
    
    /** Creates a Double preference key. */
    fun doubleKey(name: String) = doublePreferencesKey(name)
    
    /** Creates a String Set preference key. */
    fun stringSetKey(name: String) = stringSetPreferencesKey(name)
}
