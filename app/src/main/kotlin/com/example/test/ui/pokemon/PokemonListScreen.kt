package com.example.test.ui.pokemon

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues // Added for LazyColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.test.R
import com.example.test.domain.model.PokemonItem
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel() // Inject ViewModel
) {
    val state = viewModel.state.collectAsState()
    PokemonListContent(
        navController = navController,
        state = state.value,
        eventHandler = viewModel::process
    )
}

@Composable
fun PokemonListContent(
    navController: NavController,
    state: PokemonListViewState,
    eventHandler: (PokemonListEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {
            Text(text = "Error: ${state.error}")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    // My Pocket (Captured)
                    if (state.capturedList.isNotEmpty()) {
                        Row {
                            Text(
                                text = "My Pocket",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "${state.capturedList.size}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            // contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            items(state.capturedList) { pokemon ->
                                PokemonCard(
                                    pokemon = pokemon,
                                    isFromMyPocket = true,
                                    eventHandler = eventHandler
                                ) {
                                    navController.navigate("pokemonDetail/${pokemon.name}")
                                }
                            }
                        }
                    }
                }

                val groupedPokemonsByType = state.groupedPokemonByTypes

                groupedPokemonsByType.forEach { (type, list) ->
                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = type.replaceFirstChar { it.uppercaseChar() },
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = list.size.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(list) { pokemon ->
                                PokemonCard(
                                    pokemon = pokemon,
                                    eventHandler = eventHandler
                                ) {
                                    navController.navigate("pokemonDetail/${pokemon.name}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonCard(
    pokemon: PokemonItem,
    isFromMyPocket: Boolean = false,
    eventHandler: (PokemonListEvent) -> Unit,
    onClick: () -> Unit
) {
    var showAnimation by remember { mutableStateOf(false) }
    var showReleaseAnimation by remember { mutableStateOf(false) }

    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { onClick() } // Entire card (excluding icon) navigates
                .padding(8.dp)
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = pokemon.name,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_poke_ball),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .clickable {
                    if (isFromMyPocket) {
                        showReleaseAnimation = true
                        eventHandler(PokemonListEvent.ReleasePokemon(pokemon))
                    } else {
                        showAnimation = true
                        eventHandler(PokemonListEvent.CapturePokemon(pokemon))
                    }
                },
            tint = Color.Red
        )

        if (showAnimation) {
            CaptureAnimation(isCaptured = true) {
                showAnimation = false
            }
        }
        if (showReleaseAnimation) {
            ReleaseAnimation {
                showReleaseAnimation = false
            }
        }
    }
}

@Composable
fun CaptureAnimation(isCaptured: Boolean, onAnimationEnd: () -> Unit) {
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(isCaptured) {
        if (isCaptured) {
            scale.animateTo(
                targetValue = 1.5f,
                animationSpec = tween(durationMillis = 200, easing = LinearOutSlowInEasing)
            )
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 300)
            )
            onAnimationEnd()
            scale.snapTo(1f)
            alpha.snapTo(1f)
        }
    }

    if (isCaptured) {
        Icon(
            painter = painterResource(id = R.drawable.ic_poke_ball),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                },
            tint = Color.Red
        )
    }
}

@Composable
fun ReleaseAnimation(onAnimationEnd: () -> Unit) {
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 0.5f,
            animationSpec = tween(durationMillis = 200, easing = LinearOutSlowInEasing)
        )
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 300)
        )
        onAnimationEnd()
        scale.snapTo(1f)
        alpha.snapTo(1f)
    }

    Icon(
        painter = painterResource(id = R.drawable.ic_poke_ball),
        contentDescription = null,
        modifier = Modifier
            .size(48.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        tint = Color.Gray
    )
}



