package nl.deltadak.plep.keylisteners

import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TreeView
import javafx.scene.input.KeyCode
import nl.deltadak.plep.HomeworkTask
import nl.deltadak.plep.commands.DeleteCommand
import nl.deltadak.plep.commands.DeleteSubtaskCommand
import nl.deltadak.plep.commands.UndoFacility
import nl.deltadak.plep.ui.util.converters.toHomeworkTaskList
import java.time.LocalDate

/**
 * This class provides initialisation of deletion of tasks.
 */
class TaskDeletionInitialiser(
        /** User feedback. */
        val progressIndicator: ProgressIndicator,
        /** The facility which provides deletion including the undoing of deletion. */
        private val undoFacility: UndoFacility) {

    /**
     * Add a listener on a TreeView which listens for the delete key to be pressed. It will find out which item was selected and delete that one.
     *
     * @param tree TreeView to set the listener on.
     * @param localDate Date of the TreeView.
     */
    fun addDeleteKeyListener(tree: TreeView<HomeworkTask>, localDate: LocalDate) {

        /**
         * Delete the selected parent task (task on the highest level: not a subtask) and save that to the database.
         */
        fun deleteParentTask() {

            // Get the selected item.
            val selectedItem = tree.selectionModel
                    .selectedItem

            // Get the index of the selected item.
            // Note: using selectionModel().getSelectedIndex() returns the index
            // when also counting subtasks, so this does not work.
            val parentIndex = tree.root.children.indexOf(selectedItem)

            undoFacility.execute(DeleteCommand(
                    progressIndicator,
                    localDate,
                    tree.toHomeworkTaskList(),
                    parentIndex,
                    tree
            ))
        }

        /**
         * Delete the selected subtask.
         */
        fun deleteSubtask() {
            undoFacility.execute(DeleteSubtaskCommand(
                    progressIndicator,
                    localDate,
                    tree.toHomeworkTaskList(),
                    tree.selectionModel.selectedIndex,
                    tree
            ))
        }


        tree.setOnKeyPressed { event ->
            if (event.code == KeyCode.DELETE) {
                // Check whether we want to delete a parent task or subtask.
                if (tree.selectionModel.selectedItem.parent == tree.root) {
                    // Delete the parent task.
                    deleteParentTask()
                } else {
                    // Delete subtask.
                    deleteSubtask()
                }

            }
        }


    }

}