package world.gregs.services

import org.slf4j.LoggerFactory
import java.io.File
import java.util.regex.Pattern
import kotlin.reflect.KClass

class Loader(private val path: String) {

    private var pattern: Pattern = convertToPattern(path)

    private val logger = LoggerFactory.getLogger(Loader::class.java)

    fun <T : Any> load(packageName: String, c: KClass<T>, recursive: Boolean = true): List<T> {
        return load(packageName, c.java, recursive)
    }

    fun <T> load(packageName: String, c: Class<T>, recursive: Boolean = true): List<T> {
        val file = File("$path${packageName.replace(".", "/")}/")
        return if (file.exists()) {
            //Get all package names
            val packages = getPackages(file, recursive)

            //Get classes for each
            val classes = packages.map { Class.forName(it, false, ClassLoader.getSystemClassLoader()) }

            //Instantiate all classes
            classes.asSequence().filter { c.isAssignableFrom(it) }.map { it.getDeclaredConstructor().newInstance() }.filterIsInstance(c).toList()
        } else {
            logger.warn("Invalid plugin path: ${file.absolutePath}")
            emptyList()
        }
    }

    fun <T : Class<*>> loadClasses(packageName: String, c: T, recursive: Boolean = true): List<T> {
        val file = File("$path${packageName.replace(".", "/")}/")
        return if (file.exists()) {
            //Get all package names
            val packages = getPackages(file, recursive)

            //Get classes for each
            val classes = packages.map { Class.forName(it) }

            //Instantiate all classes
            classes.asSequence().filter { c.isAssignableFrom(it) }.toList() as List<T>
        } else {
            logger.warn("Invalid plugin path: ${file.absolutePath}")
            emptyList()
        }
    }

    private fun getPackages(dir: File, recursive: Boolean): List<String> {
        val files = arrayListOf<String>()
        if (dir.isDirectory) {
            dir.listFiles().forEach { file ->
                if (file.isFile) {
                    val str = getPackage(file.path) ?: return files
                    files.add(str)
                } else if (file.isDirectory && recursive) {
                    files.addAll(getPackages(file, true))
                }
            }
        } else if (dir.isFile) {
            val str = getPackage(dir.path) ?: return files
            files.add(str)
        }
        return files
    }

    private fun getPackage(string: String): String? {
        var input = string
        //Remove forward slashes
        if (input.contains("/"))
            input = input.replace("/", "\\")

        //Match against file type & location
        val matcher = pattern.matcher(input)
        if (matcher.matches()) {
            return matcher.group(1).replace("\\", ".")
        }

        return null
    }

    private fun convertToPattern(directory: String, extensions: Array<String> = arrayOf("kt", "java")): Pattern {
        var dir = directory
        //Replace forward slashes with backslash
        if (dir.contains("/")) {
            dir = dir.replace("/", "\\")
        }

        //Replace backslash with both slash regex
        dir = dir.replace("\\", "[/\\\\]")

        //Escape dots
        dir = dir.replace(".", "\\.")

        //Build pattern
        return Pattern.compile("$dir(.*)\\.(?:${extensions.joinToString("|")})")
    }
}