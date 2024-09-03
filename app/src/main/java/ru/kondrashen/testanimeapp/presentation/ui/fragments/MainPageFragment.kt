package ru.kondrashen.testanimeapp.presentation.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.FlexboxLayoutManager
import ru.kondrashen.testanimeapp.databinding.MainFragmentAnimelistBinding
import ru.kondrashen.testanimeapp.presentation.viewmodels.MainInfoViewModel
import ru.kondrashen.testanimeapp.presentation.ui.adapters.MainAnimeAdapter
import ru.kondrashen.testanimeapp.domain.usecase.BottomNavigationBase
import ru.kondrashen.testanimeapp.domain.usecase.BottomNavigationBase.setPageViewsDesign
import ru.kondrashen.testanimeapp.domain.usecase.BottomNavigationBase.setPaginationNavigation

class MainPageFragment: Fragment() {
    private var _binding: MainFragmentAnimelistBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataModel: MainInfoViewModel
    private var index: Int = 1
    private var maxPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataModel = ViewModelProvider(requireActivity()).get(MainInfoViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentAnimelistBinding.inflate(inflater, container, false)
        binding.animeList.layoutManager = FlexboxLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BottomNavigationBase.addNavigationOnBottomBar("main", binding.navBar, findNavController())
        getInfoFromServ(index)
        dataModel.getAllGenreFromServ()
        dataModel.setMainPageAnime(index)
        dataModel.getAnimeFromRoom().observe(viewLifecycleOwner){
            val adapter = MainAnimeAdapter(it.toMutableList(), activity as Activity, findNavController(), false)
            binding.animeList.adapter = adapter

        }
    }

    private fun getInfoFromServ(index: Int){
        dataModel.getAnimeMainInfo(index).observe(viewLifecycleOwner){ resp ->
            resp.maxPage?.also { maxPages = it }
            setPaginationNavigation(binding.navBar, resp.curPage?: 1, resp.maxPage, onTextViewClickUpdate())
            setPageViewsDesign(binding.navBar, resp.curPage?: 1, resp.maxPage)
            if (resp.status == "success"){
                dataModel.setMainPageAnime(index)
            }
            else if (resp.status == "fail"){
                dataModel.setMainPageAnime(index)
            }
        }
    }


    fun onTextViewClickUpdate(): View.OnClickListener{
        return View.OnClickListener { view ->
            view as TextView
            this.index = view.text.toString().toInt()
            getInfoFromServ(index)
            dataModel.setMainPageAnime(index)
            setPaginationNavigation(binding.navBar, index, maxPages, onTextViewClickUpdate())
            setPageViewsDesign(binding.navBar,index, maxPages)
        }

    }
}