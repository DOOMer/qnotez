package org.doomer.qnotez.viewmodel

import android.arch.lifecycle.ViewModel

import dagger.MapKey
import kotlin.reflect.KClass
import kotlin.annotation.AnnotationRetention
import kotlin.annotation.Retention

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)