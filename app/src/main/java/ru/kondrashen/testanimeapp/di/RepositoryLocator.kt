package ru.kondrashen.testanimeapp.di

import android.app.Application
import ru.kondrashen.testanimeapp.repository.repositories.AnimeRepository
import ru.kondrashen.testanimeapp.repository.repositories.FavoriteRepository
import ru.kondrashen.testanimeapp.repository.repositories.GenreRepository
import ru.kondrashen.testanimeapp.repository.repositories.StudioRepository
import ru.kondrashen.testanimeapp.repository.db.AnimeInfoDB

object RepositoryLocator {
    private lateinit var database: AnimeInfoDB
    private lateinit var animeRepository: AnimeRepository
    private lateinit var genreRepository: GenreRepository
    private lateinit var studioRepository: StudioRepository
    private lateinit var favoriteRepository: FavoriteRepository

    fun init(application: Application) {
        database = AnimeInfoDB.getDatabase(application)

        val animeDao = database.animeDao()
        val genreDao = database.genreDao()
        val studiosDao = database.studiosDao()
        val animeVirtualDao = database.animeVirtualDao()
        val operationTypeDao = database.operationTypeDao()
        val pageDao = database.animeToPageDao()
        val favoriteDao = database.favoriteDao()

        animeRepository = AnimeRepository(
            animeDao, genreDao,
            studiosDao, animeVirtualDao,
            pageDao, operationTypeDao
        )
        genreRepository = GenreRepository(genreDao)
        favoriteRepository = FavoriteRepository(animeDao, favoriteDao)
        studioRepository = StudioRepository(studiosDao)
    }

    fun provideAnimeRepository(): AnimeRepository {
        return animeRepository
    }

    fun provideGenreRepository(): GenreRepository {
        return genreRepository
    }

    fun provideFavoriteRepository(): FavoriteRepository {
        return favoriteRepository
    }

    fun provideStudiosRepository(): StudioRepository {
        return studioRepository
    }
}