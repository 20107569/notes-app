package persistence

import tools.jackson.databind.ObjectMapper
import java.io.File

class JSONSerializer(private val file: File) : Serializer {

    private val mapper = ObjectMapper()

    override fun write(obj: Any?) {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, obj)
    }

    override fun <T> read(classType: Class<T>): T {
        return mapper.readValue(file, classType)
    }
}
