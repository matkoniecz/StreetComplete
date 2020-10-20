package de.westnordost.streetcomplete.quests.traffic_signals_sound

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.meta.updateWithCheckDate
import de.westnordost.streetcomplete.data.osm.osmquest.SimpleOverpassQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.mapdata.OverpassMapDataAndGeometryApi
import de.westnordost.streetcomplete.ktx.toYesNo
import de.westnordost.streetcomplete.quests.YesNoQuestAnswerFragment
import de.westnordost.streetcomplete.settings.ResurveyIntervalsStore

class AddTrafficSignalsSound(o: OverpassMapDataAndGeometryApi, r: ResurveyIntervalsStore)
    : SimpleOverpassQuestType<Boolean>(o) {

    override val tagFilters = """
        nodes with crossing = traffic_signals and highway ~ crossing|traffic_signals 
        and (
          !$SOUND_SIGNALS
          or $SOUND_SIGNALS = no and $SOUND_SIGNALS older today -${r * 4} years
          or $SOUND_SIGNALS older today -${r * 8} years
        )
    """

    override val commitMessage = "Add $SOUND_SIGNALS tag"
    override val wikiLink = "Key:$SOUND_SIGNALS"
    override val icon = R.drawable.ic_quest_blind_traffic_lights_sound

    override fun getTitle(tags: Map<String, String>) = R.string.quest_traffic_signals_sound_title

    override fun createForm() = YesNoQuestAnswerFragment()

    override fun applyAnswerTo(answer: Boolean, changes: StringMapChangesBuilder) {
        changes.updateWithCheckDate(SOUND_SIGNALS, answer.toYesNo())
    }
    override val defaultDisabledMessage = R.string.default_disabled_msg_boring
}

private const val SOUND_SIGNALS = "traffic_signals:sound"