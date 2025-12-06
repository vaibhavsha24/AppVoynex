package com.voynex.app.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voynex.app.domain.model.ChatMessage
import com.voynex.app.domain.model.Itinerary
import com.voynex.app.domain.usecase.Category
import com.voynex.app.domain.usecase.HomeDestination
import com.voynex.app.preferences.SharedPref
import com.voynex.app.ui.common.ViewModelFactory
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.painterResource
import voynex.composeapp.generated.resources.Res
import voynex.composeapp.generated.resources.home_tab
import voynex.composeapp.generated.resources.mic
import voynex.composeapp.generated.resources.send
import voynex.composeapp.generated.resources.voice
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun HomeScreen(onDestinationClick: (String) -> Unit, factory: ViewModelFactory,onCategoryClick:(String) ->Unit,onSavedItineraryClick:(Itinerary)->Unit) {
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val chatViewModel: ChatViewModel = viewModel(factory = factory)
    var selectedIndex by remember { mutableStateOf(0) }

    val chatMessages = chatViewModel.uiState.collectAsState().value.messages

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
            ErrorScreen {
                viewModel.loadHomeContent()
            }
        } else {
            Scaffold(
                topBar = {
                    if(selectedIndex ==0) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(WindowInsets.statusBars.asPaddingValues())
                                .padding(top = 16.dp, start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Explore", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                Text("your next destination", fontSize = 16.sp, color = Color.Gray)
                            }
                        }
                    }

                }
            ) { padding ->

                Box(
                    Modifier
                        .fillMaxSize()
                ) {
                    when (selectedIndex){
                        0-> HomeTab(padding, categories = uiState.categories, savedItinerary = uiState.savedItinerary, destinations = uiState.destinations,onDestinationClick,onCategoryClick, onSavedItineraryClick = { dest->
                            val itinerary = viewModel.getItinerary(dest)
                            itinerary?.let {
                                onSavedItineraryClick(it)
                            }
                        })

                        1 -> ChatPage(padding, messages = chatMessages){ userInput->
                            chatViewModel.sendMessage(userInput)
                        }
                    }
                    BottomNavBar(modifier= Modifier.align(Alignment.BottomCenter),selectedIndex,){
                        selectedIndex = it
                    }

                }

            }
        }
    }

}
@Composable
fun BottomNavBar(
    modifier: Modifier,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Box(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .clip(RoundedCornerShape(36.dp))
                .background(Color.White.copy(alpha = 0.12f))
                .shadow(18.dp, RoundedCornerShape(36.dp))
                .padding(horizontal = 22.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedTabIcon(
                index = 0,
                selectedIndex = selectedIndex,
                icon = painterResource(Res.drawable.home_tab),
                onClick = { onSelect(0) }
            )

            AnimatedTabIcon(
                index = 1,
                selectedIndex = selectedIndex,
                icon = painterResource(Res.drawable.voice),
                onClick = { onSelect(1) }
            )
        }
    }
}
@Composable
fun AnimatedTabIcon(
    index: Int,
    selectedIndex: Int,
    icon: Painter,
    onClick: () -> Unit,
) {
    val isSelected = index == selectedIndex

    val scale = animateFloatAsState(if (isSelected) 1.25f else 1f)
    val bgColor = animateColorAsState(
        if (isSelected) Color(0xFF4F46E5) else Color.Transparent
    )

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor.value)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size((26 * scale.value).dp),
            colorFilter = ColorFilter.tint(
                if (isSelected) Color.White else Color.LightGray
            )
        )
    }
}


@Composable
fun TabIcon(
    index: Int,
    selectedIndex: Int,
    icon: Painter,
    onClick: () -> Unit
) {
    val isSelected = index == selectedIndex

    Box(
        modifier = Modifier
            .size(46.dp) // radius circle area
            .clip(CircleShape)
            .background(
                if (isSelected) Color(0xFF448AFF).copy(alpha = 0.95f) // Blue highlight
                else Color.Transparent
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(if (isSelected) 30.dp else 26.dp), // icon grows slightly
            colorFilter = ColorFilter.tint(
                if (isSelected) Color.White else Color.LightGray
            )
        )
    }
}

@Composable
fun HomeTab(padding: PaddingValues, categories:List<Category>, savedItinerary:List<Category>, destinations: List<HomeDestination>, onDestinationClick: (String) -> Unit, onCategoryClick:(String) ->Unit, onSavedItineraryClick:(String)->Unit){
    var searchQuery by remember { mutableStateOf("") }
    val screenWidth = GetScreenSizeInDp().first    // 70% of screen width
    val cardHeight = screenWidth
    LazyColumn(
        modifier = Modifier.padding(padding).padding(horizontal = 16.dp).padding(bottom = 72.dp),
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
            if(savedItinerary.isNotEmpty()) {
                Text(
                    "Saved Itinerary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(savedItinerary) { savedItinerary ->
                        CategoryCard(modifier = Modifier
                            .height(cardHeight*0.6f)
                            .padding(end = 16.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(32.dp),
                                ambientColor = Color(0x22000000),
                                spotColor = Color(0x22000000)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable(onClick ={
                                onSavedItineraryClick.invoke(savedItinerary.name)
                            }),savedItinerary,screenWidth*0.9f,cardHeight*0.6f)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
        item {
            Text("Categories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(categories) { category ->
                    CategoryCard(modifier = Modifier
                        .height(cardHeight * 0.55f)
                        .padding(end = 16.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(32.dp),
                            ambientColor = Color(0x22000000),
                            spotColor = Color(0x22000000)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .clickable {
                            onCategoryClick.invoke(category.name)
                        },category,screenWidth* 0.6f ,cardHeight * 0.55f)

                }
            }
            Spacer(Modifier.height(16.dp))
        }
        item{
            Text("Top Recommendation for You", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        }
        items(destinations) { destination ->
            DestinationCard(destination, onDestinationClick)
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

    val progress by animateLottieCompositionAsState(composition,
        iterations = Int.MAX_VALUE
    )
    Image(
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress },
        ),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        contentDescription = "Lottie animation",
        modifier = Modifier.size(20.dp)
    )
}


@Composable
fun CategoryCard(modifier: Modifier,category: Category,cardWidth:Dp,cardHeight:Dp) {

    Card(
        modifier =  modifier,
        shape = RoundedCornerShape(32.dp)
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
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomStart).padding(32.dp)
            )
        }
    }
}

@Composable
fun DestinationCard(destination: HomeDestination, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clickable { onClick(destination.name) },
        shape = RoundedCornerShape(32.dp)
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
                modifier = Modifier.align(Alignment.BottomStart).padding(32.dp)
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
@OptIn(ExperimentalUuidApi::class)
@Composable
fun ChatPage(padding: PaddingValues,messages: List<ChatMessage>,sendMessage:(String)->Unit, ) {
    var inputText by remember { mutableStateOf("") }

    ChatScreen(
        messages = messages,
        onSendMessage = { msg ->
            sendMessage(msg)// this triggers when user sends

        },
        modifier =  Modifier.padding(padding).padding(bottom = 72.dp)
    )
}
@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var input by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()

    ) {
        // Main scaffold
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = { ChatTopBar() },
            bottomBar = {
                ChatInputBar(
                    text = input,
                    onTextChange = { input = it },
                    onSendClick = {
                        if (input.isNotBlank()) {
                            onSendMessage(input.trim())
                            input = ""
                        }
                    },
                    onMicClick = { /* TODO: open voice */ }
                )
            }
        ) { innerPadding ->

            ChatMessagesList(
                messages = messages,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatTopBar() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.06f))
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.12f),
                    RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4F46E5))
            )

            Column(Modifier.weight(1f)) {
                Text(
                    "Voynex AI",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    "Online • Responding instantly",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

        }
    }
}
@Composable
fun ChatMessagesList(
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messages, key = { it.id }) { msg ->
            ChatBubble(message = msg)
        }
        item { Spacer(Modifier.height(72.dp)) } // space above input bar
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isMine) Color(0xFF4F46E5) else Color.White.copy(alpha = 0.08f)
    val textColor = if (message.isMine) Color.White else Color.White
    val alignment = if (message.isMine) Alignment.Start else Alignment.End
    val shape = if (message.isMine) {
        RoundedCornerShape(18.dp, 4.dp, 18.dp, 18.dp)
    } else {
        RoundedCornerShape(4.dp, 18.dp, 18.dp, 18.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(bubbleColor, shape)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (message.time.isNotBlank()) {
            Text(
                text = message.time,
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .align(alignment)
            )
        }
    }
}


@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onMicClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.06f))
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.12f),
                    RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMicClick) {
                Image(
                    painter = painterResource(Res.drawable.mic),  // <-- your send.png/svg
                    contentDescription = "Send",
                    modifier = Modifier
                        .size(20.dp)   // adjust icon size
                        .padding(2.dp),
                    colorFilter = ColorFilter.tint(Color.White) // apply tint if needed
                )
            }

            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Ask anything…",
                        color = Color.White.copy(alpha = 0.5f)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                maxLines = 4
            )

            IconButton(
                onClick = onSendClick,
                enabled = text.isNotBlank()
            ) {
                Box(
                    Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (text.isNotBlank()) Color(0xFF4F46E5) else Color.White.copy(alpha = 0.2f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.send),  // <-- your send.png/svg
                        contentDescription = "Send",
                        modifier = Modifier
                            .size(20.dp)   // adjust icon size
                            .padding(2.dp),
                        colorFilter = ColorFilter.tint(Color.White) // apply tint if needed
                    )
                }
            }
        }
    }
}

