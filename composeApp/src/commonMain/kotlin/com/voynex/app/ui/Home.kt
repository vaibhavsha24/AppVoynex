package com.voynex.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voynex.app.domain.usecase.Category
import com.voynex.app.domain.usecase.HomeDestination
import com.voynex.app.ui.common.ViewModelFactory
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.painterResource
import voynex.composeapp.generated.resources.Res

@Composable
fun HomeScreen(onDestinationClick: (String) -> Unit, factory: ViewModelFactory,onCategoryClick:(String) ->Unit) {
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

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

                    item {
                        Text("Categories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(uiState.categories) { category ->
                                CategoryCard(category, onCategoryClick)
                            }
                        }
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

}


@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color(0x33000000),
                spotColor = Color(0x33000000)
            ),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "Search Next Destination",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            trailingIcon = {
                SearchLottieIcon()
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(value) }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}


@Composable
fun SearchLottieIcon(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/search.json").decodeToString()
        )
    }

    val progress by animateLottieCompositionAsState(composition)

    Image(
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress },
        ),
        contentDescription = "Lottie animation"
    )
}

@Composable
fun CategoryCard(category: Category, onClick: (String) -> Unit) {
    val cardWidth = GetScreenSizeInDp().first * 0.6f    // 70% of screen width
    val cardHeight = cardWidth * 0.55f
    Card(
        modifier =  Modifier
            .height(cardHeight)
            .padding(end = 16.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color(0x22000000),
                spotColor = Color(0x22000000)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick(category.name)

    },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier) {
            KamelImage(
                resource = asyncPainterResource(category.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(cardWidth).height(cardHeight)
                ,
                onLoading = { CircularProgressIndicator() }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 200f
                        )
                    )
            )
            Text(
                text = category.name,
                color = MaterialTheme.colorScheme.surface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomCenter).padding(12.dp)
            )
        }
    }
}

@Composable
fun DestinationCard(destination: HomeDestination, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick(destination.name) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            KamelImage(
                resource = asyncPainterResource(destination.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                onLoading = { CircularProgressIndicator() }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 400f
                        )
                    )
            )
            Text(
                text = destination.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            )
        }
    }
}
@Composable
fun GetScreenSizeInDp(): Pair<Dp, Dp> {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    val screenWidthPx = windowInfo.containerSize.width
    val screenHeightPx = windowInfo.containerSize.height

    // Convert pixels to Dp using the current density
    val screenWidthDp = with(density) { screenWidthPx.toDp() }
    val screenHeightDp = with(density) { screenHeightPx.toDp() }

    return Pair(screenWidthDp, screenHeightDp)
}


@Composable
fun LottieIcon(
    filePath: String,        // Example: "files/search.json"
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes(filePath).decodeToString()
        )
    }

    val progress by animateLottieCompositionAsState(composition,
        iterations = Int.MAX_VALUE
    )

    Image(
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress },
        ),
        contentDescription = null,
        modifier = modifier
    )
}
