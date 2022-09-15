package app.family.api.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import app.family.api.models.StatusCollectionProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object StatusCollectionProtoSerializer : Serializer<StatusCollectionProto> {
    override val defaultValue: StatusCollectionProto = StatusCollectionProto.getDefaultInstance()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun readFrom(input: InputStream): StatusCollectionProto {
        try {
            return StatusCollectionProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: StatusCollectionProto, output: OutputStream) = t.writeTo(output)
}