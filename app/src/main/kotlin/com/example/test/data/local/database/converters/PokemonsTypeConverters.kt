package com.example.test.data.local.database.converters

import androidx.room.TypeConverter

class PokemonsTypeConverters {
    @TypeConverter
    fun fromList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toList(value: String): List<String> = value.split(",")
}