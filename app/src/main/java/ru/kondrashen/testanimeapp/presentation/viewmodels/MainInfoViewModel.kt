package ru.kondrashen.testanimeapp.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kondrashen.testanimeapp.di.RepositoryLocator
import ru.kondrashen.testanimeapp.repository.repositories.AnimeRepository
import ru.kondrashen.testanimeapp.repository.repositories.GenreRepository
import ru.kondrashen.testanimeapp.domain.data_class.info_classes.AnimeFullInfo
import ru.kondrashen.testanimeapp.domain.data_class.info_classes.AnimeMainInfo
import ru.kondrashen.testanimeapp.domain.data_class.responses.MainResponse

class MainInfoViewModel (application: Application, ): AndroidViewModel(application) {
    private val animRepository: AnimeRepository by lazy { RepositoryLocator.provideAnimeRepository() }
    private val genreRepository: GenreRepository by lazy { RepositoryLocator.provideGenreRepository() }

    private val _animeInfoList = MutableLiveData<List<AnimeMainInfo>>()

    private val mainAnimeMainInfo = MutableLiveData<List<AnimeMainInfo>>()


    fun getAnimeMainInfo(pageIndex: Int): LiveData<MainResponse>{
       return animRepository.getAnimeMainPageFromServ(pageIndex)
    }
    fun getAllGenreFromServ(): LiveData<String>{
       return genreRepository.getGenresFromServ()
    }
    fun getAnimeFullInfoById(id: Int): LiveData<String>{
        return animRepository.getAnimeFullByIdFromServ(id)
    }
    fun getAnimeFromRoom(): LiveData<List<AnimeMainInfo>>{
        return mainAnimeMainInfo
    }
    fun getChosenAnimeFromRoom(animeId: Int): LiveData<AnimeFullInfo>{
        return animRepository.getChosenAnimeInfoFromRoom(animeId)
    }

    fun deleteAnimeFromFavoriteRoom(animeId: Int)= animRepository.deleteAnimeFromFavorite(animeId)

    fun getAnimeBySearchFromServ(pageIndex: Int, searchText: String): LiveData<MainResponse>{
        return animRepository.getAnimeBySearchFromServ(pageIndex, searchText)
    }

    fun setSearchedAnime(offsetItem: Int, searchText: String){
        viewModelScope.launch (Dispatchers.IO){
            var res = animRepository.getAnimeBySearchFromRoom(offsetItem, searchText)
            withContext(Dispatchers.Main) {
                _animeInfoList.value = res
            }
        }
    }
    fun setMainPageAnime(pageIndex: Int){
        viewModelScope.launch (Dispatchers.IO){
            var res = animRepository.getAnimeMainPageFromRoom(pageIndex)
            withContext(Dispatchers.Main) {
                mainAnimeMainInfo.value = res
            }
        }
    }
    fun getAnimeBySearchFromRoom(): LiveData<List<AnimeMainInfo>>{
        return _animeInfoList
    }

}