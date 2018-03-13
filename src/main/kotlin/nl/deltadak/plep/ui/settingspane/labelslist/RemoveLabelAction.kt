package nl.deltadak.plep.ui.settingspane.labelslist

import javafx.scene.control.Button
import javafx.scene.control.ListView
import nl.deltadak.plep.Database
import nl.deltadak.plep.ui.SlidingSettingsPane
import kotlin.reflect.KMutableProperty

/**
 * This buttons removes the selected course label from the list.
 */
class RemoveLabelAction(
        /** The FXML reference to the button. */
        val removeLabelButton: Button) {

    /**
     * Temporary function to be called from Java, since that has no pass by reference for variables.
     */
    fun javaSet(slidingSettingsPane: SlidingSettingsPane, refreshUI: () -> Unit) {
        set(slidingSettingsPane::labelsList, refreshUI)
    }

    /**
     * Set the button action.
     *
     * @param labelsListProp Should be a variable reference to the list of labels.
     * @param refreshUI Should refresh the UI when called.
     */
    fun set(labelsListProp: KMutableProperty<ListView<String>>,
            refreshUI: () -> Unit) {

        removeLabelButton.setOnAction {

            // Get current value.
            val labelsList = labelsListProp.getter.call()

            val selectedIndex = labelsList.selectionModel.selectedIndex
            // Removing an item means replacing it with an empty item, so it is again editable.
            labelsList.items[selectedIndex] = ""
            Database.INSTANCE.updateLabel(selectedIndex, "")
            refreshUI()

        }
    }
}
