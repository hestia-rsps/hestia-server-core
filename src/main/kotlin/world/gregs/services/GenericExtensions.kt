package world.gregs.services


/*
    Generic
 */

val Boolean.int get() = if (this) 1 else 0

fun String.plural(count: Int, plural: String = this + 's'): String {
    return if(count == 1) this else plural
}

fun String?.formatUsername(): String {
    return this?.replace(" ", "_")?.toLowerCase() ?: ""
}