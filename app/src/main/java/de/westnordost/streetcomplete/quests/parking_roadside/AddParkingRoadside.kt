package de.westnordost.streetcomplete.quests.parking_roadside

import de.westnordost.osmapi.map.data.BoundingBox
import de.westnordost.osmapi.map.data.Element
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.meta.ANYTHING_UNPAVED
import de.westnordost.streetcomplete.data.osm.elementgeometry.ElementGeometry
import de.westnordost.streetcomplete.data.osm.osmquest.OsmElementQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.mapdata.OverpassMapDataAndGeometryApi
import de.westnordost.streetcomplete.data.tagfilters.getQuestPrintStatement
import de.westnordost.streetcomplete.data.tagfilters.toGlobalOverpassBBox

import de.westnordost.streetcomplete.quests.bikeway.Cycleway.*

class AddParkingRoadside(private val overpassApi: OverpassMapDataAndGeometryApi) : OsmElementQuestType<ParkingRoadsideAnswer> {

    override val commitMessage = "Add type of parking beside road"
    override val wikiLink = "Key:parking_lane"
    override val icon = R.drawable.ic_quest_parking // TODO needs a dedicated icon!

    // TODO - limit enablement?
    /*
    // See overview here: https://ent8r.github.io/blacklistr/?streetcomplete=bikeway/AddCycleway.kt
    // #749. sources:
    // Google Street View (driving around in virtual car)
    // https://en.wikivoyage.org/wiki/Cycling
    // http://peopleforbikes.org/get-local/ (US)
    override val enabledInCountries = NoCountriesExcept(
            // all of Northern and Western Europe, most of Central Europe, some of Southern Europe
            "NO", "SE", "FI", "IS", "DK",
            "GB", "IE", "NL", "BE", "FR", "LU",
            "DE", "PL", "CZ", "HU", "AT", "CH", "LI",
            "ES", "IT",
            // East Asia
            "JP", "KR", "TW",
            // some of China (East Coast)
            "CN-BJ", "CN-TJ", "CN-SD", "CN-JS", "CN-SH",
            "CN-ZJ", "CN-FJ", "CN-GD", "CN-CQ",
            // Australia etc
            "NZ", "AU",
            // some of Canada
            "CA-BC", "CA-QC", "CA-ON", "CA-NS", "CA-PE",
            // some of the US
            // West Coast, East Coast, Center, South
            "US-WA", "US-OR", "US-CA",
            "US-MA", "US-NJ", "US-NY", "US-DC", "US-CT", "US-FL",
            "US-MN", "US-MI", "US-IL", "US-WI", "US-IN",
            "US-AZ", "US-TX"
    )
    */

    override val isSplitWayEnabled = true

    override fun getTitle(tags: Map<String, String>) = R.string.quest_parking_roadside_title

    override fun isApplicableTo(element: Element):Boolean? = null

    override fun download(bbox: BoundingBox, handler: (element: Element, geometry: ElementGeometry?) -> Unit): Boolean {
        return overpassApi.query(getOverpassQuery(bbox), handler)
    }

    /** returns overpass query string to get streets without cycleway info not near paths for
     * bicycles.
     */
    private fun getOverpassQuery(bbox: BoundingBox): String {
        val minDistToParkings = 15 //m

        return bbox.toGlobalOverpassBBox() + "\n" +
            "way[highway ~ '^(trunk|trunk_link|primary|primary_link|secondary|secondary_link|tertiary|tertiary_link|unclassified|residential|living_street)$']" +
            "[area != yes]" +
            // only without parking lane tags
            "[!'parking:lane:left'][!'parking:lane:right'][!'parking:lane:both']" +
            // not any motorroads or roundabouts
            "[motorroad != yes][junction != roundabout]" +
            // not in tunnels
            "[tunnel != yes]" +
            // not any unpaved ones
            "[surface !~ '^(" + ANYTHING_UNPAVED.joinToString("|") + ")$']" +
            // not any private ones, asking here about parking access is tricky at best
            "[access !~ '^(private|no)$']" +
            "[vehicle !~ '^(private|no)$']" +
            // not any with very high speed limit because they not very likely to have parking lanes
            "[maxspeed !~ '^(70|80|90|[1-9][0-9][0-9]|45 mph|50 mph|55 mph|60 mph|65 mph|70 mph)$']" +
            " -> .streets;\n" +
            "(\n" +
                "  node[amenity=parking](around.streets: " + minDistToParkings + ");\n" +
                "  way[amenity=parking](around.streets: " + minDistToParkings + ");\n" +
                "  relation[amenity=parking](around.streets: " + minDistToParkings + ");\n" +
            ") -> .parkings;\n" +
            "way.streets(around.parkings: " + minDistToParkings + ") -> .streets_near_parkings;\n" +
            "(.streets; - .streets_near_parkings;);\n" +
            getQuestPrintStatement()
    }


    override fun createForm() = AddParkingRoadsideForm()

    override fun applyAnswerTo(answer: ParkingRoadsideAnswer, changes: StringMapChangesBuilder) {
        answer.apply {
            if (left == right) {
                left?.let { applyParkingRoadsideAnswerTo(it, Side.BOTH, changes) }
            } else {
                left?.let { applyParkingRoadsideAnswerTo(it, Side.LEFT, changes) }
                right?.let { applyParkingRoadsideAnswerTo(it, Side.RIGHT, changes) }
            }
        }
    }

    private enum class Side(val value: String) {
        LEFT("left"), RIGHT("right"), BOTH("both")
    }

    private fun applyParkingRoadsideAnswerTo(parkingRoadside: ParkingRoadside, side: Side,
                                      changes: StringMapChangesBuilder ) {
        val parkingRoadsideKey = "parking:lane:" + side.value
        when (parkingRoadside) {
            ParkingRoadside.PARALLEL  -> {
                changes.add(parkingRoadsideKey, "parallel")
            }
            ParkingRoadside.DIAGONAL  -> {
                changes.add(parkingRoadsideKey, "diagonal")
            }
            ParkingRoadside.PERPENDICULAR  -> {
                changes.add(parkingRoadsideKey, "perpendicular")
            }
            ParkingRoadside.NO_PARKING  -> {
                changes.add(parkingRoadsideKey, "no_parking")
            }
            ParkingRoadside.NO_STOPPING  -> {
                changes.add(parkingRoadsideKey, "no_stopping")
            }
            ParkingRoadside.NO_STOPPING_FIRE_LANE  -> {
                changes.add(parkingRoadsideKey, "fire_lane")
            }
            ParkingRoadside.NO_GENERIC  -> {
                changes.add(parkingRoadsideKey, "no")
            }
        }
    }
}
