package nl.deltadak.plep.ui.settingspane.applybuttons

import javafx.scene.control.Button
import javafx.scene.control.Spinner
import nl.deltadak.plep.database.settingsdefaults.SettingsDefaults
import nl.deltadak.plep.database.tables.Settings
import kotlin.reflect.KMutableProperty

/**
 * This button applies the number of days that the forward and backward button skip when pressed.
 */
class ApplyNumberOfMovingDaysAction(
        /** The FXML reference to the button. */
        private val applyNumberOfMovingDaysButton: Button,
        /** The FXML reference to the spinner. */
        private val numberOfMovingDaysSpinner: Spinner<Int>) {

    /**
     * Set the button action.
     *
     * @param numberOfMovingDaysProperty Should point to the number of days that the forward and backward button skip when pressed.
     */
    fun set(numberOfMovingDaysProperty: KMutableProperty<Int>, refreshUI: () -> Unit) {

        applyNumberOfMovingDaysButton.setOnAction {

            // Get current user-selected value.
            val numberOfMovingDays = numberOfMovingDaysSpinner.value

            // Update property.
            numberOfMovingDaysProperty.setter.call(numberOfMovingDays)

            // Update database.
            Settings.update(SettingsDefaults.NUMBER_OF_MOVING_DAYS, numberOfMovingDays.toString())

            // Update UI.
            refreshUI()

        }

    }

}
