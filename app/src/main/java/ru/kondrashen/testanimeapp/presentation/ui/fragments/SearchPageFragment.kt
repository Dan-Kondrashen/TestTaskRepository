package ru.kondrashen.testanimeapp.presentation.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import ru.kondrashen.testanimeapp.R
import ru.kondrashen.testanimeapp.databinding.SearchableFragmentAnimelistBinding
import ru.kondrashen.testanimeapp.presentation.viewmodels.MainInfoViewModel
import ru.kondrashen.testanimeapp.presentation.ui.adapters.MainAnimeAdapter
import ru.kondrashen.testanimeapp.domain.usecase.navigation.BottomNavigationBase
import ru.kondrashen.testanimeapp.presentation.base.PublicConstants

class SearchPageFragment: Fragment() {
    private var _binding: SearchableFragmentAnimelistBinding? = null
    private val binding get() = _binding!!
    private var index = 1
    var lastSearchTime: Long = 0L
    private var isLoading =false
    private var maxIndex: Int? = null
    private var curPageIndex: Int = 1
    private var totalElements: Int = 0
    private val handler = Handler(Looper.getMainLooper())

    private var searchText: String? = null
    private lateinit var dataModel: MainInfoViewModel

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(PublicConstants.SEARCH_WORD, searchText)
        outState.putInt(PublicConstants.PAGE_NUM, index)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataModel = ViewModelProvider(requireActivity()).get(MainInfoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchableFragmentAnimelistBinding.inflate(inflater, container, false)
        binding.animeList.layoutManager = FlexboxLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MainAnimeAdapter(mutableListOf(), activity as Activity, findNavController(), false)
        var menuProviderMain = object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_with_searchbar, menu)
                val searchItem = menu.findItem(R.id.search_bar)
                val searchView = searchItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query?.let {
                            makeASearchRequest(adapter, it)
                        }
                        return true
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.search_bar -> {
                        true
                    }
                    else -> false
                }
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(menuProviderMain, viewLifecycleOwner)

        searchText = savedInstanceState?.getString(PublicConstants.SEARCH_WORD)

        searchText?.let { str ->
            totalElements = 0
            dataModel.getAnimeBySearchFromServ(index,str).observeOneData(viewLifecycleOwner){
                if (it.status == "success"){
                    dataModel.setSearchedAnime(totalElements, str)
                }
                else if(it.status == "fail"){
                    dataModel.setSearchedAnime(totalElements, str)
                }
            }
        }
        binding.animeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val itemVisibleNum = recyclerView.layoutManager?.childCount?: 0
                val itemIn = recyclerView.layoutManager?.itemCount?: 0
                totalElements = adapter.itemCount
                val firstVisibleItemPos = (recyclerView.layoutManager as FlexboxLayoutManager)
                    .findFirstVisibleItemPosition()
                if (!isLoading){
                    if ( (firstVisibleItemPos+itemVisibleNum) >= itemIn) {
                        isLoading = true
                        curPageIndex +=1
                        searchText?.let { str ->
                            dataModel.getAnimeBySearchFromServ(curPageIndex, str)
                                .observeOneData(viewLifecycleOwner){

                                    if (it.status == "success"){
                                        Log.i("INFO", "сервер отвечает $curPageIndex")
//                                        totalElements = it.total
//                                        maxIndex = it.maxPage
                                        dataModel.setSearchedAnime(totalElements, str)
                                        println("total +$totalElements")
                                        isLoading = false
                                    }
                                    else if(it.status == "fail"){
                                        Log.i("INFO", "сервер отвечает провалом $curPageIndex")
                                        dataModel.setSearchedAnime(totalElements, str)
                                        isLoading = false
                                    }

                                }
                        }

                    }
                }
            }
        })
        BottomNavigationBase.addNavigationOnBottomBar("search", binding.navBar, findNavController())
        getItemsFromRoom(adapter)
    }

    private fun makeASearchRequest(adapter: MainAnimeAdapter, text: String){
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastSearchTime > PublicConstants.searchDelay) {
            lastSearchTime = currentTime
            adapter.clearAdapter()
            binding.priogress.visibility = VISIBLE
            binding.searchResultInfo.visibility = GONE
            dataModel.getAnimeBySearchFromServ(1,text)
                .observeOneData(viewLifecycleOwner){ res ->
                    curPageIndex =1
                    if (res.status == "success"){
                        totalElements =  0
                        maxIndex = res.maxPage
                        dataModel.setSearchedAnime(0, text)
                    } else if(res.status == "fail"){
                        dataModel.setSearchedAnime(0, text)
                    }
                }
            index = 1
            searchText = text
        }
        else Log.e(PublicConstants.TEST_TAG, "ЗАПРОС ДО 500мс")


    }
    fun getItemsFromRoom(adapter: MainAnimeAdapter){
        dataModel.getAnimeBySearchFromRoom().observe(viewLifecycleOwner){ resList ->
            if( resList != null && resList.isNotEmpty()){
                binding.priogress.visibility =GONE
                if (curPageIndex == 1){
                    adapter.setAdapterItems(resList.toMutableList())
                    binding.animeList.adapter = adapter
                }
                else adapter.addItems(resList.toMutableList())
            }
            else {
                binding.priogress.visibility =GONE
                binding.searchResultInfo.visibility = VISIBLE
            }

        }
    }

    fun <T> LiveData<T>.observeOneData(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        var previousKey: Any? = value?: "NULL"
        observe(lifecycleOwner){
            if(previousKey == "NULL"){
                previousKey = value
                observer.onChanged(value!!)
            }
        }
    }
}