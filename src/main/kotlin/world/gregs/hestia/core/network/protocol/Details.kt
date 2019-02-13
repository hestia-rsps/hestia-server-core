package world.gregs.hestia.core.network.protocol

/**
 * World details
 * @param id The world id
 * @param size Number of players online
 * @param ip Server's ip address
 * @param region Server region description
 * @param country Server country
 * @param location Server location
 * @param activity Special activity world description
 * @param flag World type flag (mem, pvp, loot etc...)
 */
open class Details(var id: Int, var size: Int, val ip: String, val region: String, val country: Int, val location: Int, val activity: String, val flag: Int)