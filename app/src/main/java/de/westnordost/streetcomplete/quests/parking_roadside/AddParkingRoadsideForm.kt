package de.westnordost.streetcomplete.quests.parking_roadside

import android.os.Bundle
import androidx.annotation.AnyThread
import android.view.View

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.elementgeometry.ElementPolylinesGeometry
import de.westnordost.streetcomplete.quests.AbstractQuestFormAnswerFragment
import de.westnordost.streetcomplete.quests.OtherAnswer
import de.westnordost.streetcomplete.quests.StreetSideRotater
import de.westnordost.streetcomplete.view.dialogs.ImageListPickerDialog
import kotlinx.android.synthetic.main.quest_street_side_puzzle.*


class AddParkingRoadsideForm : AbstractQuestFormAnswerFragment<ParkingRoadsideAnswer>() {

    override val contentLayoutResId = R.layout.quest_street_side_puzzle
    override val contentPadding = false

    private var streetSideRotater: StreetSideRotater? = null

    private var leftSide: ParkingRoadside? = null
    private var rightSide: ParkingRoadside? = null

    /** returns whether the side that goes into the opposite direction of the driving direction of a
     * one-way is on the right side of the way */
    private val isReverseSideRight get() = isReversedOneway xor isLeftHandTraffic

    private val isOneway get() = isForwardOneway || isReversedOneway

    private val isForwardOneway get() = osmElement!!.tags["oneway"] == "yes"
    private val isReversedOneway get() = osmElement!!.tags["oneway"] == "-1"

    // just a shortcut
    private val isLeftHandTraffic get() = countryInfo.isLeftHandTraffic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.getString(CYCLEWAY_RIGHT)?.let { rightSide = ParkingRoadside.valueOf(it) }
        savedInstanceState?.getString(CYCLEWAY_LEFT)?.let { leftSide = ParkingRoadside.valueOf(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        puzzleView.listener = { isRight -> showCyclewaySelectionDialog(isRight) }

        streetSideRotater = StreetSideRotater(puzzleView, compassNeedleView, elementGeometry as ElementPolylinesGeometry)
        val defaultResId =
            if (isLeftHandTraffic) R.drawable.ic_cycleway_unknown_l
            else                   R.drawable.ic_cycleway_unknown

        val oppositeResId =
                if (isLeftHandTraffic) R.drawable.ic_cycleway_unknown
                else                   R.drawable.ic_cycleway_unknown_l

        if(!isOneway) {
            puzzleView.setLeftSideImageResource(leftSide?.getIconResId(isLeftHandTraffic) ?: defaultResId)
            puzzleView.setRightSideImageResource(rightSide?.getIconResId(isLeftHandTraffic) ?: defaultResId)
        } else if (isForwardOneway) {
            puzzleView.setLeftSideImageResource(rightSide?.getIconResId(!isLeftHandTraffic) ?: oppositeResId)
            puzzleView.setRightSideImageResource(rightSide?.getIconResId(isLeftHandTraffic) ?: defaultResId)
        } else if (isReversedOneway){
            puzzleView.setLeftSideImageResource(leftSide?.getIconResId(isLeftHandTraffic) ?: defaultResId)
            puzzleView.setRightSideImageResource(leftSide?.getIconResId(!isLeftHandTraffic) ?: oppositeResId)
        } else {
            throw IllegalStateException("oneway should be either forward or reversed, other situations are not handled")
        }

        checkIsFormComplete()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        rightSide?.let { outState.putString(CYCLEWAY_RIGHT, it.name) }
        leftSide?.let { outState.putString(CYCLEWAY_LEFT, it.name) }
    }

    @AnyThread
    override fun onMapOrientation(rotation: Float, tilt: Float) {
        streetSideRotater?.onMapOrientation(rotation, tilt)
    }

    override fun onClickOk() {
        applyAnswer(ParkingRoadsideAnswer(
            left = leftSide,
            right = rightSide
        ))
    }

    override fun isFormComplete() =  leftSide != null && rightSide != null

    override fun isRejectingClose() = leftSide != null || rightSide != null

    private fun showCyclewaySelectionDialog(isRight: Boolean) {
        val ctx = context ?: return
        val items = getParkingRoadsideItems(isRight).map { it.asItem(isLeftHandTraffic) }
        ImageListPickerDialog(ctx, items, R.layout.labeled_icon_button_cell, 2) { selected ->
            val cycleway = selected.value!!
            val iconResId = cycleway.getIconResId(isLeftHandTraffic)

            if (isRight) {
                puzzleView.replaceRightSideImageResource(iconResId)
                rightSide = cycleway
            } else {
               puzzleView.replaceLeftSideImageResource(iconResId)
                leftSide = cycleway
            }
            checkIsFormComplete()
        }.show()
    }

    private fun getParkingRoadsideItems(isRight: Boolean): List<ParkingRoadside> {
        val values = ParkingRoadside.displayValues.toMutableList()
        /*
        TODO is there need for any special handling based on country?
        val country = countryInfo.countryCode
        if ("BE" == country) {
            // Belgium does not make a difference between continuous and dashed lanes -> so don't tag that difference
            // also, in Belgium there is a differentiation between the normal lanes and suggestion lanes
            values.remove(ParkingRoadside.EXCLUSIVE_LANE)
            values.remove(ParkingRoadside.ADVISORY_LANE)
            values.add(0, ParkingRoadside.LANE_UNSPECIFIED)
            values.add(1, ParkingRoadside.SUGGESTION_LANE)
        }
        */
        return values
    }

    companion object {
        private const val CYCLEWAY_LEFT = "cycleway_left"
        private const val CYCLEWAY_RIGHT = "cycleway_right"
    }
}
