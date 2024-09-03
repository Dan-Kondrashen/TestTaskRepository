package ru.kondrashen.testanimeapp.presentation.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.kondrashen.testanimeapp.databinding.FavoritesListFragmentBinding
import ru.kondrashen.testanimeapp.presentation.viewmodels.ExtraInfoViewModel
import ru.kondrashen.testanimeapp.presentation.viewmodels.MainInfoViewModel
import ru.kondrashen.testanimeapp.presentation.ui.adapters.MainAnimeAdapter
import ru.kondrashen.testanimeapp.domain.usecase.navigation.BottomNavigationBase
import ru.kondrashen.testanimeapp.domain.usecase.navigation.SetPaginationNavigation
import kotlin.math.absoluteValue
import kotlin.math.sign

class FavoritesAnimFragment: Fragment() {
    private var _binding: FavoritesListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataModel: MainInfoViewModel
    private lateinit var extraModel: ExtraInfoViewModel
    private var index: Int = 1
    private var maxPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataModel = ViewModelProvider(requireActivity()).get(MainInfoViewModel::class.java)
        extraModel = ViewModelProvider(requireActivity()).get(ExtraInfoViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var index =1
        BottomNavigationBase.addNavigationOnBottomBar("favorite", binding.navBar, findNavController())
        binding.navBar.paginationBar.visibility = VISIBLE
        extraModel.countFavorite().observe(viewLifecycleOwner){ count ->
            count?.let { countLoc ->
                var maxItems = countLoc.calculatePage(25)
                SetPaginationNavigation().setPaginationNavigation(
                    binding.navBar,
                    1,
                    maxItems,
                    onTextViewClickUpdate()
                )
                BottomNavigationBase.setPageViewsDesign(
                    binding.navBar,
                    1,
                    maxItems
                )
            }
        }
        extraModel.getFavoriteAnimeFromRoom(index).observe(viewLifecycleOwner){
            val adapter = MainAnimeAdapter(it.toMutableList(), activity as Activity, findNavController(), true)
            binding.animeList.adapter = adapter

        }

    }
    fun Int.calculatePage(other: Int): Int {
        return this.floorDiv(other) + this.rem(other).sign.absoluteValue
    }

    fun onTextViewClickUpdate(): View.OnClickListener{
        return View.OnClickListener { view ->
            view as TextView
            this.index = view.text.toString().toInt()
            extraModel.getFavoriteAnimeFromRoom(index)
            dataModel.setMainPageAnime(index)
            SetPaginationNavigation().setPaginationNavigation(
                binding.navBar,
                index,
                maxPages,
                onTextViewClickUpdate()
            )
            BottomNavigationBase.setPageViewsDesign(binding.navBar, index, maxPages)
        }

    }
}