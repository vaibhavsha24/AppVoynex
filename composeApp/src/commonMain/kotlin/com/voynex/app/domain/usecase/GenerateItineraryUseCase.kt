package com.voynex.app.domain.usecase

import com.voynex.app.domain.repository.DestinationImagesRepository
import com.voynex.app.domain.repository.ItineraryRepository
import com.voynex.app.ui.TripInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenerateItineraryUseCase(private val repository: ItineraryRepository) {
    suspend operator fun invoke(tripInput: TripInput): String {
        val userInput = """
            - Destination: ${tripInput.destination}
            - Departure Date: ${tripInput.departureDate}
            - Arrival Date: ${tripInput.arrivalDate}
            - Traveller Type: ${tripInput.travellerType}
            - Pace of Travel: ${tripInput.paceOfTravel}
            - Accommodation Preference: ${tripInput.accommodationPreference}
            - Transportation Preference: ${tripInput.transportation}
            - Food Preference: ${tripInput.foodPreference}
            - Budget per Person: ₹${tripInput.budget}
            - Interests: ${tripInput.preferences.joinToString(", ")}
            - Special Notes: ${tripInput.notes}
        """.trimIndent()

        val prompt = """
            You are a professional travel-planning AI.

            Create a very detailed travel itinerary based on the user's input.
            Always respond ONLY in valid JSON.
            Use this schema and fill all details thoroughly:

            {
              "trip_summary": {
                "destination": "string",
                "trip_duration": "string",
                "travel_type": "string",
                "budget_level": "string",
                "best_time_to_visit": "string",
                "overall_vibe": "string"
              },

              "daywise_itinerary": [
                {
                  "day": "Day 1",
                  "start_time": "string",
                  "end_time": "string",
                  "highlights": ["string"],
                  "timeline": [
                    {
                      "time": "7:00 AM",
                      "activity": "string",
                      "details": "string",
                      "travel_time": "string",
                      "cost": "string"
                    }
                  ],
                  "food_recommendations": [
                    {
                      "meal_type": "breakfast/lunch/dinner",
                      "place": "string",
                      "must_try": ["string"],
                      "approx_cost": "string"
                    }
                  ]
                }
              ],

              "hotel_recommendations": [
                {
                  "name": "string",
                  "price_range": "string",
                  "location": "string",
                  "why_recommended": "string"
                }
              ],

              "transportation_plan": {
                "local_transport": "string",
                "inter_city_transport": "string or null",
                "suggested_rentals": ["string"],
                "average_costs": "string"
              },

              "packaging_and_safety": {
                "what_to_pack": ["string"],
                "safety_tips": ["string"],
                "scams_to_avoid": ["string"]
              },

              "budget_breakdown": {
                "stay": "string",
                "food": "string",
                "local_transport": "string",
                "sightseeing": "string",
                "extra_buffer": "string",
                "total_estimated_cost": "string"
              }
            }

            User Input:
            $userInput
        """.trimIndent()

        return repository.generateItinerary(prompt)
    }
}
class GetCoverImage(private val repository: DestinationImagesRepository){

    suspend operator fun invoke(destination:String):Category = withContext(Dispatchers.Default) {
        val imageUrl = repository.getCoverImage(destination)
        Category(destination, imageUrl ?: "")
    }
}