package com.voynex.app.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voynex.app.domain.model.monthLength
import com.voynex.app.ui.common.ViewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minusMonth
import kotlinx.datetime.plus
import kotlinx.datetime.plusMonth
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import voynex.composeapp.generated.resources.Res
import voynex.composeapp.generated.resources.add
import voynex.composeapp.generated.resources.minus
import voynex.composeapp.generated.resources.family_restroom
import voynex.composeapp.generated.resources.group
import voynex.composeapp.generated.resources.directions_walk
import voynex.composeapp.generated.resources.directions_run
import voynex.composeapp.generated.resources.bolt
import voynex.composeapp.generated.resources.bed
import voynex.composeapp.generated.resources.couple
import voynex.composeapp.generated.resources.hotel
import voynex.composeapp.generated.resources.diamond
import voynex.composeapp.generated.resources.flight
import voynex.composeapp.generated.resources.train
import voynex.composeapp.generated.resources.directions_car
import voynex.composeapp.generated.resources.directions_bus
import voynex.composeapp.generated.resources.ramen_dining
import voynex.composeapp.generated.resources.eco
import voynex.composeapp.generated.resources.next
import voynex.composeapp.generated.resources.previous
import voynex.composeapp.generated.resources.restaurant
import voynex.composeapp.generated.resources.solo
import voynex.composeapp.generated.resources.vegan
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@Composable
fun DestinationScreen(destination:String,viewModelFactory: ViewModelFactory) {
    val viewModel: DestinationViewModel = viewModel(factory = viewModelFactory)

    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }

        uiState.error != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Something went wrong.\n${uiState.error}")
        }

        else -> ImageCarousel(uiState.images)
    }

    LaunchedEffect(Unit) {
        viewModel.loadImages(destination)
    }
}

@Composable
fun ImageCarousel(images: List<String>) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(images) {
        if (images.isEmpty()) return@LaunchedEffect

        while (true) {
            delay(3000) // Wait for 3 seconds
            if (!state.isScrollInProgress) {
                val currentVisibleIndex = state.firstVisibleItemIndex
                val nextIndex = (currentVisibleIndex + 1) % images.size
                scope.launch {
                    state.animateScrollToItem(nextIndex)
                }
            }
        }
    }
    val cardWidth = GetScreenSizeInDp().first

    LazyRow(
        state = state,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        itemsIndexed(images) { i,image ->
            Card(modifier = Modifier.height(300.dp).width(cardWidth)) {
                KamelImage(
                    resource = {
                        asyncPainterResource(data = image)
                    },
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().aspectRatio(.5f),
                    contentScale = ContentScale.Fit,
                    onLoading = {
                        CircularProgressIndicator()
                    },
                    onFailure = {exception->
                        Text(text = "Failed to load image, $exception")
                    },
                    animationSpec = tween(durationMillis = 300)
                )
            }
        }
    }
}

@Composable
fun ChoiceCard(
    text: String,
    icon: Painter,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    )

    val height by animateDpAsState(if (selected) 110.dp else 100.dp)
    Card(
        modifier = Modifier
            .padding(6.dp)
            .height(height)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .border(
                2.dp,
                if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(bg),
        elevation = CardDefaults.cardElevation(if (selected) 8.dp else 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(if (selected) 52.dp else 42.dp)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                // 🔥 text visible now
            )
        }
    }
}


@OptIn(ExperimentalTime::class)
@Composable
fun PlanTripScreen(
    destination: String,
    factory: ViewModelFactory,
    onGeneratePlan: (TripInput) -> Unit
) {
    var startDate by remember { mutableStateOf<LocalDate>(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date) }
    var endDate by remember { mutableStateOf<LocalDate>(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.plus(7,DateTimeUnit.DAY)) }
    var travellerType by remember { mutableStateOf("") }
    var paceOfTravel by remember { mutableStateOf("") }
    var accommodationPreference by remember { mutableStateOf("") }
    var transportationPreference by remember { mutableStateOf("") }
    var foodPreference by remember { mutableStateOf("") }
    var budget by remember { mutableFloatStateOf(6000f) }
    var notes by remember { mutableStateOf("") }

    val preferences = remember { mutableStateListOf<String>() }

    val allPreferences = listOf(
        "Beaches", "Nature", "Adventure", "Food", "Photography",
        "Shopping", "Nightlife", "Heritage", "Theme Parks"
    )

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    onGeneratePlan(
                        TripInput(
                            destination, 
                            departureDate = startDate.toString(), 
                            arrivalDate = endDate.toString(), 
                            travellerType, 
                            budget.toInt(),
                            preferences.toList(), 
                            notes, 
                            paceOfTravel, 
                            accommodationPreference,
                            transportationPreference, 
                            foodPreference
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.navigationBars.asPaddingValues())
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("✨ Generate My Travel Plan")
            }
        }
    ) { padding ->

        Column(Modifier.fillMaxSize()) {
            DestinationScreen(destination,factory)
            Column(
                modifier = Modifier
//                .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(16.dp))
                Text("Plan Your Trip to $destination",  fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))

                Spacer(Modifier.height(8.dp))
                PremiumDateRangePicker(
                    initialStart = startDate,
                    initialEnd = endDate,
                    onDateRangeSelected = { s, e ->
                        startDate = s
                        endDate = e
                    }
                )

                Spacer(Modifier.height(22.dp))

                // Traveller type
                Text("Traveller Type", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                val types = listOf(
                    "Solo" to Res.drawable.solo,
                    "Couple" to Res.drawable.couple,
                    "Family" to Res.drawable.family_restroom,
                    "Friends" to Res.drawable.group
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    types.forEach { (type, icon) ->
                        ChoiceCard(type, painterResource(icon), travellerType == type) { travellerType = type }
                    }
                }

                Spacer(Modifier.height(22.dp))

                // Pace of Travel
                Text("Pace of Travel", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                val paces = listOf(
                    "Relaxed" to Res.drawable.directions_walk,
                    "Normal" to Res.drawable.directions_run,
                    "Packed" to Res.drawable.bolt
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    paces.forEach { (pace, icon) ->
                        ChoiceCard(pace, painterResource(icon), paceOfTravel == pace) { paceOfTravel = pace }
                    }
                }

                Spacer(Modifier.height(22.dp))

                // Accommodation Preference
                Text("Accommodation Preference", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                val accommodations = listOf(
                    "Budget" to Res.drawable.bed,
                    "Mid-range" to Res.drawable.hotel,
                    "Luxury" to Res.drawable.diamond
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    accommodations.forEach { (accommodation, icon) ->
                        ChoiceCard(accommodation, painterResource(icon), accommodationPreference == accommodation) { accommodationPreference = accommodation }
                    }
                }

                Spacer(Modifier.height(22.dp))

                // Transportation
                Text("Transportation", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                val transports = listOf(
                    "Flights" to Res.drawable.flight,
                    "Trains" to Res.drawable.train,
                    "Rental Car" to Res.drawable.directions_car,
                    "Public Transport" to Res.drawable.directions_bus
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    transports.forEach { (transport, icon) ->
                        ChoiceCard(transport, painterResource(icon), transportationPreference == transport) { transportationPreference = transport }
                    }
                }

                Spacer(Modifier.height(22.dp))

                // Food Preference
                Text("Food Preference", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                val foods = listOf(
                    "Local Cuisine" to Res.drawable.ramen_dining,
                    "Vegetarian" to Res.drawable.eco,
                    "Vegan" to Res.drawable.vegan,
                    "Fine Dining" to Res.drawable.restaurant
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    foods.forEach { (food, icon) ->
                        ChoiceCard(food, painterResource(icon), foodPreference == food) { foodPreference = food }
                    }
                }

                Spacer(Modifier.height(22.dp))

                // Budget
                Text("Budget per Person (₹${budget.toInt()})", fontWeight = FontWeight.SemiBold)
                Slider(
                    value = budget,
                    onValueChange = {
                        val snappedValue = (it / 100).roundToInt() * 100f
                        budget = snappedValue
                    },
                    valueRange = 2000f..30000f
                )

                Spacer(Modifier.height(22.dp))

                // Preferences
                Text("Preferences", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    allPreferences.forEach { pref ->
                        FilterChip(
                            selected = preferences.contains(pref),
                            onClick = {
                                if (preferences.contains(pref)) preferences.remove(pref)
                                else preferences.add(pref)
                            },
                            label = { Text(pref) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,      // 🔥 background when selected
                                selectedLabelColor = Color.White,                                   // text when selected
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,          // normal background
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant             // normal label
                            )
                        )

                    }
                }

                Spacer(Modifier.height(22.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Special Requests (optional)") },
                    minLines = 3
                )
                Spacer(Modifier.height(90.dp)) // to leave space above CTA
            }
        }

    }
}

@Serializable
data class TripInput(
    val destination: String,
    val departureDate: String,
    val arrivalDate: String,
    val travellerType: String,
    val budget: Int,
    val preferences: List<String>,
    val notes: String,
    val paceOfTravel: String,
    val accommodationPreference: String,
    val transportation: String,
    val foodPreference: String
)

@Composable
fun NumberSelector(title: String, value: Int, onValueChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (value > 1) onValueChange(value - 1) }) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(Res.drawable.minus),
                    contentDescription = "App Image",
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }
            Text("$value", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { onValueChange(value + 1) }) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(Res.drawable.add),
                    contentDescription = "App Image",
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)

                )
            }
        }
    }
}

@Composable
fun PremiumDateRangePicker(
    initialStart: LocalDate,
    initialEnd: LocalDate,
    onDateRangeSelected: (LocalDate, LocalDate) -> Unit
) {
    var start by remember { mutableStateOf(initialStart) }
    var end by remember { mutableStateOf(initialEnd) }
    var showPicker by remember { mutableStateOf(false) }

    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DateCard(modifier =  Modifier.weight(1f)
                .wrapContentHeight(),"Departure", start) { showPicker = true }
            DateCard(modifier =  Modifier.weight(1f)
                .wrapContentHeight(),"Return", end) { showPicker = true }
        }
    }

    if (showPicker) {
        DatePickerDialog(
            initialStart = start,
            initialEnd = end,
            onDismiss = { showPicker = false },
            onApply = { s, e ->
                if (s != null) {
                    start = s
                }
                if (e != null) {
                    end = e
                }else{
                    end = start.plus(1, DateTimeUnit.DAY)
                }
                if (s != null && e != null) {
                    onDateRangeSelected(s, e)
                }
                showPicker = false
            }
        )
    }
}

@Composable
private fun DateCard(modifier: Modifier,label: String, date: LocalDate?, onClick: () -> Unit) {
    Card(
        modifier =modifier.clickable{
            onClick.invoke()
        },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))

            if (date == null) {
                Text("Select", color = MaterialTheme.colorScheme.primary)
            } else {
                Text(
                    "${date.dayOfMonth} ${date.month.name.take(3)}, ${date.year}",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/* ----------------------------------------------------
   DATE PICKER DIALOG
----------------------------------------------------- */

@Composable
private fun DatePickerDialog(
    initialStart: LocalDate,
    initialEnd: LocalDate?,
    onDismiss: () -> Unit,
    onApply: (LocalDate?, LocalDate?) -> Unit
) {
    val today = initialStart
    var visibleMonth by remember { mutableStateOf(YearMonth(today.year, today.month)) }
    var start by remember { mutableStateOf(initialStart) }
    var end by remember { mutableStateOf(initialEnd) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 8.dp
        ) {
            Column(Modifier.padding(20.dp)) {

                /* ----------- Month Navigation ----------- */
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { visibleMonth = visibleMonth.minusMonth() }) {
                        Image(painter = painterResource(Res.drawable.previous), contentDescription = "Next",
                            contentScale = ContentScale.FillBounds, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(visibleMonth.month.name, fontWeight = FontWeight.Bold)
                        Text("${visibleMonth.year}", fontSize = 12.sp)
                    }

                    IconButton(onClick = { visibleMonth = visibleMonth.plusMonth() }) {
                        Image(painter = painterResource(Res.drawable.next), contentDescription = "Next",
                            contentScale = ContentScale.FillBounds, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )

                    }
                }

                Spacer(Modifier.height(12.dp))

                WeekHeader()
                MonthGrid(
                    yearMonth = visibleMonth,
                    startDate = start,
                    endDate = end
                ) { clicked ->
                    if (end != null) {
                        start = clicked
                        end = null
                    } else {
                        if (clicked < start!!) {
                            end = start
                            start = clicked
                        } else {
                            end = clicked
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                /* ----------- Footer ----------- */
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                   horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column {
                        if (start == null) Text("No dates")
                        else if (end == null) Text("Depart: $start")
                        else Text("$start → $end")
                    }

                    Row {
                        TextButton(onClick = onDismiss) { Text("Cancel") }
                        Button(onClick = { onApply(start, end) }, enabled = start != null) {
                            Text("Apply")
                        }
                    }
                }
            }
        }
    }
}

/* ----------------------------------------------------
   GRID + DAY CELL
----------------------------------------------------- */

@Composable
private fun WeekHeader() {
    val names = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(Modifier.fillMaxWidth()) {
        names.forEach {
            Text(
                it,
                Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun MonthGrid(
    yearMonth: YearMonth,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDayClick: (LocalDate) -> Unit
) {
    val daysInMonth = monthLength(yearMonth.year, yearMonth.month.name)

    val firstDay = LocalDate(yearMonth.year, yearMonth.month, 1)
    val dayOfWeekIndex = firstDay.dayOfWeek.isoDayNumber % 7 // Sun = 0

    val totalCells = dayOfWeekIndex + daysInMonth
    val rows = (totalCells + 6) / 7

    Column {
        for (row in 0 until rows) {
            Row(Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val index = row * 7 + col
                    val dayNumber = index - dayOfWeekIndex + 1

                    Box(
                        Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dayNumber in 1..daysInMonth) {
                            val daysInMonth = yearMonth.numberOfDays // <-- Get real days of month

                            if (dayNumber in 1..daysInMonth) {
                                val date = LocalDate(yearMonth.year, yearMonth.month, dayNumber) // 🔥 Safe now
                                DayCell(date, startDate, endDate) { onDayClick(date) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    start: LocalDate?,
    end: LocalDate?,
    onClick: () -> Unit
) {
    val isStart = date == start
    val isEnd = date == end
    val isInRange = start != null && end != null && date >= start && date <= end

    val bg = animateColorAsState(
        when {
            isStart || isEnd -> MaterialTheme.colorScheme.primary
            isInRange -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            else -> Color.Transparent
        }
    )

    val textColor =
        if (isStart || isEnd) MaterialTheme.colorScheme.onPrimary
        else if (isInRange) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurface

    Box(
        Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(bg.value)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text("${date.dayOfMonth}", color = textColor)
    }
}
