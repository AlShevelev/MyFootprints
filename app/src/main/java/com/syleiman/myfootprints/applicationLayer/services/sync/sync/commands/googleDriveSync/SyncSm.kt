package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync

import android.util.Log
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.common.create2DArray
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncResult
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions.*
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.notifications.IForegroundManager
import java.sql.SQLException

/** State machine for sync  */
class SyncSm
/**  */
(
    private val footprints : IFootprintsService,
    private val options : IOptionsCacheService,
    private val sysInfo : SystemInformationService,
    private val localDb: ILocalDbService,
    private val locker: Any,
    private val foregroundManager: IForegroundManager,
    private val syncResult: SyncResult) : ISyncSmProcess
{
    private var currentState = States.Initial

    // row - states; cols - events
    private var transitionsMatrix: Array<Array<TransitionInfo>>? = null

    init
    {

        createMatrix()
    }

    /**
     * Create transitions matrix
     */
    private fun createMatrix()
    {
        val syncContext = SyncContext()

        val invalidTransition = InvalidTransition()

        transitionsMatrix = create2DArray(6, 5){TransitionInfo(invalidTransition, States.Final)}

        val completeWithDisconnectTransition = CompleteWithDisconnectTransition(options, sysInfo, localDb, syncContext, locker)
        val completeWithoutDisconnectTransition = CompleteWithoutDisconnectTransition(options, sysInfo, localDb, locker)
        val prepareDataItemsTransition = PrepareDataItemsTransition(options, sysInfo, localDb, syncContext, this, foregroundManager)
        val processExtRecordTransition = ProcessExternalRecordTransition(footprints, options, sysInfo, localDb, syncContext, this, foregroundManager, syncResult)
        val sendLogRecordTransition = SendLogRecordTransition(options, sysInfo, localDb, syncContext, this, foregroundManager)
        val startConnectTransition = StartConnectTransition(options, sysInfo, localDb, syncContext, this, foregroundManager)

        transitionsMatrix!![States.Initial.value][Events.StartConnect.value] = TransitionInfo(startConnectTransition, States.Connecting)

        transitionsMatrix!![States.Connecting.value][Events.Error.value] = TransitionInfo(completeWithoutDisconnectTransition, States.Final)
        transitionsMatrix!![States.Connecting.value][Events.Connected.value] = TransitionInfo(prepareDataItemsTransition, States.GetFiles)

        transitionsMatrix!![States.GetFiles.value][Events.Error.value] = TransitionInfo(completeWithDisconnectTransition, States.Final)
        transitionsMatrix!![States.GetFiles.value][Events.ProcessCompleted.value] = TransitionInfo(sendLogRecordTransition, States.SendLogRecords)

        transitionsMatrix!![States.SendLogRecords.value][Events.Error.value] = TransitionInfo(completeWithDisconnectTransition, States.Final)
        transitionsMatrix!![States.SendLogRecords.value][Events.ProcessOneItem.value] = TransitionInfo(sendLogRecordTransition, States.SendLogRecords)
        transitionsMatrix!![States.SendLogRecords.value][Events.ProcessCompleted.value] = TransitionInfo(processExtRecordTransition, States.ProcessExtRecords)

        transitionsMatrix!![States.ProcessExtRecords.value][Events.Error.value] = TransitionInfo(completeWithDisconnectTransition, States.Final)
        transitionsMatrix!![States.ProcessExtRecords.value][Events.ProcessOneItem.value] = TransitionInfo(processExtRecordTransition, States.ProcessExtRecords)
        transitionsMatrix!![States.ProcessExtRecords.value][Events.ProcessCompleted.value] = TransitionInfo(completeWithDisconnectTransition, States.Final)
    }

    /** Process event */
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    override fun process(event: Events)
    {
        try
        {
            val transitionInfo = transitionsMatrix!![currentState.value][event.value]
            currentState = transitionInfo.targetState

            Log.d(LogTags.SYNC_PROCESS, "Current state is: $currentState; Event is: $event")

            transitionInfo.transition.process()
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()

            resetLockOnLogRecords()
            synchronized(locker) {
                (locker as Object).notify()
            }
        }

    }

    /** Reset locks on log records (in case of error when not all records were processed)  */
    private fun resetLockOnLogRecords()
    {
        try
        {
            localDb.syncLog().resetAllLocks()
        } catch (e: SQLException)
        {
            e.printStackTrace()
        }

    }
}