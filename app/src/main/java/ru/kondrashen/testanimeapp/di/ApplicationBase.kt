package ru.kondrashen.testanimeapp.di

import android.app.Application

class ApplicationBase : Application() {
    override fun onCreate() {
        super.onCreate()
        RepositoryLocator.init(this)
    }
}