package com.voynex.app.navigation


import androidx.compose.runtime.Composable
import com.voynex.app.domain.model.Itinerary
import com.voynex.app.preferences.SharedPref
import com.voynex.app.ui.AppTheme
import com.voynex.app.ui.HomeScreen
import com.voynex.app.ui.ItineraryScreen
import com.voynex.app.ui.PlanTripScreen
import com.voynex.app.ui.SplashScreen
import com.voynex.app.ui.SuggestedCountriesScreen
import com.voynex.app.ui.TripInput
import com.voynex.app.ui.common.ViewModelFactory
import kotlinx.serialization.json.Json
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
@Composable
fun App(factory: ViewModelFactory) {
    val navigator = rememberNavigator()

    AppTheme {
        NavHost(
            navigator = navigator,
            initialRoute = "/splash"        // 👈 Start here
        ) {

            // --- ✔ Splash Screen ---
            scene("/splash") {
                SplashScreen(
                    onFinished = {
                        navigator.navigate("/home", NavOptions(popUpTo = PopUpTo.First(inclusive = true)))
//
                    }
                )
            }

            // --- Existing routes ---
            scene("/home") {
                HomeScreen(
                    onDestinationClick = { destination ->
                        navigator.navigate("/destination/$destination")
                    },
                    onCategoryClick = {category->
                        navigator.navigate("/category/$category")
                    },
                    factory = factory,
                    onSavedItineraryClick = {
                        navigator.navigate("/itinerarySaved/${it.tripSummary.destination}")
                    }
                )
            }
            scene("/category/{category}") {backStackEntry->
                val category = backStackEntry.path<String>("category") ?: ""

                SuggestedCountriesScreen(
                    category = category,
                    onDestinationClick = { destination ->
                        navigator.navigate("/destination/$destination")
                    },
                    factory = factory
                )
            }

            scene("/destination/{destination}") { backStackEntry ->
                val destination = backStackEntry.path<String>("destination") ?: ""
                PlanTripScreen(destination = destination, factory = factory) { tripInput ->
                    val tripInputJson = Json.encodeToString(TripInput.serializer(), tripInput)
                    navigator.navigate("/itinerary/$tripInputJson")
                }
            }

            scene("/itinerary/{tripInputJson}") { backStackEntry ->
                val tripInputJson = backStackEntry.path<String>("tripInputJson")
                if (tripInputJson != null) {
                    val tripInput = Json.decodeFromString(TripInput.serializer(), tripInputJson)
                    ItineraryScreen(tripInput = tripInput, factory = factory)
                }
            }
            scene("/itinerarySaved/{destination}") { backStackEntry ->
                val itinerarySavedJson = backStackEntry.path<String>("destination") ?: ""
                if (itinerarySavedJson != null) {
                    ItineraryScreen(tripInput = null, factory = factory,itinerarySavedJson)
                }
            }
        }
    }
}
