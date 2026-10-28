import model.Note
import service.NoteService
import utils.readNextInt
import utils.readNextBoolean
import utils.readNextLine
import io.github.oshai.kotlinlogging.KotlinLogging
import persistence.XMLSerializer
import java.io.File


val noteService = NoteService(XMLSerializer(File("notes.xml")))
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
         > |   20) Save Notes               |
         > |   21) Load Notes               |
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
            20 -> save()
            21 -> load()
            0 -> logger.info { "Notes App Exiting" }

            else -> println("Invalid Option")
        }
    } while (input != 0)
}

fun addNote(){

    val title = readNextLine("Title: ")

    val body = readNextLine("Body: ")

    val priority = readNextInt("Priority (1-5): ")

    val category = readNextLine("Category: ")

    noteService.addNote(
        Note(0, title, body, priority, category, false)
    )

    println("Note added")
}

fun listNotes() = noteService.getNotes().forEach { println(it) }

fun deleteNote() {

    val id = readNextInt("Enter ID to delete: ")

    if (noteService.deleteNote(id)) {
        println("Deleted")
    } else {
        println("Not found")
    }
}

fun updateNote() {

    val id = readNextInt("Enter ID to update: ")

    val title = readNextLine("Title: ")

    val body = readNextLine("Body: ")

    val priority = readNextInt("Priority (1-5): ")

    val category = readNextLine("Category: ")

    val isArchived = readNextBoolean("Is archived (y/n): ")

    val updated = Note(id, title, body, priority, category, isArchived)

    if (noteService.updateNote(id, updated)) {
        println("Updated")
    } else {
        println("Not found")
    }
}

fun save() {
    try {
        noteService.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteService.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}
