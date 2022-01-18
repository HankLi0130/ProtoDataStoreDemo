package app.hankdev.protodatastoredemo

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import app.hankdev.protodatastoredemo.proto.User
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

val Context.dataStore: DataStore<User> by dataStore(
    fileName = "user.pb",
    serializer = UserSerializer
)

object UserSerializer : Serializer<User> {
    override val defaultValue: User
        get() = User.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): User {
        try {
            return User.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: User, output: OutputStream) = t.writeTo(output)
}
