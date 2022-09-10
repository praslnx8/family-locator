package app.family.locator.ui.route

object ScreenRoute {

    object Login {
        const val TEMPLATE = "login"
    }

    object PermissionCheck {
        const val TEMPLATE = "permission_check"
    }

    object Home {
        const val TEMPLATE = "home"
    }

    object UserDetail {
        const val TEMPLATE = "user_detail/{user_id}"
        const val USER_ID_ARG = "user_id"

        fun getUserDetailRoute(userId: Long) = "user_detail/$userId"
    }
}
