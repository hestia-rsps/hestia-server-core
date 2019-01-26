package world.gregs.hestia.core.world

interface Details {
    /**
     * Worlds id
     */
    var id: Int

    /**
     * Number of players online
     */
    var size: Int

    /**
     * Server's ip address
     */
    val ip: String

    /**
     * Server region description
     */
    val region: String

    /**
     * Server country
     */
    val country: Int

    /**
     * Server location
     */
    val location: Int

    /**
     * Special activity world description
     */
    val activity: String

    /**
     * World type flag (mem, pvp, loot etc...)
     */
    val flag: Int
}