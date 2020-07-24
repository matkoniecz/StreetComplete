package de.westnordost.streetcomplete.quests.parking_roadside

import android.util.Log
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

class AddParkingRoadsideNearParking(private val overpassApi: OverpassMapDataAndGeometryApi) : AddParkingRoadside(overpassApi) {

    override fun getTitle(tags: Map<String, String>) = R.string.quest_parking_roadside_near_parking_title

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
                "(.streets; - .streets_near_parkings;) -> .queried_far_away_from_parking;\n" +
                "(.streets; - .queried_far_away_from_parking;);\n" +
                getQuestPrintStatement()
    }
}
