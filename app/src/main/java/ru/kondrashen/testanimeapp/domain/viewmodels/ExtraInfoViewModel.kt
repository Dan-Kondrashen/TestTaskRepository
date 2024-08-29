package ru.kondrashen.testanimeapp.domain.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.kondrashen.testanimeapp.domain.domainBase.RepositoryLocator
import ru.kondrashen.testanimeapp.domain.repositories.AnimeRepository
import ru.kondrashen.testanimeapp.domain.repositories.FavoriteRepository
import ru.kondrashen.testanimeapp.domain.repositories.GenreRepository
import ru.kondrashen.testanimeapp.domain.repositories.StudioRepository
import ru.kondrashen.testanimeapp.repository.data_class.info_classes.AnimeFullInfo
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Genres
import ru.kondrashen.testanimeapp.repository.data_class.room_table.Studios
import ru.kondrashen.testanimeapp.repository.db.AnimeInfoDB

class ExtraInfoViewModel(application: Application): AndroidViewModel(application) {
    private val animRepository: AnimeRepository by lazy { RepositoryLocator.provideAnimeRepository() }
    private val genreRepository: GenreRepository by lazy { RepositoryLocator.provideGenreRepository() }
    private val studioRepository: StudioRepository by lazy { RepositoryLocator.provideStudiosRepository() }
    private val favoriteRepository: FavoriteRepository by lazy { RepositoryLocator.provideFavoriteRepository() }

    fun getChosenAnimeGenresFromRoom(animeId: Int): LiveData<List<Genres>>{
        return genreRepository.getGenreByAnimeIdFromRoom(animeId)
    }
    fun getChosenAnimeAuthorStudiosFromRoom(animeId: Int): LiveData<List<Studios>>{
        return studioRepository.getStudiosByAnimeIdFromRoom(animeId)
    }

    fun countFavorite(): LiveData<Int>{
        return favoriteRepository.getFavoriteCountFromRoom()
    }
    fun getFavoriteAnimeFromRoom(pageId: Int): LiveData<List<AnimeFullInfo>>{
        return favoriteRepository.getFavoriteAnimeFromRoom(pageIndex = pageId)
    }
    fun addToFavorite(itemId: Int){
        favoriteRepository.addToFavoriteRoom(itemId)
    }
    fun deleteFromFavorite(itemId: Int){
        favoriteRepository.deleteFromFavoriteRoom(itemId)
    }
}