package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync

import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions.ITransition

/** One transition in matrix  */
class TransitionInfo(val transition: ITransition, val targetState: States)