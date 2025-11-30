package com.voynex.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.voynex.app.domain.model.BudgetBreakdown
import com.voynex.app.domain.model.DaywiseItinerary
import com.voynex.app.domain.model.FoodRecommendation
import com.voynex.app.domain.model.HotelRecommendation
import com.voynex.app.domain.model.Itinerary
import com.voynex.app.domain.model.PackagingAndSafety
import com.voynex.app.domain.model.TimelineItem
import com.voynex.app.domain.model.TransportationPlan
import com.voynex.app.domain.model.TripSummary
import com.voynex.app.domain.usecase.GetSavedItinerary
import com.voynex.app.preferences.SharedPref
import com.voynex.app.ui.common.ViewModelFactory
import org.jetbrains.compose.resources.painterResource
import voynex.composeapp.generated.resources.Res
import voynex.composeapp.generated.resources.circle


@Composable
fun ItineraryScreen(tripInput: TripInput?, factory: ViewModelFactory,savedItineraryDestination: String?=null) {
    val viewModel: ItineraryViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if(tripInput!=null){
            viewModel.generateItinerary(tripInput)
        }else if (savedItineraryDestination!=null){
            viewModel.generateSavedItinerary(savedItineraryDestination)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            uiState.loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LottieIcon(
                        filePath = "files/loader.json",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text("Generating your itinerary...")
                }
            }
            uiState.error != null -> {
                ErrorScreen {
                    tripInput?.let {
                        viewModel.generateItinerary(tripInput)
                    }
                }
            }
            else -> {
                uiState.itinerary?.let {itinerary->
                    ItineraryScreenUI(itinerary, showSaveButton = savedItineraryDestination==null){
                        viewModel.saveOffline()
                    }
                }
            }
        }
    }
}
// ---------------------------------------------------------
//        MAIN SCREEN
// ------
@Composable
fun ItineraryScreenUI(itinerary: Itinerary,showSaveButton:Boolean,download:()->Unit,) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(top = 16.dp, start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Your Itinerary",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        bottomBar = {
            if(showSaveButton) {
                Button(
                    onClick = {
                        download.invoke()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(WindowInsets.navigationBars.asPaddingValues())
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Download Offline")
                }
            }
        }


    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                TripSummaryCard(itinerary.tripSummary)
            }

            item {
                GradientHeader("Day-wise Itinerary", "📅")
            }
            itemsIndexed(itinerary.daywiseItinerary) { _, day ->
                DayCard(day)
            }

            item {
                GradientHeader("Hotels", "🏨")
            }
            itemsIndexed(itinerary.hotelRecommendations) { _, hotel ->
                HotelCard(hotel)
            }

            item {
                GradientHeader("Transportation", "🚗")
                Spacer(Modifier.height(20.dp))
                TransportCard(itinerary.transportationPlan)
            }

            item {
                GradientHeader("Packing & Safety", "🎒")
                Spacer(Modifier.height(20.dp))
                PackingSafetyCard(itinerary.packagingAndSafety)
            }

            item {
                GradientHeader("Budget Breakdown", "💸")
                Spacer(Modifier.height(20.dp))
                BudgetCard(itinerary.budgetBreakdown)
            }
        }
    }
}


//----------------------------------------------------------
//                     HEADER
//----------------------------------------------------------

@Composable
fun GradientHeader(title: String, icon: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Text(
            text = "$icon  $title",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}


//----------------------------------------------------------
//                  TRIP SUMMARY CARD
//----------------------------------------------------------

@Composable
fun TripSummaryCard(s: TripSummary) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
    ) {
        Column(Modifier.padding(18.dp)) {

            Text(
                "📍 ${s.destination}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(10.dp))

            SummaryRow("⏳ Duration", s.tripDuration)
            SummaryRow("🧭 Travel Type", s.travelType)
            SummaryRow("💰 Budget", s.budgetLevel)
            SummaryRow("🌤 Best Time", s.bestTimeToVisit)
            SummaryRow("✨ Vibe", s.overallVibe)
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(2.dp))
        Text(value, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}


//----------------------------------------------------------
//                         DAY CARD
//----------------------------------------------------------

@Composable
fun DayCard(day: DaywiseItinerary) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(Modifier.padding(18.dp)) {

            Text(day.day, style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "🕒 ${day.startTime} - ${day.endTime}",
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(14.dp))
            Text("⭐ Highlights", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

            Spacer(Modifier.height(6.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                day.highlights.forEach { Chip(it) }
            }

            Spacer(Modifier.height(18.dp))
            Text("📍 Timeline", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(8.dp))

            day.timeline.forEach { TimelineItemView(it) }

            Spacer(Modifier.height(16.dp))
            Text("🍴 Food Recommendations", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(6.dp))

            day.foodRecommendations.forEach {
                FoodCard(it)
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}


//----------------------------------------------------------
//                           CHIP
//----------------------------------------------------------

@Composable
fun Chip(text: String) {
    Box(
        modifier = Modifier
            .padding(top = 6.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


//----------------------------------------------------------
//                   TIMELINE ITEM
//----------------------------------------------------------

@Composable
fun TimelineItemView(item: TimelineItem) {
    Row(Modifier.height(IntrinsicSize.Max).padding(vertical = 10.dp)) {

        // left connector
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                painter = painterResource(resource = Res.drawable.circle),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(10.dp)
            )

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(12.dp)
                )
                .padding(14.dp)
                .fillMaxWidth()
        ) {
            Text(item.time, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(item.activity, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(item.details, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("🚗 Travel: ${item.travelTime}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("💵 Cost: ${item.cost}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}


//----------------------------------------------------------
//                       FOOD CARD
//----------------------------------------------------------

@Composable
fun FoodCard(food: FoodRecommendation) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {

        Column(Modifier.padding(14.dp)) {

            Text("🍽 ${food.mealType}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text("📍 Place: ${food.place}", color = MaterialTheme.colorScheme.onSurface)
            Text("✨ Must Try: ${food.mustTry.joinToString()}", color = MaterialTheme.colorScheme.onSurface)
            Text("💰 Cost: ${food.approxCost}", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}


//----------------------------------------------------------
//                        HOTEL CARD
//----------------------------------------------------------

@Composable
fun HotelCard(hotel: HotelRecommendation) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(14.dp)) {

            Text("🏨 ${hotel.name}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text("💵 ${hotel.priceRange}", color = MaterialTheme.colorScheme.tertiary)
            Text("📍 ${hotel.location}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("⭐ ${hotel.whyRecommended}", color = MaterialTheme.colorScheme.onSurface)
        }
    }
}


//----------------------------------------------------------
//                 TRANSPORT CARD
//----------------------------------------------------------

@Composable
fun TransportCard(t: TransportationPlan) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {

        Column(Modifier.padding(16.dp)) {
            Text("Local Transport: ${t.localTransport}", color = MaterialTheme.colorScheme.onSurface)
            Text("Suggested Rentals: ${t.suggestedRentals.joinToString()}", color = MaterialTheme.colorScheme.onSurface)
            Text("Average Costs: ${t.averageCosts}", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}


//----------------------------------------------------------
//                  PACKING & SAFETY
//----------------------------------------------------------

@Composable
fun PackingSafetyCard(p: PackagingAndSafety) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {

        Column(Modifier.padding(16.dp)) {

            SafetyList("🧳 What to Pack", p.whatToPack)
            SafetyList("⚠️ Safety Tips", p.safetyTips)
            SafetyList("🚫 Scams to Avoid", p.scamsToAvoid)
        }
    }
}

@Composable
fun SafetyList(title: String, items: List<String>) {

    Text(
        title,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(Modifier.height(6.dp))

    items.forEach { Text("• $it", color = MaterialTheme.colorScheme.onSurface) }

    Spacer(Modifier.height(10.dp))
}


//----------------------------------------------------------
//                   BUDGET CARD
//----------------------------------------------------------

@Composable
fun BudgetCard(b: BudgetBreakdown) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {

        Column(Modifier.padding(18.dp)) {

            SummaryRow("🏨 Stay", b.stay)
            SummaryRow("🍽 Food", b.food)
            SummaryRow("🚗 Transport", b.localTransport)
            SummaryRow("🎟 Sightseeing", b.sightseeing)
            SummaryRow("➕ Buffer", b.extraBuffer)

            Divider(Modifier.padding(vertical = 8.dp))

            SummaryRow("💰 Total", b.totalEstimatedCost)
        }
    }
}

@Composable
fun ErrorScreen(
    retryLabel: String = "Retry",
    isRetrying: Boolean = false,
    onRetry: () -> Unit
) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Lottie (if available) with semantic label for accessibility
            LottieIcon(
                filePath = "files/error.json",
                modifier = Modifier.wrapContentSize()
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { if (!isRetrying) onRetry() },
                enabled = !isRetrying,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(0.6f)
            ) {
                Text(text = retryLabel)
            }
        }
}

