package world.gregs.hestia.core.services


/*
    Generic
 */

val Boolean?.int get() = if (this == true) 1 else 0

fun String.plural(count: Int, plural: String = "s"): String {
    return if(count == 1) this else removeSuffix(plural).plus(plural)
}
/**
 * Formats messages, all lower case characters except at the start of sentences.
 * @return the formatted message
 */
fun String.formatChatMessage(): String {
    val newText = StringBuilder()
    var newSentence = true
    //For every character in the message
    for (character in this) {
        newText.append(
                //If it's the first char in a new sentence
                if (newSentence) {
                    //If it's anything but a space
                    if (character != ' ') {
                        //It's no longer a new sentence
                        newSentence = false
                    }
                    //Add as uppercase
                    character.toUpperCase()
                } else {
                    //Add as lowercase
                    character.toLowerCase()
                }
        )
        //If is the end of a line
        if (character == '.' || character == '!' || character == '?') {
            //Then start a new sentence
            newSentence = true
        }
    }
    return newText.toString()
}


/**
 * Formats a name for protocol; all lowercase, replaces all spaces with underscores
 * @return The formatted name
 */
fun String?.formatName(): String {
    return this?.toLowerCase()?.replace(" ", "_") ?: ""
}

/**
 * Formats a players username for display, uppercase on only beginning of new words
 * @return The formatted name
 */
fun String?.formatUsername(): String {
    if (this == null) {
        return ""
    }
    val formatted = StringBuilder()
    var newWord = true
    for (char in this.toLowerCase().replace("_", " ")) {
        formatted.append(if (newWord) {
            newWord = false
            char.toUpperCase()
        } else {
            char
        })
        if(char == ' ') {
            newWord = true
        }
    }
    return formatted.toString()
}

/**
 * Converts a basic string to a long
 * @return The long value
 */
fun String.toRSLong(): Long {
    var l = 0L
    var i = 0
    while (i < Math.min(this.length, 12)) {
        val c = this[i].toInt()
        l *= 37L
        when (c.toChar()) {
            in 'A'..'Z' -> l += (1 + c - 65).toLong()
            in 'a'..'z' -> l += (1 + c - 97).toLong()
            in '0'..'9' -> l += (27 + c - 48).toLong()
        }
        i++
    }
    while (l % 37L == 0L && l != 0L) {
        l /= 37L
    }
    return l
}