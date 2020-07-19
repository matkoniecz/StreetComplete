package de.westnordost.streetcomplete.quests.parking_roadside

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.view.Item

enum class ParkingRoadside(private val iconResId: Int, private val iconResIdLeft: Int, val nameResId: Int) {
    // TODO: fix icons!
    PARALLEL              (R.drawable.ic_cycleway_lane,        R.drawable.ic_cycleway_lane,         R.string.quest_parking_roadside_value_parallel ),
    DIAGONAL              (R.drawable.ic_cycleway_sidewalk,    R.drawable.ic_cycleway_sidewalk,     R.string.quest_parking_roadside_value_diagonal ),
    PERPENDICULAR         (R.drawable.ic_cycleway_sidewalk,    R.drawable.ic_cycleway_sidewalk,     R.string.quest_parking_roadside_value_perpendicular ),
    NO_GENERIC            (R.drawable.ic_cycleway_suggestion_lane,  R.drawable.ic_cycleway_suggestion_lane,     R.string.quest_parking_roadside_value_no_generic ),
    NO_PARKING            (R.drawable.ic_cycleway_bus_lane,    R.drawable.ic_cycleway_bus_lane,     R.string.quest_parking_roadside_value_no_parking ),
    NO_STOPPING           (R.drawable.ic_cycleway_pictograms,  R.drawable.ic_cycleway_pictograms,   R.string.quest_parking_roadside_value_no_stopping ),
    NO_STOPPING_FIRE_LANE (R.drawable.ic_cycleway_none,        R.drawable.ic_cycleway_none,         R.string.quest_parking_roadside_value_no_stopping_fire_lane );
    //INDIVIDUAL_SPOTS      (R.drawable.ic_cycleway_none,        R.drawable.ic_cycleway_none,         R.string.quest_parking_lane_value_individual_marked ), // marked - how to describe this? Via Cant say?

    fun asItem(isLeftHandTraffic: Boolean) = Item(this, getIconResId(isLeftHandTraffic), nameResId)

    fun getIconResId(isLeftHandTraffic: Boolean) =
        if (isLeftHandTraffic) iconResIdLeft else iconResId

    companion object {
        val displayValues = listOf(
            PARALLEL, DIAGONAL, PERPENDICULAR, NO_PARKING, NO_STOPPING, NO_STOPPING_FIRE_LANE, NO_GENERIC
        )
    }
}
