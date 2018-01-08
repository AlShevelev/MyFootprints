package com.syleiman.myfootprints.presentationLayer.activities.gridGallery

import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.ISyncResultChannel
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncCompletedReceiver
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.modelLayer.dto.FootprintForGridDto

import javax.inject.Inject

class GridGalleryActivityModel
@Inject
constructor(private val footprints : IFootprintsService, private val bitmapService: IBitmapService)
{
    private var gridData: List<FootprintForGridDto>? = null

    /**  */
    var lastClickedPosition = 0
        private set

    private var syncCompletedReceiver: SyncCompletedReceiver? = null

    /**
     * Get source data for grid
     * @param result false in case of error
     */
    fun initGridData(result: (Boolean) -> Unit)
    {
        footprints.getAllForGrid { gridData ->
            this.gridData = gridData
            result.invoke(gridData != null)
        }
    }

    /**  */
    fun getDataItemByPosition(position: Int): GridDataItem
    {
        val (footprintId, createDateTime, photoFileName, cityName, countryName) = gridData!![position]

        val photoFile = FileSingle.withName(photoFileName).inPrivate()
        val thumbnailFile = FileSingle.fromExistWithPrefix(photoFile, FileSingle.THUMBNAIL_FILES_PREFIX)
        if(!thumbnailFile.isExists())
            bitmapService.createThumbnail(photoFile, thumbnailFile)

        return GridDataItem(footprintId, createDateTime, thumbnailFile.getUrl(), cityName, countryName)
    }

    /**  */
    val totalItems: Int
        get() = gridData!!.size

    /**  */
    fun getDataItemIdByPosition(position: Int): Long
    {
        lastClickedPosition = position
        return gridData!![position].footprintId
    }

    /** Start receive sync events  */
    fun startReceiveSyncStatus(syncResultChannel: ISyncResultChannel)
    {
        syncCompletedReceiver = SyncCompletedReceiver(syncResultChannel)
    }

    /** Start receive sync events  */
    fun stopReceiveSyncStatus()
    {
        if (syncCompletedReceiver != null)
            syncCompletedReceiver!!.unregister()
    }
}