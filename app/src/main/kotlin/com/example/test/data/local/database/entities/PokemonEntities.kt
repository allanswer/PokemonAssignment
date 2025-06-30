package com.example.test.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.test.domain.model.PokemonDetails
import com.example.test.domain.model.PokemonItem

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val name: String,
    val id: Int,
    val imageUrl: String,
    val types: List<String>,
    val description: String,
    val evolvesFrom: String? = null,
    val isCaptured: Boolean = false
    // Add other fields as necessary
)

fun PokemonEntity.toPokemonItem() = PokemonItem(
    name = name.replaceFirstChar { it.uppercase() },
    imageUrl = imageUrl,
    types = types,
    id = id,
)

fun PokemonEntity.toPokemonDetails() = PokemonDetails(
    name = name.replaceFirstChar { it.uppercase() },
    imageUrl = imageUrl,
    types = types,
    description = description,
    evolvesFromName = evolvesFrom?.replaceFirstChar { it.uppercase() },
    isCaptured = isCaptured,
    id = id
)


