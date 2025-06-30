package com.example.test.ui.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.test.R

@Composable
fun PokemonDetailScreen(
    pokemonName: String?,
    navController: NavController,
    viewModel: PokemonDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (state.error != null) {
        Text("Error: ${state.error}")
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            PokemonDetailsActionRow(
                state = state,
                navController = navController
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = state.pokemonDetails?.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(200.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = state.pokemonDetails?.name ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )

                // Types Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.pokemonDetails?.types?.forEach { type ->
                        Text(
                            text = type,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp) // Inner padding
                        )

                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
            ) {
                // Evolution Info
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.evolves_from),
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = state.pokemonDetails?.evolvesFromName ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    AsyncImage(
                        model = state.pokemonDetails?.evolvesFromImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(48.dp)
                            .clickable {
                                state.pokemonDetails?.evolvesFromName?.let { evolvesFromName ->
                                    navController.navigate("pokemonDetail/$evolvesFromName")
                                }
                            },
                        contentScale = ContentScale.Crop
                    )
                }

                // Description
                Text(
                    text = state.pokemonDetails?.description ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.Start)
                )
            }
        }
    }
}

@Composable
fun PokemonDetailsActionRow(
    state: PokemonDetailsViewState,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = state.pokemonDetails?.id.toString(),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

