package com.example.test.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.test.data.local.database.entities.PokemonEntity
import com.example.test.data.local.database.converters.PokemonsTypeConverters
import com.example.test.data.local.database.PokemonDao
import com.example.test.data.local.database.entities.CapturedPokemonEntity


@Database(
    entities = [PokemonEntity::class, CapturedPokemonEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PokemonsTypeConverters::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
