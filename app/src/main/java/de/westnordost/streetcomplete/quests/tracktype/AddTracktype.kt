package de.westnordost.streetcomplete.quests.tracktype

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.meta.ANYTHING_UNPAVED
import de.westnordost.streetcomplete.data.meta.updateWithCheckDate
import de.westnordost.streetcomplete.data.osm.osmquest.OsmFilterQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder

class AddTracktype : OsmFilterQuestType<String>() {

    override val elementFilter = """
        ways with highway = track
        and (
          !tracktype
          or tracktype != grade1 and tracktype older today -4 years
          or surface ~ ${ANYTHING_UNPAVED.joinToString("|")} and tracktype older today -4 years
          or tracktype older today -8 years
        )
        and (access !~ private|no or (foot and foot !~ private|no))
    """
    /* ~paved tracks are less likely to change the surface type */

    override val commitMessage = "Add tracktype"
    override val wikiLink = "Key:tracktype"
    override val icon = R.drawable.ic_quest_tractor
    override val isSplitWayEnabled = true

    override fun getTitle(tags: Map<String, String>) = R.string.quest_tracktype_title

    override fun createForm() = AddTracktypeForm()

    override fun applyAnswerTo(answer: String, changes: StringMapChangesBuilder) {
        changes.updateWithCheckDate("tracktype", answer)
    }
}
