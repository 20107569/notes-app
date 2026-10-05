package service

import model.Note

class NoteService {

    private val notes = ArrayList<Note>()
    private var lastId = 0
    private fun getId(): Int = lastId++

    fun addNote(note: Note) {
        note.id = getId()
        notes.add(note)
    }

    fun getNotes(): List<Note> {
        return notes
    }

}

// Note how there is no print statements or read statements.
// This service class does not talk to the user.