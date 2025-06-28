package com.example.test.data

import com.squareup.moshi.Json

// --- Data Classes for API Responses ---

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItem>
)

data class PokemonListItem(
    val name: String,
    val url: String
)

data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    @Json(name = "base_experience") val baseExperience: Int,
    val height: Int,
    @Json(name = "is_default") val isDefault: Boolean,
    val order: Int,
    val weight: Int,
    val abilities: List<PokemonAbility>,
    val forms: List<NamedApiResource>,
    @Json(name = "game_indices") val gameIndices: List<GameIndex>,
    @Json(name = "held_items") val heldItems: List<HeldItem>,
    @Json(name = "location_area_encounters") val locationAreaEncounters: String,
    val moves: List<PokemonMove>,
    val species: NamedApiResource,
    val sprites: PokemonSprites,
    val cries: Cries,
    val stats: List<PokemonStat>,
    val types: List<PokemonType>,
    @Json(name = "past_types") val pastTypes: List<PokemonPastType>,
    @Json(name = "past_abilities") val pastAbilities: List<PokemonPastAbility>
)

data class PokemonAbility(
    @Json(name = "is_hidden") val isHidden: Boolean,
    val slot: Int,
    val ability: NamedApiResource
)

data class GameIndex(
    @Json(name = "game_index") val gameIndex: Int,
    val version: NamedApiResource
)

data class HeldItem(
    val item: NamedApiResource,
    @Json(name = "version_details") val versionDetails: List<VersionDetail>
)

data class VersionDetail(
    val rarity: Int,
    val version: NamedApiResource
)

data class PokemonMove(
    val move: NamedApiResource,
    @Json(name = "version_group_details") val versionGroupDetails: List<VersionGroupDetail>
)

data class VersionGroupDetail(
    @Json(name = "level_learned_at") val levelLearnedAt: Int,
    @Json(name = "version_group") val versionGroup: NamedApiResource,
    @Json(name = "move_learn_method") val moveLearnMethod: NamedApiResource
)

data class PokemonSprites(
    @Json(name = "front_default") val frontDefault: String?,
    @Json(name = "back_default") val backDefault: String?,
    @Json(name = "front_shiny") val frontShiny: String?,
    @Json(name = "back_shiny") val backShiny: String?
    // Add `other` and `versions` if needed
)

data class Cries(
    val latest: String?,
    val legacy: String?
)

data class PokemonStat(
    @Json(name = "base_stat") val baseStat: Int,
    val effort: Int,
    val stat: NamedApiResource
)

data class PokemonType(
    val slot: Int,
    val type: NamedApiResource
)

data class PokemonPastType(
    val generation: NamedApiResource,
    val types: List<PokemonType>
)

data class PokemonPastAbility(
    val generation: NamedApiResource,
    val abilities: List<PokemonPastAbilityDetail>
)

data class PokemonPastAbilityDetail(
    val ability: NamedApiResource?,
    @Json(name = "is_hidden") val isHidden: Boolean,
    val slot: Int
)

data class NamedApiResource(
    val name: String,
    val url: String
)



data class PokemonSpeciesResponse(
    val id: Int,
    val name: String,
    @Json(name = "order") val order: Int,
    @Json(name = "gender_rate") val genderRate: Int,
    @Json(name = "capture_rate") val captureRate: Int,
    @Json(name = "base_happiness") val baseHappiness: Int,
    @Json(name = "is_baby") val isBaby: Boolean,
    @Json(name = "is_legendary") val isLegendary: Boolean,
    @Json(name = "is_mythical") val isMythical: Boolean,
    @Json(name = "hatch_counter") val hatchCounter: Int,
    @Json(name = "has_gender_differences") val hasGenderDifferences: Boolean,
    @Json(name = "forms_switchable") val formsSwitchable: Boolean,
    @Json(name = "growth_rate") val growthRate: NamedApiResource,
    val pokedex_numbers: List<PokedexNumber>,
    val egg_groups: List<NamedApiResource>,
    @Json(name = "color") val color: NamedApiResource,
    @Json(name = "shape") val shape: NamedApiResource?,
    @Json(name = "evolves_from_species") val evolvesFromSpecies: NamedApiResource?,
    @Json(name = "evolution_chain") val evolutionChain: ApiResource?,
    @Json(name = "habitat") val habitat: NamedApiResource?,
    @Json(name = "generation") val generation: NamedApiResource,
    val names: List<Name>,
    @Json(name = "flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>,
    @Json(name = "form_descriptions") val formDescriptions: List<Description>,
    @Json(name = "genera") val genera: List<Genus>
)

data class ApiResource(
    val url: String
)

data class PokedexNumber(
    @Json(name = "entry_number") val entryNumber: Int,
    @Json(name = "pokedex") val pokedex: NamedApiResource
)

data class Name(
    val name: String,
    @Json(name = "language") val language: NamedApiResource
)

data class FlavorTextEntry(
    @Json(name = "flavor_text") val flavorText: String,
    @Json(name = "language") val language: NamedApiResource,
    @Json(name = "version") val version: NamedApiResource
)

data class Description(
    val description: String,
    @Json(name = "language") val language: NamedApiResource
)

data class Genus(
    val genus: String,
    @Json(name = "language") val language: NamedApiResource
)

