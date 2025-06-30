package com.example.test.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.test.ui.pokemon.PokemonDetailScreen
import com.example.test.ui.pokemon.PokemonListScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route
    ) {
        composable(route = Screen.PokemonList.route) {
            PokemonListScreen(navController = navController)
        }
        composable(route = Screen.PokemonDetail.route) { backStackEntry ->
            val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: ""
            PokemonDetailScreen(
                pokemonName = pokemonName,
                navController = navController
            )
        }
    }
}
