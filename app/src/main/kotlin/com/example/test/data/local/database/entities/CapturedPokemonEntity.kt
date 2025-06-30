package com.example.test.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.test.domain.model.PokemonItem

@Entity(tableName = "captured_pokemon")
data class CapturedPokemonEntity(
    @PrimaryKey(autoGenerate = true) val captureId: Int = 0,
    val name: String,
    val capturedAt: Long = System.currentTimeMillis(),
    val imageUrl: String,
)

fun CapturedPokemonEntity.toPokemonItem(): PokemonItem {
    return PokemonItem(
        name = name,
        imageUrl = imageUrl,
        capturedAt = capturedAt,
    )
}