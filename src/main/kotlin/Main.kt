import model.Note
import service.NoteService
import utils.readNextInt
import io.github.oshai.kotlinlogging.KotlinLogging


val noteService = NoteService()
private val logger = KotlinLogging.logger {}

fun main(){
    logger.info { "Notes App Started" }

    runMenu()
}

fun mainMenu(): Int {
    print(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List all notes            |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > """.trimMargin(">"))
    return readNextInt("==>> ")
}

fun runMenu(){
    var input : Int
    do {
        input = mainMenu()
        when(input) {
            1 -> addNote()
            2 -> listNotes()
            3 -> updateNote()
            4 -> deleteNote()
            -1 -> logger.info { "Notes App Exiting" }

            else -> println("Invalid Option")
        }
    } while (input != -1)
}

fun addNote(){
    print("Title: ")
    val title = readlnOrNull() ?: ""

    print("Body: ")
    val body = readlnOrNull() ?: ""

    print("Priority (1-5): ")
    val priority = readlnOrNull()?.toIntOrNull() ?: 1

    print("Category: ")
    val category = readlnOrNull() ?: ""

    noteService.addNote(
        Note(0, title, body, priority, category, false)
    )

    println("Note added")
}

fun listNotes() {
    println(noteService.getNotes())
}

fun deleteNote() {
    print("Enter ID to delete: ")
    val id = readln().toInt()

    if (noteService.deleteNote(id)) {
        println("Deleted")
    } else {
        println("Not found")
    }
}

fun updateNote() {
    print("Enter ID to update: ")
    val id = readln().toInt()

    print("Title: ")
    val title = readlnOrNull() ?: ""

    print("Body: ")
    val body = readlnOrNull() ?: ""

    print("Priority (1-5): ")
    val priority = readlnOrNull()?.toIntOrNull() ?: 1

    print("Category: ")
    val category = readlnOrNull() ?: ""

    print("Is archived (y/n): ")
    val isArchived = when (readlnOrNull()?.lowercase()) {
        "y", "yes", "true" -> true
        else -> false
    }

    val updated = Note(id, title, body, priority, category, isArchived)

    if (noteService.updateNote(id, updated)) {
        println("Updated")
    } else {
        println("Not found")
    }
}
