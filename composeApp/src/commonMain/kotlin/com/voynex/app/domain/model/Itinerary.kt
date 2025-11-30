package com.voynex.app.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Itinerary(
    @SerialName("trip_summary")
    val tripSummary: TripSummary,
    @SerialName("daywise_itinerary")
    val daywiseItinerary: List<DaywiseItinerary>,
    @SerialName("hotel_recommendations")
    val hotelRecommendations: List<HotelRecommendation>,
    @SerialName("transportation_plan")
    val transportationPlan: TransportationPlan,
    @SerialName("packaging_and_safety")
    val packagingAndSafety: PackagingAndSafety,
    @SerialName("budget_breakdown")
    val budgetBreakdown: BudgetBreakdown
)

@Serializable
data class TripSummary(
    val destination: String,
    @SerialName("trip_duration")
    val tripDuration: String,
    @SerialName("travel_type")
    val travelType: String,
    @SerialName("budget_level")
    val budgetLevel: String,
    @SerialName("best_time_to_visit")
    val bestTimeToVisit: String,
    @SerialName("overall_vibe")
    val overallVibe: String
)

@Serializable
data class DaywiseItinerary(
    val day: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    val highlights: List<String>,
    val timeline: List<TimelineItem>,
    @SerialName("food_recommendations")
    val foodRecommendations: List<FoodRecommendation>
)

@Serializable
data class TimelineItem(
    val time: String,
    val activity: String,
    val details: String,
    @SerialName("travel_time")
    val travelTime: String,
    val cost: String
)

@Serializable
data class FoodRecommendation(
    @SerialName("meal_type")
    val mealType: String,
    val place: String,
    @SerialName("must_try")
    val mustTry: List<String>,
    @SerialName("approx_cost")
    val approxCost: String
)

@Serializable
data class HotelRecommendation(
    val name: String,
    @SerialName("price_range")
    val priceRange: String,
    val location: String,
    @SerialName("why_recommended")
    val whyRecommended: String
)

@Serializable
data class TransportationPlan(
    @SerialName("local_transport")
    val localTransport: String,
    @SerialName("inter_city_transport")
    val interCityTransport: String?,
    @SerialName("suggested_rentals")
    val suggestedRentals: List<String>,
    @SerialName("average_costs")
    val averageCosts: String
)

@Serializable
data class PackagingAndSafety(
    @SerialName("what_to_pack")
    val whatToPack: List<String>,
    @SerialName("safety_tips")
    val safetyTips: List<String>,
    @SerialName("scams_to_avoid")
    val scamsToAvoid: List<String>
)

@Serializable
data class BudgetBreakdown(
    val stay: String,
    val food: String,
    @SerialName("local_transport")
    val localTransport: String,
    val sightseeing: String,
    @SerialName("extra_buffer")
    val extraBuffer: String,
    @SerialName("total_estimated_cost")
    val totalEstimatedCost: String
)
fun monthLength(year: Int, month: String): Int {
    return when (month) {
        "January" -> 31
        "February" -> if (year % 4 == 0) 29 else 28
        "March" -> 31
        "April" -> 30
        "May" -> 31
        "June" -> 30
        "July" -> 31
        "August" -> 31
        "September" -> 30
        "October" -> 31
        "November" -> 30
        "December"-> 31
        else -> 30
    }
}
