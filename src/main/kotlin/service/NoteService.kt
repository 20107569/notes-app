package service

import persistence.Serializer
import model.Note

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

    fun getArchivedNotes(): List<Note> {
        val result = ArrayList<Note>()

        for (note in notes) {
            if (note.isArchived) {
                result.add(note)
            }
        }
        return result
    }

   fun getNotesByPriority(priority: Int): List<Note> {
       var result = ArrayList<Note>()

       for (note in notes) {
           if (note.priority == priority) {
               result.add(note)
           }
       }
       return result
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
        return notes.stream()
            .filter{note: Note -> note.isArchived}
            .count()
            .toInt()
    }

    fun numberOfActiveNotes(): Int {
        return notes.stream()
            .filter{note: Note -> !note.isArchived}
            .count()
            .toInt()
    }

    fun numberOfNotesByCategory(category: String): Int {
        return notes.stream()
            .filter { note: Note -> note.category == category }
            .count()
            .toInt()
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        return notes.stream()
            .filter { note: Note -> note.priority == priority }
            .count()
            .toInt()
    }

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

