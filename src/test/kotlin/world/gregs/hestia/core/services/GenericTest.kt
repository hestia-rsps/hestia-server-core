package world.gregs.hestia.core.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class GenericTest {

    @Test
    fun `Format a game chat message`() {
        //Given
        val message = "a CHAT MESSAGE, lengthy. wITH sentences!and questions? fun and games. :)"
        //When
        val formatted = message.formatChatMessage()
        //Then
        Assertions.assertThat(formatted).isEqualTo("A chat message, lengthy. With sentences!And questions? Fun and games. :)")
    }

    @Test
    fun `Format a username`() {
        //Given
        val message = "pLAYER user_name"
        //When
        val formatted = message.formatUsername()
        //Then
        Assertions.assertThat(formatted).isEqualTo("Player User Name")
    }
}