package nl.deltadak.plep.ui.settingspane.spinners

import nl.deltadak.plep.database.DatabaseSettings
import nl.deltadak.plep.ui.settingspane.SPINNER_WIDTH
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.GridPane
import nl.deltadak.plep.Database

/**
 * This spinner allows the user to select the number of columns that the main GridPane should show.
 */
class NumberOfColumnsSpinner {

    /**
     * Construct a new spinner.
     */
    fun getNew(): Spinner<Int> {

        val spinner = Spinner<Int>()

        // Get initial value from database
        val initialNumberOfColumns = Database.INSTANCE.getSetting(DatabaseSettings.MAX_COLUMNS.settingsName)
        spinner.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 14, initialNumberOfColumns.toInt())
        spinner.id = "maxColumnsSpinner"
        spinner.prefWidth = SPINNER_WIDTH // todo check good for double digits?
        GridPane.setColumnIndex(spinner, 2)
        GridPane.setRowIndex(spinner, 2)

        return spinner

    }

}