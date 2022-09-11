package app.family.domain.usecases

import app.family.api.apis.MyStatusApi
import app.family.domain.mappers.StatusMapper
import app.family.domain.models.status.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MyStatusUseCase(
    private val myStatusApi: MyStatusApi,
    private val statusMapper: StatusMapper
) {

    fun getStatus(): Flow<Status> {
        return myStatusApi.getStatus().map { statusDto ->
            statusMapper.mapFromStatusDto(statusDto)
        }
    }


}