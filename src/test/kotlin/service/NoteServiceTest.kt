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

            val retrievedNote1 = emptyNoteService.getNotes()[0]
            val retrievedNote2 = emptyNoteService.getNotes()[1]

            assertEquals(false, retrievedNote1.isArchived)
            assertEquals(false, retrievedNote2.isArchived)
        }

        @Test
        fun `ensure ids remain unique after multiple inserts`() {
            assertEquals(0, populatedNoteService.getNotes()[0].id)
            assertEquals(1, populatedNoteService.getNotes()[1].id)
            assertEquals(2, populatedNoteService.getNotes()[2].id)
            assertEquals(3, populatedNoteService.getNotes()[3].id)
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
    }

}
