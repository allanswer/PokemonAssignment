package com.example.test.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.test.data.local.database.entities.CapturedPokemonEntity
import com.example.test.data.local.database.entities.PokemonEntity

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon")
    suspend fun getAll(): List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): PokemonEntity?

    @Insert
    suspend fun insertCapture(entity: CapturedPokemonEntity)

    @Query("SELECT * FROM captured_pokemon ORDER BY capturedAt DESC")
    suspend fun getAllCaptured(): List<CapturedPokemonEntity>

    @Query("DELETE FROM captured_pokemon WHERE name = :name")
    suspend fun deleteCapturedPokemon(name: String)
}