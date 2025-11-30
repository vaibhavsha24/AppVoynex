package com.voynex.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voynex.app.ui.common.ViewModelFactory
import moe.tlaster.precompose.navigation.Navigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestedCountriesScreen(factory: ViewModelFactory, category: String, onDestinationClick:(String) ->Unit) {
    val viewModel: SuggestedCountryViewModel = viewModel(factory = factory)
    val uiState by viewModel.categoryUiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val selectedCategory = remember { category }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (uiState.loading) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LottieIcon(
                    filePath = "files/loader.json",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(Modifier.height(16.dp))
            }
        }else if (uiState.error != null) {
            Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)

        } else {
            Scaffold(
                topBar = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(WindowInsets.statusBars.asPaddingValues()).padding(top=16.dp,start=16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Explore", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            Text("your next destination", fontSize = 16.sp, color = Color.Gray)
                        }
                    }
                }
            ) { padding ->
                LazyColumn(
                    modifier = Modifier.padding(padding).padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(Modifier.height(16.dp))
                    }

                    item {
                        SearchBar(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            onSearch = { onDestinationClick(searchQuery) }
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    item{
                        Text("Top Recommendation for You", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                    }
                    items(uiState.destinations) { destination ->
                        DestinationCard(destination, onDestinationClick)
                    }
                }
            }
        }
    }

    var loaded by rememberSaveable{mutableStateOf(false)}

    LaunchedEffect(category) {
        if (!loaded) {
            viewModel.loadCategoryUiState(category)
            loaded = true
        }
    }
}

