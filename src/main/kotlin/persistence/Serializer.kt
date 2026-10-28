package persistence

interface Serializer {
    fun write(obj: Any?)

    fun <T> read(classType: Class<T>): T

}