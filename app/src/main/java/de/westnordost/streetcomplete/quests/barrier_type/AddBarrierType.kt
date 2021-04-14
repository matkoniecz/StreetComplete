package de.westnordost.streetcomplete.quests.bike_parking_type

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.osmquest.OsmFilterQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder

class AddBarrierType : OsmFilterQuestType<BarrierType>() {

    override val elementFilter = """
        nodes, with barrier=yes
    """
    override val commitMessage = "Add specific barrier type on a point"
    override val wikiLink = "Key:barrier"
    override val icon = R.drawable.ic_quest_bicycle_parking // TODO fix!
    override val isDeleteElementEnabled = true

    override fun getTitle(tags: Map<String, String>) = R.string.quest_barrier_type_title

    override fun createForm() = AddBarrierTypeForm()

    override fun applyAnswerTo(answer: BarrierType, changes: StringMapChangesBuilder) {
        changes.modify("barrier", answer.osmValue)
    }
}
