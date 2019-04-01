package world.gregs.hestia.core.network.protocol

object ClientOpcodes {

    /* Decode */

    const val JOIN_FRIEND_CHAT = 1
    const val OBJECT_2 = 2
    const val ENTER_INTEGER = 3
    const val WIDGET_BUTTON_3 = 4
    const val MOVE_CAMERA = 5
    const val REMOVE_FRIEND = 8
    const val MOB_CLICK_1 = 9
    const val WIDGET_BUTTON_7 = 10
    const val OBJECT_1 = 11
    const val WALK = 12
    const val PLAYER_OPTION_1 = 14
    const val RECEIVE_COUNT = 15
    const val PING = 16
    const val ADD_IGNORE = 17
    const val WIDGET_BUTTON_6 = 18
    const val UNKNOWN_1 = 19
    const val WIDGET_BUTTON_10 = 20
    const val MAGIC_ON_GROUND = 21
    const val COLOUR_ID = 22
    const val CHAT_TYPE = 23
    const val ITEM_TAKE = 24
    const val WIDGET_BUTTON_8 = 25
    const val SWITCH_INTERFACE_COMPONENT = 26
    const val MOB_CLICK_4 = 28
    const val MOVE_MOUSE = 29
    const val QUICK_PUBLIC_MESSAGE = 30
    const val MOB_CLICK_2 = 31
    const val KICK_FRIEND_CHAT = 32
    const val DONE_LOADING_REGION = 33
    const val REFRESH_WORLDS = 34
    const val PUBLIC_MESSAGE = 36
    const val REMOVE_IGNORE = 38
    const val INTERFACE_ON_PLAYER = 40
    const val CHANGE_FRIEND_CHAT = 41
    const val ITEM_ON_OBJECT = 42
    const val ATTACK_TRADE_CHAT = 46
    const val OBJECT_EXAMINE = 47
    const val ADD_FRIEND = 51
    const val WIDGET_BUTTON_4 = 52
    const val PLAYER_OPTION_2 = 53
    const val DIALOGUE_CONTINUE = 54
    const val SCREEN_CLOSE = 56
    const val ONLINE_STATUS = 57
    const val STRING_ENTRY = 59
    const val WIDGET_BUTTON_1 = 61
    const val WIDGET_BUTTON_2 = 64
    const val INTERFACE_ON_MOB = 65
    const val ATTACK_MOB = 66
    const val MOB_CLICK_3 = 67
    const val KEY_TYPED = 68
    const val OBJECT_5 = 69
    const val CONSOLE_COMMAND = 70
    const val PRIVATE_MESSAGE = 72
    const val ITEM_ON_ITEM = 73
    const val IN_OUT_SCREEN = 75
    const val OBJECT_3 = 76
    const val PLAYER_TRADE_OPTION = 77
    const val QUICK_PRIVATE_MESSAGE = 79
    const val REPORT_ABUSE = 80
    const val WIDGET_BUTTON_5 = 81
    const val MINI_MAP_WALK = 83
    const val CLICK = 84
    const val PING_LATENCY = 85
    const val SCREEN_CHANGE = 87
    const val HYPERLINK_TEXT = 88
    const val WORLD_MAP_CLICK = 89
    const val WIDGET_BUTTON_9 = 91
    const val MOB_EXAMINE = 92
    const val TOGGLE_FOCUS = 93
    const val UNKNOWN = 232

    /* Encode */

    const val LOGIN_DETAILS = 2
    const val LOBBY_DETAILS = 2
    const val WIDGET_COMPONENT_SETTINGS = 3
    const val WIDGET_OPEN = 5
    const val WIDGET_ITEM = 9
    const val FRIENDS_CHAT_UPDATE = 12
    const val RUN_ENERGY = 13
    const val FILE_CONFIG = 14
    const val FRIENDS_QUICK_CHAT_MESSAGE = 20
    const val WIDGET_ANIMATION = 23
    const val FRIEND_LIST_APPEND = 24
    const val CLIENT_PING = 25
    const val WIDGET_TEXT = 33
    const val WIDGET_ITEMS = 37
    const val CONFIG_LARGE = 39
    const val FRIENDS_CHAT_MESSAGE = 40
    const val PRIVATE_QUICK_CHAT_FROM = 42
    const val REGION = 43
    const val SCRIPT = 50
    const val LOGOUT = 51
    const val IGNORE_LIST = 57
    const val LOGOUT_LOBBY = 59
    const val WIDGET_WINDOW = 67
    const val WIDGET_CLOSE = 73
    const val PRIVATE_CHAT_TO = 77
    const val FILE_CONFIG_LARGE = 84
    const val FRIEND_LIST = 85
    const val WORLD_LIST = 88
    const val PUBLIC_CHAT = 91
    const val SKILL_LEVEL = 93
    const val PRIVATE_QUICK_CHAT_TO = 97
    const val WIDGET_MOB_HEAD = 98
    const val CONFIG = 101
    const val CHAT = 102
    const val IGNORE_LIST_UPDATE = 105
    const val GLOBAL_CONFIG = 111
    const val GLOBAL_CONFIG_LARGE = 112
    const val WIDGET_PLAYER_HEAD = 114
    const val PRIVATE_CHAT_FROM = 120
    const val DYNAMIC_REGION = 128
    const val PRIVATE_STATUS = 134
    const val FRIEND_LIST_DISCONNECT = 135

    /* Handshake */
    const val LOGIN_HANDSHAKE = 14
    const val GAME_LOGIN = 16
    const val LOBBY_LOGIN = 19
}