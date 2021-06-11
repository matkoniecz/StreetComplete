package de.westnordost.streetcomplete.quests.sidewalk

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.meta.ANYTHING_UNPAVED
import de.westnordost.streetcomplete.data.meta.MAXSPEED_TYPE_KEYS
import de.westnordost.streetcomplete.data.osm.edits.update_tags.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType

class AddSidewalk : OsmFilterQuestType<SidewalkAnswer>() {

    /* the filter additionally filters out ways that are unlikely to have sidewalks:
     *
     * + unpaved roads, roads with very low speed limits and roads that are probably not developed
     *   enough to have sidewalk (i.e. country roads). But let's ask for urban roads at least
     *
     * + roads with a very low speed limit
     *
     * + Also, anything explicitly tagged as no pedestrians or explicitly tagged that the sidewalk
     *   is mapped as a separate way
    * */
    override val elementFilter = """
        ways with
          highway ~ trunk|trunk_link|primary|primary_link|secondary|secondary_link|tertiary|tertiary_link|unclassified|residential
          and area != yes
          and motorroad != yes
          and !sidewalk and !sidewalk:left and !sidewalk:right and !sidewalk:both
          and (
            !maxspeed
            or maxspeed > 8
            or (maxspeed ~ ".*mph" and maxspeed !~ "[1-5] mph")
          )
          and surface !~ ${ANYTHING_UNPAVED.joinToString("|")}
          and (
            lit = yes
            or highway = residential
            or ~${(MAXSPEED_TYPE_KEYS + "maxspeed").joinToString("|")} ~ .*urban|.*zone.*
          )
          and foot != no and access !~ private|no
          and foot != use_sidepath
    """

    override val commitMessage = "Add whether there are sidewalks"
    override val wikiLink = "Key:sidewalk"
    override val icon = R.drawable.ic_quest_sidewalk
    override val isSplitWayEnabled = true

    override fun getTitle(tags: Map<String, String>) = R.string.quest_sidewalk_title

    override fun createForm() = AddSidewalkForm()

    override fun applyAnswerTo(answer: SidewalkAnswer, changes: StringMapChangesBuilder) {
        changes.add("sidewalk", getSidewalkValue(answer))
    }

    private fun getSidewalkValue(answer: SidewalkAnswer) =
        when (answer) {
            is SeparatelyMapped -> "separate"
            is SidewalkSides -> when {
                answer.left && answer.right -> "both"
                answer.left -> "left"
                answer.right -> "right"
                else -> "none"
            }
        }
}
