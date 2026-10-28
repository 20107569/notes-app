package persistence

import tools.jackson.dataformat.xml.XmlMapper
import tools.jackson.module.kotlin.kotlinModule
import java.io.File

class XMLSerializer(private val file: File) : Serializer {

    private val mapper = XmlMapper.builder()
        .addModule(kotlinModule())
        .build()

    override fun write(obj: Any?) {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, obj)
    }

    override fun <T> read(classType: Class<T>): T {
        return mapper.readValue(file, classType)
    }
}
