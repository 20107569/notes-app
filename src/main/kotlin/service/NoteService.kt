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

    fun findNoteById(id: Int): Note? {
        return notes.find { it.id == id }
    }

}

// Note how there is no print statements or read statements.
// This service class does not talk to the user.

