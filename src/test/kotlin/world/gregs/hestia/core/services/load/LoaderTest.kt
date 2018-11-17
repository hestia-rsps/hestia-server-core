package world.gregs.hestia.core.services.load

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class LoaderTest {

    private lateinit var loader: Loader
    companion object {
        private const val PACKAGE = "world.gregs.hestia.core.services.load"
    }

    @BeforeEach
    fun setup() {
        loader = Loader("./src/test/kotlin/")
    }

    @Test
    fun loadClasses() {
        val classes = loader.loadClasses(PACKAGE, DummyClass::class)
        assertThat(classes).hasSize(1)
    }

    @Test
    fun loadJavaClasses() {
        val classes = loader.loadJavaClasses(PACKAGE, DummyClass::class.java)
        assertThat(classes).hasSize(1)
    }

    @Test
    fun load() {
        val classes = loader.load<DummyClass>(PACKAGE)
        assertThat(classes).hasSize(1)
    }

    @Test
    fun getPackages() {
        val packages = loader.getPackages(File("${loader.path}${PACKAGE.replace(".", "/")}/"), false)
        assertThat(packages).contains("$PACKAGE.LoaderTest")
        assertThat(packages).contains("$PACKAGE.DummyClass")
    }

    @Test
    fun invalidPath() {
        try {
            Loader(null)
            Assertions.assertFalse(true)
        } catch (i : NullPointerException) {
        }
    }

    @Test
    fun invalidPackage() {
        try {
            val loader = Loader("invalid")
            loader.load<DummyClass>("nope")
            Assertions.assertFalse(true)
        } catch (i : IllegalArgumentException) {
        }
    }
}