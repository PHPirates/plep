package nl.deltadak.plep.database

import javafx.scene.control.ProgressIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.javafx.JavaFx as FxMain
import kotlinx.coroutines.launch
import nl.deltadak.plep.HomeworkTask
import nl.deltadak.plep.database.tables.Tasks
import java.time.LocalDate


/**
 * This class provides a facade to the database. General methods like pushing and pulling data from the database are included here, so extras can be added like proper user feedback and multithreading.
 */
class DatabaseFacade(
        /** For user feedback. */
        val progressIndicator: ProgressIndicator) {

    /**
     * Updates database using the given homework tasks for a day. Uses the progress indicator for user feedback.
     *
     * @param day Date from which the tasks are.
     * @param homeworkTasks Tasks to be put in the database.
     */
    fun pushData(day: LocalDate, homeworkTasks: List<List<HomeworkTask>>) {

        // Pushing to the database using coroutines.
        val job = GlobalScope.launch(Dispatchers.FxMain) {
            TaskFamily.updateAllDay(day, homeworkTasks)
        }

        switchProgressIndicator(job)
    }

    /**
     * Update only the parent tasks in the database.
     *
     * @param day The day which contains the tasks.
     * @param parentTasks The list with parents to updateOrInsert.
     */
    fun pushParentData(day: LocalDate, parentTasks: List<HomeworkTask>) {

        val job = GlobalScope.launch(Dispatchers.FxMain) {
            Tasks.updateDay(day, parentTasks)
        }

        switchProgressIndicator(job)
    }

    /**
     * If progress indicator is not yet visible, switch it on and also switch it off when the job is finished.
     *
     * @param job The job the progress indicator has to wait for.
     */
    private fun switchProgressIndicator(job: Job) {
        // Only switch it on and off if it's not yet on.
        if (!progressIndicator.isVisible) {

            // Switch on progress indicator.
            progressIndicator.isVisible = true

            // Switch off progress indicator.
            job.invokeOnCompletion { progressIndicator.isVisible = false }
        }
    }

}