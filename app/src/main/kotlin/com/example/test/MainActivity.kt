package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.ui.pokemon.PokemonDetailScreen
import com.example.test.ui.pokemon.PokemonListScreen
import com.example.test.ui.theme.TestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "pokemonList",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("pokemonList") {
                            PokemonListScreen(navController = navController)
                        }
                        composable("pokemonDetail/{pokemonName}") { backStackEntry ->
                            PokemonDetailScreen(pokemonName = backStackEntry.arguments?.getString("pokemonName"))
                        }
                    }
                }
            }
        }
    }
}
