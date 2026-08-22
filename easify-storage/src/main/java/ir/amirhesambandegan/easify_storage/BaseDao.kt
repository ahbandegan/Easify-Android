package ir.amirhesambandegan.easify_storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * A generic Base DAO for Room Database to reduce boilerplate.
 * Extend this interface for your specific entities.
 */
@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<T>)

    @Update
    suspend fun update(entity: T)

    @Update
    suspend fun updateAll(vararg entity: T)

    @Delete
    suspend fun delete(entity: T)
}
