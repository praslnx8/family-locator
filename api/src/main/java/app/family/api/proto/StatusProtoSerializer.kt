package app.family.api.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import app.family.api.models.StatusProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object StatusProtoSerializer : Serializer<StatusProto> {
    override val defaultValue: StatusProto = StatusProto.getDefaultInstance()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun readFrom(input: InputStream): StatusProto {
        try {
            return StatusProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: StatusProto, output: OutputStream) = t.writeTo(output)
}