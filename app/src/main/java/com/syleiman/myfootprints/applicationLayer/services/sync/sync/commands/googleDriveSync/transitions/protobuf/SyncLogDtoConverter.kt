package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions.protobuf

import com.google.protobuf.ByteString
import com.google.protobuf.InvalidProtocolBufferException
import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto

/** Convert from Protobuf to SyncLogDto and back  */
object SyncLogDtoConverter {
    /** Convert from bytes to log record  */
    fun fromBytes(sourceData: ByteArray): SyncLogDto? {
        val message = extractSyncMessage(sourceData) ?: return null

        val result = SyncLogDto(0, message.operationType, message.entityType, message.entityUid)

        val strValues = message.fieldStrCount
        for (i in 0..strValues - 1)
            result.addValue(message.getFieldStr(i), message.getValueStr(i))

        val binValues = message.fieldBinCount
        for (i in 0..binValues - 1) {
            var value: ByteArray? = message.getValueBin(i).toByteArray()

            if (value!!.isEmpty())          // See comment in @toBytes method
                value = null

            result.addBinValue(message.getFieldBin(i), value)
        }

        //Log.d(LogTags.Sync, result.toLogString("Received record: "));

        return result
    }

    /** Convert log record to bytes  */
    fun toBytes(sourceData: SyncLogDto): ByteArray {
        //Log.d(LogTags.Sync, sourceData.toLogString("Record to send: "));

        val messageBuilder = SyncMessageProtobuf.SyncMessage.newBuilder().setOperationType(sourceData.operationType).setEntityType(sourceData.entityType).setEntityUid(sourceData.entityUid)

        for ((key, value1) in sourceData.strValues) {
            val value = value1 ?: continue          // Avoid NullPointerException in protobuf

//Log.d(LogTags.SYNC_PROCESS, "Key: "+key);
            messageBuilder.addFieldStr(key)
            messageBuilder.addValueStr(value)
        }

        for (entry in sourceData.binValues.entries) {
            messageBuilder.addFieldBin(entry.key)

            var value: ByteArray? = entry.value
            if (value == null)          // I think it is a bug in Protobuf. It raises "NullPointerException: Attempt to get length of null array" if we use null array
                value = ByteArray(0)

            messageBuilder.addValueBin(ByteString.copyFrom(value))
        }

        return messageBuilder.build().toByteArray()
    }

    /** Extract protobuf message from bytes  */
    private fun extractSyncMessage(sourceData: ByteArray?): SyncMessageProtobuf.SyncMessage? {
        if (sourceData == null)
            return null

        try {
            return SyncMessageProtobuf.SyncMessage.parseFrom(sourceData)
        } catch (e: InvalidProtocolBufferException) {
            e.printStackTrace()
            return null
        }

    }
}