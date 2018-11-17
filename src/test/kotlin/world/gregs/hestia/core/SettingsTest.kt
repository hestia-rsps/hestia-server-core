package world.gregs.hestia.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SettingsTest {

    companion object {
        private const val EMPTY_STRING = "null"
        private const val EMPTY_INT = -1
        private const val EMPTY_BOOL = false
        private const val STRING = "String"
        private const val INT = 1
        private const val BOOL = true
        private const val KEY = "test"
    }

    @BeforeEach
    fun onStart() {
        Settings.clear()
    }

    @Test
    fun load() {
        Settings.init("$KEY: '$STRING'")
        assertThat(Settings.getString(KEY)).isEqualTo(STRING)
    }

    @Test
    fun get() {
        assertThat(Settings.get(KEY, EMPTY_STRING)).isEqualTo(EMPTY_STRING)
        Settings.init("$KEY: '$STRING'")
        assertThat(Settings.get(KEY, EMPTY_STRING)).isEqualTo(STRING)
    }

    @Test
    fun getString() {
        //Empty test
        assertThat(Settings.getString(KEY, EMPTY_STRING)).isEqualTo(EMPTY_STRING)
        assertThat(Settings.getString(KEY)).isNull()
        //Direct test
        Settings.init("$KEY: $STRING")
        assertThat(Settings.getString(KEY, EMPTY_STRING)).isEqualTo(STRING)
        assertThat(Settings.getString(KEY)).isNotNull()
        //String test
        Settings.init("$KEY: \"$STRING\"")
        assertThat(Settings.getString(KEY, EMPTY_STRING)).isEqualTo(STRING)
        assertThat(Settings.getString(KEY)).isNotNull()
        //Invalid test
        Settings.init("$KEY: $INT")
        assertThat(Settings.getString(KEY, EMPTY_STRING)).isEqualTo(INT.toString())
        assertThat(Settings.getString(KEY)).isNotNull()
    }

    @Test
    fun getInt() {
        //Empty test
        assertThat(Settings.getInt(KEY, EMPTY_INT)).isEqualTo(EMPTY_INT)
        assertThat(Settings.getInt(KEY)).isNull()
        //Direct test
        Settings.init("$KEY: $INT")
        assertThat(Settings.getInt(KEY, EMPTY_INT)).isEqualTo(INT)
        assertThat(Settings.getInt(KEY)).isNotNull()
        //String test
        Settings.init("$KEY: \"$INT\"")
        assertThat(Settings.getInt(KEY, EMPTY_INT)).isEqualTo(INT)
        assertThat(Settings.getInt(KEY)).isNotNull()
        //Invalid test
        Settings.init("$KEY: '$STRING'")
        assertThat(Settings.getInt(KEY, EMPTY_INT)).isEqualTo(EMPTY_INT)
        assertThat(Settings.getInt(KEY)).isNull()
    }

    @Test
    fun getBool() {
        //Empty test
        assertThat(Settings.getBool(KEY, EMPTY_BOOL)).isEqualTo(EMPTY_BOOL)
        assertThat(Settings.getBool(KEY)).isNull()
        //Direct test
        Settings.init("$KEY: $BOOL")
        assertThat(Settings.getBool(KEY, EMPTY_BOOL)).isEqualTo(BOOL)
        assertThat(Settings.getBool(KEY)).isNotNull()
        //String test
        Settings.init("$KEY: \"$BOOL\"")
        assertThat(Settings.getBool(KEY, EMPTY_BOOL)).isEqualTo(BOOL)
        assertThat(Settings.getBool(KEY)).isNotNull()
        //Invalid test
        Settings.init("$KEY: '$STRING'")
        assertThat(Settings.getBool(KEY, EMPTY_BOOL)).isEqualTo(EMPTY_BOOL)
        assertThat(Settings.getBool(KEY)).isNull()
    }
}