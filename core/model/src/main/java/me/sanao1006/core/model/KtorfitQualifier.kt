package me.sanao1006.core.model

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StreamingApi
