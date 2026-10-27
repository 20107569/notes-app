package service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import model.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested

class NoteServiceTest {

    private lateinit var emptyNoteService: NoteService
    private lateinit var populatedNoteService: NoteService
    private lateinit var note1: Note
    private lateinit var note2: Note
    private lateinit var note3: Note
    private lateinit var note4: Note
    private lateinit var note5: Note

    @BeforeEach
    fun setUp() {
        note1 = Note(0, "Test1", "Body1", 1, "Work", false)
        note2 = Note(0, "Test2", "Body2", 2, "Home", true)
        note3 = Note(0, "Test3", "Body3", 3, "College", false)
        note4 = Note(0, "Test4", "Body4", 4, "Social", true)
        // For use in tests - not added to populated list in SetUp
        note5 = Note(0, "Test5", "Body5", 5, "Hobbies", true)

        emptyNoteService = NoteService()
        populatedNoteService = NoteService()

        populatedNoteService.addNote(note1)
        populatedNoteService.addNote(note2)
        populatedNoteService.addNote(note3)
        populatedNoteService.addNote(note4)
    }

    @AfterEach
    fun tearDown() {
        println("Test completed")
    }

    @Nested
    inner class AddNotes {

        @Test
        fun `add note to an empty service increases size correctly`() {
            assertEquals(0, emptyNoteService.getNotes().size)
            emptyNoteService.addNote(note1)
            assertEquals(1, emptyNoteService.getNotes().size)
        }

        @Test
        fun `add note to an populated service increases size correctly`() {
            assertEquals(4, populatedNoteService.getNotes().size)
            populatedNoteService.addNote(note5)
            assertEquals(5, populatedNoteService.getNotes().size)
        }

        @Test
        fun `add note stores correct values`() {
            emptyNoteService.addNote(note1)

            val retrievedNote = emptyNoteService.getNotes().first()
            // Note: other functions test isArchived and id
            assertEquals(note1.title, retrievedNote.title)
            assertEquals(note1.body, retrievedNote.body)
            assertEquals(note1.priority, retrievedNote.priority)
            assertEquals(note1.category, retrievedNote.category)
        }

        @Test
        fun `add note sets isArchived to false`() {
            emptyNoteService.addNote(note1)
            emptyNoteService.addNote(note2)

            val retrievedNote1 = emptyNoteService.findNoteById(0)
            val retrievedNote2 = emptyNoteService.findNoteById(1)

            assertEquals(false, retrievedNote1?.isArchived)
            assertEquals(false, retrievedNote2?.isArchived)

        }

        @Test
        fun `ensure ids remain unique after multiple inserts`() {
            assertEquals(0, populatedNoteService.findNoteById(0)?.id)
            assertEquals(1, populatedNoteService.findNoteById(1)?.id)
            assertEquals(2, populatedNoteService.findNoteById(2)?.id)
            assertEquals(3, populatedNoteService.findNoteById(3)?.id)
        }
    }

    @Nested
    inner class GetNotes{

        @Test
        fun `getNotes returns an empty list when service is empty`() {
            assertTrue(emptyNoteService.getNotes().isEmpty())
        }

        @Test
        fun `getNotes returns all notes when service is populated`() {
            val notes = populatedNoteService.getNotes()
            assertEquals(4, notes.size)
        }

        @Test
        fun `getActiveNotes returns only non archived notes`() {
            populatedNoteService.archiveNote(note1.id)
            populatedNoteService.archiveNote(note3.id)

            val activeList = populatedNoteService.getActiveNotes()

            assertEquals(2, activeList.size)
            assertTrue(activeList.all { !it.isArchived })
        }

        @Test
        fun `getActiveNotes returns empty list when no notes are stored`() {
            val activeList = emptyNoteService.getActiveNotes()
            assertTrue(activeList.isEmpty())
        }

        @Test
        fun `getActiveNotes returns empty list when all notes are archived`() {
            populatedNoteService.archiveNote(note1.id)
            populatedNoteService.archiveNote(note2.id)
            populatedNoteService.archiveNote(note3.id)
            populatedNoteService.archiveNote(note4.id)

            val activeList = populatedNoteService.getActiveNotes()

            assertTrue(activeList.isEmpty())
        }

        @Test
        fun `getActiveNotes returns all notes when none are archived`() {
            val activeList = populatedNoteService.getActiveNotes()

            assertEquals(4, activeList.size)
            assertTrue(activeList.none { it.isArchived })
        }

        @Test
        fun `getActiveNotes preserves correct count and content`() {
            populatedNoteService.archiveNote(note2.id)

            val activeList = populatedNoteService.getActiveNotes()

            assertEquals(3, activeList.size)
            assertTrue(activeList.all { !it.isArchived })

            // Assert: correct notes are present
            assertTrue(activeList.contains(note1))
            assertTrue(activeList.contains(note3))
            assertTrue(activeList.contains(note4))
        }

        @Test
        fun `getNotesByCategory handles empty list`() {
            val result = emptyNoteService.getNotesByCategory("Work")
            assertTrue(result.isEmpty())
        }

        @Test
        fun `getNotesByCategory returns all matching notes when all match` () {
            emptyNoteService.addNote(Note(0, "A", "Body A", 1, "Work", false))
            emptyNoteService.addNote(Note(0, "B", "Body B", 2, "Work", false))
            emptyNoteService.addNote(Note(0, "C", "Body C", 3, "Work", false))

            assertEquals(3, emptyNoteService.numberOfNotes())
            val result = emptyNoteService.getNotesByCategory("Work")

            assertEquals(3, result.size)
            assertTrue(result.all { it.category == "Work" })
        }

        @Test
        fun `getNotesByCategory returns no notes when the category is not in the list`() {
            val result = populatedNoteService.getNotesByCategory("Invalid Category")
            assertTrue(result.isEmpty())
        }

        @Test
        fun `getNotesByCategory returns matching categories regardless of case` () {
            val result = populatedNoteService.getNotesByCategory("home")
            assertEquals(1, result.size)
            assertTrue(result.all { it.category == "Home" })
        }

        @Test
        fun `getNotesByCategory excludes notes whose categories do not match` () {
            assertEquals(4, populatedNoteService.numberOfNotes())

            val result = populatedNoteService.getNotesByCategory("Work")
            assertEquals(1, result.size)

            assertTrue(result.contains(note1))
            assertFalse(result.contains(note2))
            assertFalse(result.contains(note3))
            assertFalse(result.contains(note4))
        }

        // getArchivedNotes

        @Test
        fun `getArchivedNotes returns an empty list when service is empty` () {
            val result = emptyNoteService.getArchivedNotes()
            assertTrue(result.isEmpty())
        }

        @Test
        fun `getArchivedNotes returns an empty list when no notes are archived` () {
            val result = populatedNoteService.getArchivedNotes()
            assertTrue(result.isEmpty())
        }

        @Test
        fun `getArchivedNotes returns only notes that are archived` () {
            populatedNoteService.archiveNote(note1.id)
            populatedNoteService.archiveNote(note3.id)

            val result = populatedNoteService.getArchivedNotes()

            assertEquals(2, result.size)
            assertTrue(result.all { it.isArchived })
            assertTrue(result.contains(note1))
            assertTrue(result.contains(note3))
        }

        // getNotesByPriority

        @Test
        fun `getNotesByPriority returns an empty list when service is empty` () {
            val result = emptyNoteService.getNotesByPriority(1)
            assertTrue(result.isEmpty())
        }

        @Test
        fun `getNotesByPriority returns an empty list when priority does not exist` () {
            val result = populatedNoteService.getNotesByPriority(5)
            assertTrue(result.isEmpty())
        }

        @Test
        fun `getNotesByPriority returns only matching notes` () {
            val result = populatedNoteService.getNotesByPriority(1)
            assertEquals(1, result.size)
            assertTrue(result.contains(note1))
            assertFalse(result.contains(note2))
            assertFalse(result.contains(note3))
            assertFalse(result.contains(note4))
        }
    }

    @Nested
    inner class DeleteNotes{
        //deleting an existing note returns true
        @Test
        fun `deleting an existing note returns true`() {
            assertTrue(populatedNoteService.deleteNote(1))
        }

        //deleting a non-existent note returns false
        @Test
        fun `deleting a non-existent note returns false`(){
            assertFalse(populatedNoteService.deleteNote(5))
        }

        //deleting from an empty service returns false
        @Test
        fun `deleting from an empty service returns false`() {
            assertFalse(emptyNoteService.deleteNote(1))
        }

        //deleting an existing note reduces the number of notes by one
        @Test
        fun `deleting an existing note reduces the number of notes by one`(){
            val sizeBefore = populatedNoteService.getNotes().size
            populatedNoteService.deleteNote(3)
            assertEquals(sizeBefore - 1, populatedNoteService.getNotes().size)

        }

        //deleting an existing note removes the correct note
        @Test
        fun `deleting an existing note removes the correct note`(){
            populatedNoteService.deleteNote(1)
            assertFalse(populatedNoteService.getNotes().any {it.id == 1})
        }

        //deleting an existing note does not remove other notes
        @Test
        fun `deleting one note does not remove other notes`() {
            populatedNoteService.deleteNote(1)
            assertTrue(populatedNoteService.getNotes().any { it.id == 0 })
            assertTrue(populatedNoteService.getNotes().any { it.id == 2 })
            assertTrue(populatedNoteService.getNotes().any { it.id == 3 })
        }

    }

    @Nested
    inner class UpdateNotes {
        //updating an existing note returns true
        @Test
        fun `updating an existing note returns true`() {
            val updated = Note(1,"Updated Title", "Updated Body", 5, "Work", true)
            assertTrue(populatedNoteService.updateNote(1, updated))
        }

        //updating a non-existent note returns false
        @Test
        fun `updating a non-existent note returns false`() {
            val updated = Note(100, "Updated Title", "Updated Body", 5, "Work", false)
            assertFalse(populatedNoteService.updateNote(100, updated))
        }

        //updating from an empty service returns false
        @Test
        fun `updating from an empty service returns false`() {
            val updated = Note(1,"Updated Title", "Updated Body", 5, "Work", false)
            assertFalse(emptyNoteService.updateNote(1, updated))
        }

        //updating an existing note does not change the number of notes
        @Test
        fun `updating an existing note does not change the number of notes`() {
            val sizeBefore = populatedNoteService.getNotes().size

            val updated = Note(1,"Updated Title", "Updated Body", 5, "Work", true)
            populatedNoteService.updateNote(1, updated)
            assertEquals(sizeBefore, populatedNoteService.getNotes().size)
        }

        //updating an existing note changes it's stored values
        @Test
        fun `updating an existing note changes it's stored values`() {
            val updated = Note(1, "Updated Title", "Updated Body", 5, "Personal", true)

            populatedNoteService.updateNote(1, updated)

            val retrieved = populatedNoteService.getNotes()[1]
            assertEquals("Updated Title", retrieved.title)
            assertEquals("Updated Body", retrieved.body)
            assertEquals(5, retrieved.priority)
            assertEquals("Personal", retrieved.category)
            assertTrue(retrieved.isArchived)
        }

        //updating an existing note preserves it's ID
        @Test
        fun `updating an existing note preserves it's ID`() {
            val sizeBefore = populatedNoteService.getNotes().size
            val updated = Note(1, "Updated Title", "Updated Body", 5, "Personal", true)
            populatedNoteService.updateNote(1, updated)

            assertEquals(1, populatedNoteService.findNoteById(1)?.id)
        }

    }

    @Nested
    inner class CountingNotes {
        @Test
        fun `numberOfNotes returns correct total count for populated service`(){
            assertEquals(4, populatedNoteService.numberOfNotes())
        }

        @Test
        fun `numberOfNotes returns zero for empty service`(){
            assertEquals(0, emptyNoteService.numberOfNotes())
        }

        @Test
        fun `numberOfArchivedNotes returns correct count after archiving notes`() {
            assertTrue(populatedNoteService.archiveNote(2))
            assertTrue(populatedNoteService.archiveNote(3))
            assertEquals(2, populatedNoteService.numberOfArchivedNotes())
        }

        @Test
        fun `numberOfArchivedNotes returns zero when no notes are archived`() {
            // new notes are always set to active
            emptyNoteService.addNote(note1)
            emptyNoteService.addNote(note2)
            emptyNoteService.addNote(note3)

            assertEquals(0, emptyNoteService.numberOfArchivedNotes())
        }

        @Test
        fun `numberOfActiveNotes returns correct count when all notes are still active`() {
            // new notes are always added as active, regardless.
            assertEquals(4, populatedNoteService.numberOfActiveNotes())
        }

        @Test
        fun `numberOfActiveNotes returns zero when all notes are archived`() {
            // new notes are always set to active
            emptyNoteService.addNote(note1)
            emptyNoteService.addNote(note2)
            emptyNoteService.addNote(note3)

            // archive all notes
            emptyNoteService.archiveNote(note1.id)
            emptyNoteService.archiveNote(note2.id)
            emptyNoteService.archiveNote(note3.id)

            assertEquals(0, emptyNoteService.numberOfActiveNotes())
        }

        @Test
        fun `numberOfNotesByCategory returns correct count for Work`() {
            assertEquals(1, populatedNoteService.numberOfNotesByCategory("Work"))
        }

        @Test
        fun `numberOfNotesByCategory returns zero when category does not exist`() {
            assertEquals(0, populatedNoteService.numberOfNotesByCategory("Travel"))
        }

        @Test
        fun `numberOfNotesByPriority returns zero when priority does not exist`() {
            assertEquals(0, populatedNoteService.numberOfNotesByPriority(99))
        }
    }
}
