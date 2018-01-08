package com.syleiman.myfootprints.applicationLayer.di.scopes

import javax.inject.Scope

/** Application-level scope - object living while application living  */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope
