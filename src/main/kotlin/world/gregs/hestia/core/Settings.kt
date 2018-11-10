package world.gregs.hestia.core

import org.yaml.snakeyaml.Yaml
import java.io.File

object Settings {

    private var map: Map<String, Any>? = null
    private var yaml: Yaml? = null

    fun load(path: String = "./settings.yaml") {
        if(yaml == null) {
            yaml = Yaml()
        }

        val file = File(path)
        if(file.exists()) {
            val stream = file.inputStream()
            map = yaml?.load<Map<String, Any>>(stream)
        }
    }

    fun <T: Any?> get(key: String, default: T): T {
        return if(map?.containsKey(key) == true) {
            map!![key] as T
        } else {
            default
        }
    }
}