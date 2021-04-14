package de.westnordost.streetcomplete.quests.bike_parking_type

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.quests.AImageListQuestAnswerFragment
import de.westnordost.streetcomplete.quests.bike_parking_type.BarrierType.*
import de.westnordost.streetcomplete.view.image_select.Item

class AddBarrierTypeForm : AImageListQuestAnswerFragment<BarrierType, BarrierType>() {

    override val items = listOf(
        Item(GATE, R.drawable.bicycle_parking_type_stand, R.string.quest_bicycle_parking_type_stand),
        Item(LOG, R.drawable.bicycle_parking_type_stand, R.string.quest_bicycle_parking_type_stand),
    )

    override val itemsPerRow = 3

    override fun onClickOk(selectedItems: List<BarrierType>) {
        applyAnswer(selectedItems.single())
    }
}
