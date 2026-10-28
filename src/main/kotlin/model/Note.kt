package model

data class Note(
    var id: Int = 0,
    var title: String = "",
    var body: String = "",
    var priority: Int = 0,
    var category: String = "",
    var isArchived: Boolean = false
)
{
    override fun toString(): String {
        return "$id: $title | $body | Priority: $priority | Category: $category [${if (isArchived) "ARCHIVED" else "ACTIVE"}]"
    }
}