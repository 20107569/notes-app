package service

import persistence.Serializer
import model.Note
import java.util.stream.Collectors

class NoteService (serializerType: Serializer){

    private var serializer = serializerType
    private var notes = ArrayList<Note>()
    private var lastId = 0
    private fun getId(): Int = lastId++

    fun addNote(note: Note) {
        note.id = getId()
        note.isArchived = false
        notes.add(note)
    }

    fun getNotes(): List<Note> {
        return notes
    }

    fun deleteNote(id: Int): Boolean {
        return notes.removeIf { it.id == id }
    }

    fun updateNote(id: Int, updated: Note): Boolean {
        val index = notes.indexOfFirst { it.id == id }
        if (index != -1) {
            updated.id = id
            notes[index] = updated
            return true
        }
        return false
    }

    fun archiveNote(id: Int): Boolean {
        val note = notes.find { it.id == id && !it.isArchived }

        if (note != null) {
            note.isArchived = true
            return true
        }
        return false
    }

    fun getArchivedNotes(): List<Note> =
        notes.filter { it.isArchived }


   fun getNotesByPriority(priority: Int): List<Note> =
       notes.filter { it.priority == priority }


    fun getActiveNotes(): List<Note> =
        notes.filter { !it.isArchived }

    fun getNotesByCategory(category: String): List<Note> =
        notes.filter { it.category.equals(category, ignoreCase = true) }


    fun findNoteById(id: Int): Note? {
        return notes.find { it.id == id }
    }

    fun searchNotesByContent(content: String): List<Note> =
        notes.filter {
            it.title.contains(content, ignoreCase = true) ||
                    it.body.contains(content, ignoreCase = true)
        }


    fun numberOfNotes(): Int = notes.size

    fun numberOfArchivedNotes() = notes.count{ it.isArchived }

    fun numberOfActiveNotes() = notes.count { !it.isArchived }

    fun numberOfNotesByCategory(category: String): Int = notes.count { it.category.equals(category, ignoreCase = true) }

    fun numberOfNotesByPriority(priority: Int): Int = notes.count { it.priority == priority }

    fun load() {
        val array = serializer.read(Array<Note>::class.java)
        notes = array.toCollection(ArrayList())
    }

    //@Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }

}

// Note how there is no print statements or read statements.
// This service class does not talk to the user.

