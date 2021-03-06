package nl.deltadak.plep.ui

import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import nl.deltadak.plep.commands.UndoFacility
import nl.deltadak.plep.database.DatabaseFacade
import nl.deltadak.plep.database.regularTransaction
import nl.deltadak.plep.database.settingsdefaults.SettingsDefaults
import nl.deltadak.plep.database.tables.*
import nl.deltadak.plep.keylisteners.UndoKeyListener
import nl.deltadak.plep.ui.gridpane.GridPaneInitializer
import nl.deltadak.plep.ui.settingspane.panes.SlidingPane
import nl.deltadak.plep.ui.settingspane.panes.SlidingSettingsPane
import nl.deltadak.plep.ui.util.DEFAULT_COLORS
import org.jetbrains.exposed.sql.SchemaUtils
import java.io.File
import java.time.LocalDate

@Suppress("KDocMissingDocumentation") // FXML references.
/**
 * Class to control the UI
 */
class Controller {

    /** The main element of the UI is declared in interface.fxml. */
    @FXML lateinit var main: AnchorPane
    /** The main GridPane which contains everything.  */
    @FXML lateinit var gridPane: GridPane
    /** Top toolbar with buttons.  */
    @FXML lateinit var toolBar: ToolBar
    /** To show feedback. */
    @FXML lateinit var progressIndicator: ProgressIndicator

    // All these references have to be declared in controller because of fxml.

    /** Help pane. */
    @FXML lateinit var helpPane: AnchorPane
    @FXML lateinit var helpButton: Button

    /** Settings pane. */
    @FXML lateinit var settingsPane: AnchorPane
    @FXML lateinit var settingsButton: Button

    @FXML lateinit var editLabelsPane: GridPane
    @FXML lateinit var editLabelsButton: Button
    @FXML lateinit var editDaysPane: GridPane
    @FXML lateinit var removeLabelButton: Button
    /** To adjust the number of days to skip forward/backward.  */
    @FXML lateinit var applyNumberOfMovingDays: Button
    /** To adjust the number of days shown.  */
    @FXML lateinit var applyNumberOfDays: Button
    @FXML lateinit var autoColumnCheckBox: CheckBox
    @FXML lateinit var applyNumberOfColumns: Button

    @FXML lateinit var colorsPane: GridPane
    @FXML lateinit var colorOne: ColorPicker
    @FXML lateinit var colorTwo: ColorPicker
    @FXML lateinit var colorThree: ColorPicker
    @FXML lateinit var colorFour: ColorPicker
    @FXML lateinit var colorFive: ColorPicker

    /** Number of days shown, default value will be overridden in [initialize]. */
    var numberOfDays: Int = 0

    /** Number of days to skip when using the forward/backward buttons, default value will be overridden in [initialize]. */
    var numberOfMovingDays: Int = 0

    /** Day on which the gridpane is 'focused': the second day shown will be this day. */
    var focusDay: LocalDate = LocalDate.now()

    /** Keep a reference to the same undo facility. */
    val undoFacility = UndoFacility()

    /** Refreshes UI when called. */
    private val refreshUI = { GridPaneInitializer(undoFacility, progressIndicator).setup(gridPane, ::numberOfDays, ::focusDay, toolBar.prefHeight) }

    // Cannot be private, is called by JavaFX.
    @Suppress("MemberVisibilityCanBePrivate")
    /**
     * Initialization method for the controller.
     */
    fun initialize() {

        // Try to find out if a database already exists, create if not.
        val databasePath = "${File(DatabaseFacade::class.java.protectionDomain.codeSource.location.toURI()).parent}/plep.db"
        println("Checking for a database at $databasePath")
        if (!File(databasePath).isFile) {
            // Create the tables in the database.
            listOf(Tasks, SubTasks, Settings, Labels, Colors).forEach { regularTransaction { SchemaUtils.create(it) }}

            // Put the default settings in the database.
            SettingsDefaults.values().forEach { Settings.insert(it, it.default) }

            // Put the default colors in the database.
            (1..DEFAULT_COLORS.size).forEach { Colors.insert(it, DEFAULT_COLORS[it-1]) }
        }

        numberOfDays = Settings.get(SettingsDefaults.NUMBER_OF_DAYS).let { if(it=="") 0 else it.toInt() }
        numberOfMovingDays = Settings.get(SettingsDefaults.NUMBER_OF_MOVING_DAYS).let {if(it=="") 0 else it.toInt()}

        refreshUI()

        progressIndicator.isVisible = false

        val colorPickers = arrayListOf(colorOne, colorTwo, colorThree, colorFour, colorFive)

        // Setup the settings page.
        SlidingSettingsPane(
                refreshUI,
                ::numberOfMovingDays,
                ::numberOfDays,
                editLabelsButton,
                editLabelsPane,
                editDaysPane,
                settingsPane,
                removeLabelButton,
                applyNumberOfMovingDays,
                applyNumberOfDays,
                applyNumberOfColumns,
                autoColumnCheckBox,
                colorPickers,
                main,
                gridPane,
                toolBar,
                settingsPane,
                settingsButton
        ).setup()

        // Setup the help page.
        SlidingPane(main, gridPane, toolBar, helpPane, helpButton).setup()

        UndoKeyListener().set(gridPane, undoFacility)

    }

    /**
     * Called by the backward button.
     * Moves the planner a (few) day(s) back.
     */
    @FXML
    fun dayBackward() {
        focusDay = focusDay.plusDays((-numberOfMovingDays).toLong())
        refreshUI()
    }

    /**
     * Called by the today button.
     * Focuses the planner on today.
     */
    @FXML
    fun goToToday() {
        focusDay = LocalDate.now()
        refreshUI()
    }

    /**
     * Called by the forward button.
     * Moves the planner a (few) day(s) forward.
     */
    @FXML
    fun dayForward() {
        focusDay = focusDay.plusDays(numberOfMovingDays.toLong())
        refreshUI()
    }

}