package service

import model.Note

class NoteService {

    private val notes = ArrayList<Note>()
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
        for (note in notes) {
            if (note.id == id && !note.isArchived) {
                note.isArchived = true
                return true
            }
        }
        return false
    }

    fun getActiveNotes(): List<Note> {
        val result = ArrayList<Note>()

        for (note in notes) {
            if (!note.isArchived) {
                result.add(note)
            }
        }

        return result
    }

    fun getNotesByCategory(category: String): List<Note> {
        val result = ArrayList<Note>()

        for (note in notes) {
            if (note.category.equals(category, ignoreCase = true)) {
                result.add(note)
            }
        }
        return result
    }

    fun findNoteById(id: Int): Note? {
        return notes.find { it.id == id }
    }

    fun numberOfNotes(): Int = notes.size

    fun numberOfArchivedNotes(): Int {
        var count = 0

        for (note in notes) {
            if (note.isArchived) {
                count++
            }
        }
        return count
    }

    fun numberOfActiveNotes(): Int {
        var count = 0

        for (note in notes) {
            if (!note.isArchived) {
                count++
            }
        }
        return count
    }

    fun numberOfNotesByCategory(category: String): Int {
        var count = 0

        for (note in notes) {
            if (note.category == category) {
                count++
            }
        }
        return count
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        var count = 0

        for (note in notes) {
            if (note.priority == priority) {
                count++
            }
        }
        return count
    }

}

// Note how there is no print statements or read statements.
// This service class does not talk to the user.

