package ir.amirhesambandegan.easify_storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * A generic Base DAO for Room Database to reduce boilerplate.
 * Extend this interface for your specific entities to inherit basic CRUD operations.
 *
 * @param T The type of the entity managed by this DAO.
 */
@Dao
interface BaseDao<T> {

    /**
     * Inserts a single entity into the database. If an entity with the same primary key exists, it will be replaced.
     *
     * @param entity The entity to be inserted.
     * @return The row ID of the newly inserted entity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T): Long

    /**
     * Inserts multiple entities into the database using varargs. Existing entities with the same primary key will be replaced.
     *
     * @param entity The entities to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg entity: T)

    /**
     * Inserts a list of entities into the database. Existing entities with the same primary key will be replaced.
     *
     * @param entities The list of entities to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<T>)

    /**
     * Updates an existing entity in the database.
     *
     * @param entity The entity with updated fields.
     */
    @Update
    suspend fun update(entity: T)

    /**
     * Updates multiple existing entities in the database.
     *
     * @param entity The entities to be updated.
     */
    @Update
    suspend fun updateAll(vararg entity: T)

    /**
     * Deletes a specific entity from the database.
     *
     * @param entity The entity to be deleted.
     */
    @Delete
    suspend fun delete(entity: T)
}
