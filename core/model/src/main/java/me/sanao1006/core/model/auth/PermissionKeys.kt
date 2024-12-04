package me.sanao1006.core.model.auth

object PermissionKeys {
    const val READ_ACCOUNT = "read:account"
    const val WRITE_ACCOUNT = "write:account"
    const val READ_BLOCKS = "read:blocks"
    const val WRITE_BLOCKS = "write:blocks"
    const val READ_DRIVE = "read:drive"
    const val WRITE_DRIVE = "write:drive"
    const val READ_FAVORITES = "read:favorites"
    const val WRITE_FAVORITES = "write:favorites"
    const val READ_FOLLOWING = "read:following"
    const val WRITE_FOLLOWING = "write:following"
    const val READ_MESSAGING = "read:messaging"
    const val WRITE_MESSAGING = "write:messaging"
    const val READ_MUTES = "read:mutes"
    const val WRITE_MUTES = "write:mutes"
    const val WRITE_NOTES = "write:notes"
    const val READ_NOTIFICATIONS = "read:notifications"
    const val WRITE_NOTIFICATIONS = "write:notifications"
    const val READ_REACTIONS = "read:reactions"
    const val WRITE_REACTIONS = "write:reactions"
    const val WRITE_VOTES = "write:votes"
    const val READ_PAGES = "read:pages"
    const val WRITE_PAGES = "write:pages"
    const val WRITE_PAGE_LIKES = "write:page-likes"
    const val READ_PAGE_LIKES = "read:page-likes"
    const val READ_USER_GROUPS = "read:user-groups"
    const val WRITE_USER_GROUPS = "write:user-groups"
    const val READ_CHANNELS = "read:channels"
    const val WRITE_CHANNELS = "write:channels"
    const val READ_GALLERY = "read:gallery"
    const val WRITE_GALLERY = "write:gallery"
    const val READ_GALLERY_LIKES = "read:gallery-likes"
    const val WRITE_GALLERY_LIKES = "write:gallery-likes"
    const val READ_FLASH = "read:flash"
    const val WRITE_FLASH = "write:flash"
    const val READ_FLASH_LIKES = "read:flash-likes"
    const val WRITE_FLASH_LIKES = "write:flash-likes"
    const val READ_ADMIN_ABUSE_USER_REPORTS = "read:admin:abuse-user-reports"
    const val WRITE_ADMIN_DELETE_ACCOUNT = "write:admin:delete-account"
    const val WRITE_ADMIN_DELETE_ALL_FILES_OF_A_USER = "write:admin:delete-all-files-of-a-user"
    const val READ_ADMIN_INDEX_STATS = "read:admin:index-stats"
    const val READ_ADMIN_TABLE_STATS = "read:admin:table-stats"
    const val READ_ADMIN_USER_IPS = "read:admin:user-ips"
    const val READ_ADMIN_META = "read:admin:meta"
    const val WRITE_ADMIN_RESET_PASSWORD = "write:admin:reset-password"
    const val WRITE_ADMIN_RESOLVE_ABUSE_USER_REPORT = "write:admin:resolve-abuse-user-report"
    const val WRITE_ADMIN_SEND_EMAIL = "write:admin:send-email"
    const val READ_ADMIN_SERVER_INFO = "read:admin:server-info"
    const val READ_ADMIN_SHOW_MODERATION_LOG = "read:admin:show-moderation-log"
    const val READ_ADMIN_SHOW_USER = "read:admin:show-user"
    const val WRITE_ADMIN_SUSPEND_USER = "write:admin:suspend-user"
    const val WRITE_ADMIN_UNSET_USER_AVATAR = "write:admin:unset-user-avatar"
    const val WRITE_ADMIN_UNSET_USER_BANNER = "write:admin:unset-user-banner"
    const val WRITE_ADMIN_UNSUSPEND_USER = "write:admin:unsuspend-user"
    const val WRITE_ADMIN_META = "write:admin:meta"
    const val WRITE_ADMIN_USER_NOTE = "write:admin:user-note"
    const val WRITE_ADMIN_ROLES = "write:admin:roles"
    const val READ_ADMIN_ROLES = "read:admin:roles"
    const val WRITE_ADMIN_RELAYS = "write:admin:relays"
    const val READ_ADMIN_RELAYS = "read:admin:relays"
    const val WRITE_ADMIN_INVITE_CODES = "write:admin:invite-codes"
    const val READ_ADMIN_INVITE_CODES = "read:admin:invite-codes"
    const val WRITE_ADMIN_ANNOUNCEMENTS = "write:admin:announcements"
    const val READ_ADMIN_ANNOUNCEMENTS = "read:admin:announcements"
    const val WRITE_ADMIN_AVATAR_DECORATIONS = "write:admin:avatar-decorations"
    const val READ_ADMIN_AVATAR_DECORATIONS = "read:admin:avatar-decorations"
    const val WRITE_ADMIN_FEDERATION = "write:admin:federation"
    const val WRITE_ADMIN_ACCOUNT = "write:admin:account"
    const val READ_ADMIN_ACCOUNT = "read:admin:account"
    const val WRITE_ADMIN_EMOJI = "write:admin:emoji"
    const val READ_ADMIN_EMOJI = "read:admin:emoji"
    const val WRITE_ADMIN_QUEUE = "write:admin:queue"
    const val READ_ADMIN_QUEUE = "read:admin:queue"
    const val WRITE_ADMIN_PROMO = "write:admin:promo"
    const val WRITE_ADMIN_DRIVE = "write:admin:drive"
    const val READ_ADMIN_DRIVE = "read:admin:drive"
    const val WRITE_ADMIN_AD = "write:admin:ad"
    const val READ_ADMIN_AD = "read:admin:ad"
    const val WRITE_INVITE_CODES = "write:invite-codes"
    const val READ_INVITE_CODES = "read:invite-codes"
    const val WRITE_CLIP_FAVORITE = "write:clip-favorite"
    const val READ_CLIP_FAVORITE = "read:clip-favorite"
    const val READ_FEDERATION = "read:federation"
    const val WRITE_REPORT_ABUSE = "write:report-abuse"

    fun getAllPermissions(): List<String> {
        return listOf(
            READ_ACCOUNT,
            WRITE_ACCOUNT,
            READ_BLOCKS,
            WRITE_BLOCKS,
            READ_DRIVE,
            WRITE_DRIVE,
            READ_FAVORITES,
            WRITE_FAVORITES,
            READ_FOLLOWING,
            WRITE_FOLLOWING,
            READ_MESSAGING,
            WRITE_MESSAGING,
            READ_MUTES,
            WRITE_MUTES,
            WRITE_NOTES,
            READ_NOTIFICATIONS,
            WRITE_NOTIFICATIONS,
            READ_REACTIONS,
            WRITE_REACTIONS,
            WRITE_VOTES,
            READ_PAGES,
            WRITE_PAGES,
            WRITE_PAGE_LIKES,
            READ_PAGE_LIKES,
            READ_USER_GROUPS,
            WRITE_USER_GROUPS,
            READ_CHANNELS,
            WRITE_CHANNELS,
            READ_GALLERY,
            WRITE_GALLERY,
            READ_GALLERY_LIKES,
            WRITE_GALLERY_LIKES,
            READ_FLASH,
            WRITE_FLASH,
            READ_FLASH_LIKES,
            WRITE_FLASH_LIKES,
            READ_ADMIN_ABUSE_USER_REPORTS,
            WRITE_ADMIN_DELETE_ACCOUNT,
            WRITE_ADMIN_DELETE_ALL_FILES_OF_A_USER,
            READ_ADMIN_INDEX_STATS,
            READ_ADMIN_TABLE_STATS,
            READ_ADMIN_USER_IPS,
            READ_ADMIN_META,
            WRITE_ADMIN_RESET_PASSWORD,
            WRITE_ADMIN_RESOLVE_ABUSE_USER_REPORT,
            WRITE_ADMIN_SEND_EMAIL,
            READ_ADMIN_SERVER_INFO,
            READ_ADMIN_SHOW_MODERATION_LOG,
            READ_ADMIN_SHOW_USER,
            WRITE_ADMIN_SUSPEND_USER,
            WRITE_ADMIN_UNSET_USER_AVATAR,
            WRITE_ADMIN_UNSET_USER_BANNER,
            WRITE_ADMIN_UNSUSPEND_USER,
            WRITE_ADMIN_META,
            WRITE_ADMIN_USER_NOTE,
            WRITE_ADMIN_ROLES,
            READ_ADMIN_ROLES,
            WRITE_ADMIN_RELAYS,
            READ_ADMIN_RELAYS,
            WRITE_ADMIN_INVITE_CODES,
            READ_ADMIN_INVITE_CODES,
            WRITE_ADMIN_ANNOUNCEMENTS,
            READ_ADMIN_ANNOUNCEMENTS,
            WRITE_ADMIN_AVATAR_DECORATIONS,
            READ_ADMIN_AVATAR_DECORATIONS,
            WRITE_ADMIN_FEDERATION,
            WRITE_ADMIN_ACCOUNT,
            READ_ADMIN_ACCOUNT,
            WRITE_ADMIN_EMOJI,
            READ_ADMIN_EMOJI,
            WRITE_ADMIN_QUEUE,
            READ_ADMIN_QUEUE,
            WRITE_ADMIN_PROMO,
            WRITE_ADMIN_DRIVE,
            READ_ADMIN_DRIVE,
            WRITE_ADMIN_AD,
            READ_ADMIN_AD,
            WRITE_INVITE_CODES,
            READ_INVITE_CODES,
            WRITE_CLIP_FAVORITE,
            READ_CLIP_FAVORITE,
            READ_FEDERATION,
            WRITE_REPORT_ABUSE
        )
    }
}
