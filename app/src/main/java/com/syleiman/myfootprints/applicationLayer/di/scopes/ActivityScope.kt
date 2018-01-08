package com.syleiman.myfootprints.applicationLayer.di.scopes

import javax.inject.Scope

/** Activity-level scope - object living while activity living */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope