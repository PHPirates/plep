package nl.deltadak.plep.database.tables

import nl.deltadak.plep.HomeworkTask
import nl.deltadak.plep.database.regularTransaction
import org.jetbrains.exposed.sql.*
import java.time.LocalDate

/**
 * Describes the Tasks table for the database, and implements operations on this table.
 */
object Tasks : Table() {
    /** ID of a task. */
    val id = integer("id").primaryKey().uniqueIndex()
    /** Boolean: true if the task is done, false if the task is not done. */
    val done = bool("done")
    /** Day of the task in the format yyyy-mm-dd. */
    val day = varchar("day", length = 10)
    /** Text of the task. */
    val task = varchar("task", length = 255)
    /** Label of the task. */
    val label = varchar("label", length = 10)
    /** Color of the task. */
    val color = integer("color")
    /** Boolean: true if the task is expanded (subtasks are showing), false if the task is not expanded.
     * Always false if task has no subtasks. */
    val expanded = bool("expanded")
    /** The order of the task in a day, i.e., the tasks in a day are sorted by this number.*/
    val orderInDay = integer("orderInday")


    /**
     * Get all the parent tasks on a given day.
     *
     * @param day for which to get all the parent tasks.
     *
     * @return List<HomeworkTask>
     */
    fun getParentsOnDay(day: LocalDate) = regularTransaction {
        select { Tasks.day eq day.toString() }.map { HomeworkTask(
                done = it[Tasks.done],
                text = it[Tasks.task],
                label = it[Tasks.label],
                colorID = it[Tasks.color],
                expanded = it[Tasks.expanded],
                databaseID = it[Tasks.id]
        ) }
    }

    /**
     * Delete a parent task from the database.
     *
     * @param id of the task to be deleted.
     */
    fun delete(id: Int) = regularTransaction {
        deleteWhere { Tasks.id eq id }
    }

    /**
     * Delete the parent tasks on a day.
     *
     * @param day of which to delete the tasks.
     */
    fun deleteDay(day: LocalDate) = regularTransaction {
        deleteWhere { Tasks.day eq day.toString() }
    }

    /**
     * Updates the parent tasks in the database.
     *
     * @param day for which to update the tasks.
     * @param homeworkTasks to add to the day.
     */
    fun updateDay(day: LocalDate, homeworkTasks: List<HomeworkTask>) = regularTransaction {
        homeworkTasks.forEach { insertUpdate(day, it, homeworkTasks.indexOf(it)) }
        deleteWhere { Tasks.task eq "" } // Delete empty rows.
    }

    /**
     * Collects all the orders in a day and returns an int that is bigger than
     * the maximum.
     *
     * @param day of which to find the highest order.
     *
     * @return (The highest order that's currently in a day.) + 1
     */
    fun highestOrder(day: LocalDate): Int = regularTransaction {
        (select { Tasks.day eq day.toString() }.map { it[Tasks.orderInDay] }.maxOrNull() ?: 0) + 1
    }

    /**
     * Gets all the ids from the database, and returns (the highest id) + 1.
     *
     * @return int - (highest id currently in the database) + 1.
     */
    fun highestID(): Int = regularTransaction {
        (selectAll().map { it[Tasks.id] }.maxOrNull() ?: 0) + 1
    }

    /**
     * Inserts or updates a homeworkTask into the database. Only if the task is not empty.
     *
     * @param day of the task,
     * @param task to be inserted,
     * @param order this is the i-th homeworkTask on this day, as an int.
     */
    fun insertUpdate(day: LocalDate, task: HomeworkTask, order: Int) = regularTransaction {
        if (task.text != "") {
            // Assign ID if task was empty (and thus not in database).
            if (task.databaseID == -1) task.databaseID = highestID()
            deleteWhere { Tasks.id eq task.databaseID } // Delete to avoid duplicates.
            insert {
                it[Tasks.id] = task.databaseID
                it[Tasks.done] = task.done
                it[Tasks.day] = day.toString()
                it[Tasks.task] = task.text
                it[Tasks.label] = task.label
                it[Tasks.color] = task.colorID
                it[Tasks.expanded] = task.expanded
                it[Tasks.orderInDay] = order
            }
        }
    }
}