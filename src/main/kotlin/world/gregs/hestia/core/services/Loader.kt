package world.gregs.hestia.core.services

import java.io.File
import java.util.regex.Pattern
import kotlin.reflect.KClass

open class Loader(val path: String?) {

    private var pattern: Pattern = convertToPattern(path ?: throw NullPointerException("Loader path mustn't be null."))

    inline fun <reified T : Any> load(packageName: String, recursive: Boolean = true): List<T> {
        return loadJavaClasses(packageName, T::class.java, recursive).map { it.getDeclaredConstructor().newInstance() }
    }

    inline fun <reified T : KClass<*>> loadClasses(packageName: String, type: T, recursive: Boolean = true): List<T> {
        return loadJavaClasses(packageName, type.java, recursive).map { it::class }.filterIsInstance<T>().toList()
    }

    inline fun <reified T : Class<*>> loadJavaClasses(packageName: String, type: T, recursive: Boolean = true): List<T> {
        val file = File("$path${packageName.replace(".", "/")}/")
        return if (file.exists()) {
            //Get all package names
            val packages = getPackages(file, recursive)

            //Get classes for each
            val classes = packages.map { Class.forName(it, false, ClassLoader.getSystemClassLoader()) }

            //Instantiate all classes
            classes.asSequence().filter { type.isAssignableFrom(it) }.filterIsInstance<T>().toList()
        } else {
            throw IllegalArgumentException("Invalid plugin path: ${file.absolutePath}")
        }
    }

    fun getPackages(dir: File, recursive: Boolean): List<String> {
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