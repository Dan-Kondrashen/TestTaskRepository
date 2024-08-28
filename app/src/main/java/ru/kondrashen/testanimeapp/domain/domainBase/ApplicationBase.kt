package ru.kondrashen.testanimeapp.domain.domainBase

import android.app.Application

class ApplicationBase : Application() {
    override fun onCreate() {
        super.onCreate()
        RepositoryLocator.init(this)
    }
}