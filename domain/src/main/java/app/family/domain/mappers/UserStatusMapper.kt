package app.family.domain.mappers

import app.family.api.models.UserStatusDto
import app.family.domain.models.status.UserStatus

class UserStatusMapper(private val statusMapper: StatusMapper) {

    fun mapFromUserStatusDto(userStatusDto: UserStatusDto): UserStatus {
        return UserStatus(
            name = userStatusDto.name,
            status = statusMapper.mapFromStatusDto(userStatusDto.statusDto)
        )
    }

}