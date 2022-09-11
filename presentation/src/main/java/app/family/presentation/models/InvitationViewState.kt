package app.family.presentation.models

data class InvitationViewState(
    val isFetchingInviteKey: Boolean = false,
    val isJoiningFamily: Boolean = false,
    val inviteKey: String = "",
    val isJoinedFamily: Boolean = false
)