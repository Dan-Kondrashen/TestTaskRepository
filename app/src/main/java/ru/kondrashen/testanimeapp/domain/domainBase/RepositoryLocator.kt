package ru.kondrashen.testanimeapp.domain.domainBase

import android.app.Application
import ru.kondrashen.testanimeapp.domain.repositories.AnimeRepository
import ru.kondrashen.testanimeapp.domain.repositories.GenreRepository
import ru.kondrashen.testanimeapp.repository.db.AnimeInfoDB

object RepositoryLocator {
    private lateinit var database: AnimeInfoDB
    private lateinit var animeRepository: AnimeRepository
    private lateinit var genreRepository: GenreRepository

    fun init(application: Application) {
        database = AnimeInfoDB.getDatabase(application)

        val animeDao = database.animeDao()
        val genreDao = database.genreDao()
        val studiosDao = database.studiosDao()
        val animeVirtualDao = database.animeVirtualDao()
        val operationTypeDao = database.operationTypeDao()
        val pageDao = database.animeToPageDao()

        animeRepository = AnimeRepository(
            animeDao, genreDao,
            studiosDao, animeVirtualDao,
            pageDao, operationTypeDao
        )
        genreRepository = GenreRepository(genreDao)
    }

    fun provideAnimeRepository(): AnimeRepository {
        return animeRepository
    }

    fun provideGenreRepository(): GenreRepository {
        return genreRepository
    }
}