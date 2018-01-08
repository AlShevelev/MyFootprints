package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity

/** CreateFootprintActivityPresenter interface for child presenters */
interface ICreateFootprintActivityPresenterChildes
{
    /** Show second step - create fragment  */
    fun switchToCreateStep()

    /** finish activity  */
    fun close()
}
