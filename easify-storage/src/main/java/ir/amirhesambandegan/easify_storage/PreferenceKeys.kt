package ir.amirhesambandegan.easify_storage

import androidx.datastore.preferences.core.*

/**
 * Helper object for creating DataStore Preference Keys without boilerplate.
 * Provides functions to instantiate keys for various primitive types.
 */
object PreferenceKeys {
    /** 
     * Creates a String preference key. 
     * 
     * @param name The name of the preference.
     * @return A DataStore key for a String value.
     */
    fun stringKey(name: String) = stringPreferencesKey(name)
    
    /** 
     * Creates an Int preference key. 
     * 
     * @param name The name of the preference.
     * @return A DataStore key for an Int value.
     */
    fun intKey(name: String) = intPreferencesKey(name)
    
    /** 
     * Creates a Boolean preference key. 
     * 
     * @param name The name of the preference.
     * @return A DataStore key for a Boolean value.
     */
    fun booleanKey(name: String) = booleanPreferencesKey(name)
    
    /** 
     * Creates a Float preference key. 
     * 
     * @param name The name of the preference.
     * @return A DataStore key for a Float value.
     */
    fun floatKey(name: String) = floatPreferencesKey(name)
    
    /** 
     * Creates a Long preference key. 
     * 
     * @param name The name of the preference.
     * @return A DataStore key for a Long value.
     */
    fun longKey(name: String) = longPreferencesKey(name)
    
    /** 
     * Creates a Double preference key. 
     * 
     * @param name The name of the preference.
     * @return A DataStore key for a Double value.
     */
    fun doubleKey(name: String) = doublePreferencesKey(name)
    
    /** 
     * Creates a String Set preference key. 
     * 
     * @param name The name of the preference.
     * @return A DataStore key for a Set of String values.
     */
    fun stringSetKey(name: String) = stringSetPreferencesKey(name)
}
