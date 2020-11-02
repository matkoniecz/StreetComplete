package de.westnordost.streetcomplete.quests.leaf_detail

import de.westnordost.osmapi.map.MapDataWithGeometry
import de.westnordost.osmapi.map.data.Element
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.elementfilter.toElementFilterExpression
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.elementgeometry.ElementPolygonsGeometry
import de.westnordost.streetcomplete.data.osm.osmquest.OsmElementQuestType
import de.westnordost.streetcomplete.util.measuredMultiPolygonArea

class AddForestLeafType : OsmElementQuestType<String> {
    private val areaFilter by lazy { """
        ways, relations with (landuse = forest or natural = wood) and !leaf_type
    """.toElementFilterExpression()}

    private val wayFilter by lazy { """
        ways with natural = tree_row and !leaf_type
    """.toElementFilterExpression()}

    override val commitMessage = "Add leaf type"
    override val wikiLink = "Key:leaf_type"
    override val icon = R.drawable.ic_quest_leaf
    override val isSplitWayEnabled = true

    override fun getApplicableElements(mapData: MapDataWithGeometry): Iterable<Element> {
        val forests = mapData
            .filter { areaFilter.matches(it) }
            .filter {
                val geometry = mapData.getGeometry(it.type, it.id) as? ElementPolygonsGeometry
                val area = geometry?.polygons?.measuredMultiPolygonArea() ?: 0.0
                area > 0.0 && area < 10000
            }
        val treeRows = mapData.filter { wayFilter.matches(it) }
        return forests + treeRows
    }

    override fun isApplicableTo(element: Element):Boolean? = null

    override fun getTitle(tags: Map<String, String>) = R.string.quest_leafType_title

    override fun createForm() = AddForestLeafTypeForm()

    override fun applyAnswerTo(answer: String, changes: StringMapChangesBuilder) {
        changes.add("leaf_type", answer)
    }
}
