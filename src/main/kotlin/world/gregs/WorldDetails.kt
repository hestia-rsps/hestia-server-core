package world.gregs

class WorldDetails(val activity: String, val ip: String, val region: String, val country: Int, val location: Int = 0, val flag: Int) {
    var size = 0
    var id = -1

    constructor(activity: String, ip: String, region: String, country: Country/*, location: Int*/, vararg settings: Setting) : this(activity, ip, region, country.id, country.id/*, location*/, getFlag(settings))

    enum class Country(val id: Int) {
        AUSTRALIA(16),
        BELGIUM(22),
        BRAZIL(31),
        CANADA(38),
        DENMARK(58),
        FINLAND(69),
        IRELAND(101),
        MEXICO(152),
        NETHERLANDS(161),
        NORWAY(162),
        SWEDEN(191),
        UK(77),
        USA(225);
    }

    enum class Setting(val mask: Int) {
        NON_MEMBERS(0x0),
        MEMBERS(0x1),
        PVP(0x4),
        LOOT_SHARE(0x8),
        HIGHLIGHT(0x10),
        HIGH_RISK(0x400);
    }

    companion object {
        private fun getFlag(settings: Array<out Setting>): Int {
            var flags = 0
            settings.forEach { flags = flags or it.mask }
            return flags
        }
    }
}