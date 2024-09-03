package ru.kondrashen.testanimeapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.flexbox.FlexboxLayoutManager
import ru.kondrashen.testanimeapp.R
import ru.kondrashen.testanimeapp.databinding.ChosenAnimeInfoFragmentBinding
import ru.kondrashen.testanimeapp.presentation.viewmodels.ExtraInfoViewModel
import ru.kondrashen.testanimeapp.presentation.viewmodels.MainInfoViewModel
import ru.kondrashen.testanimeapp.presentation.adapters.LinearItemsAdapter
import ru.kondrashen.testanimeapp.domain.usecase.ImageFactory
import ru.kondrashen.testanimeapp.presentation.base.PublicConstants

class AnimeInfoFragment: Fragment() {
    private var _binding: ChosenAnimeInfoFragmentBinding? = null
    private val binding get() = _binding!!
    private var inFavoriteStatus = false
    private lateinit var dataModel: MainInfoViewModel
    private lateinit var extraInfoModel: ExtraInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataModel = ViewModelProvider(requireActivity()).get(MainInfoViewModel::class.java)
        extraInfoModel = ViewModelProvider(requireActivity()).get(ExtraInfoViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChosenAnimeInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animeId = arguments?.getInt(PublicConstants.STORY_ID)
        updateUI(animeId)
    }

    fun updateUI(animeId: Int?){
        val genresAdapter = LinearItemsAdapter(listOf())
        val studiosAdapter = LinearItemsAdapter(listOf())
        binding.genreList.layoutManager = FlexboxLayoutManager(requireContext())
        animeId?.let { id ->
            dataModel.getAnimeFullInfoById(id)
            dataModel.getChosenAnimeFromRoom(id).observe(viewLifecycleOwner){anim ->
                anim?.let {
                    println(it.anime.url + "djn")
                    binding.descriptionInfo.text = it.anime.description
                    binding.titleInfo.text = it.anime.title
                    binding.statusInfo.text = it.anime.status
                    binding.episodesInfo.text = getString(R.string.episodesInfo,
                        it.anime.episodes.toString(), it.anime.duration)
                    ImageFactory.setAnimPoster(binding.poster, it.anime.url, this.requireContext())
                    inFavoriteStatus = anim.favorite != null
                    if (inFavoriteStatus)
                        binding.floatingBar.addToFavorite
                            .setImageResource(R.drawable.add_to_favorite_activate_svg)
                    else{
                        binding.floatingBar.addToFavorite
                            .setImageResource(R.drawable.add_to_favorite_btn)
                    }

                    addListeners(id)
                    if (it.anime.trailerUrl != null) {
                        val videoPath = "<iframe width=\"100%\" height=\"100%\" src=\"${it.anime.trailerUrl}\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
                        binding.player.apply {
                            loadData(videoPath, "text/html", "utf-8")
                            settings.javaScriptEnabled = true
                            webChromeClient = WebChromeClient()
                        }
                    }
                    else {
                        binding.player.visibility = GONE
                        binding.playerText.visibility = GONE
                    }
                }
            }
            extraInfoModel.getChosenAnimeGenresFromRoom(id).observe(viewLifecycleOwner){ genresList ->
                genresList?.let {
                    genresAdapter.setAdapterItems(it)
                    binding.genreList.adapter = genresAdapter
                }
            }
            extraInfoModel.getChosenAnimeAuthorStudiosFromRoom(id).observe(viewLifecycleOwner){ studiosList ->
                studiosList?.let {
                    studiosAdapter.setAdapterItems(it)
                    binding.studioList.adapter = studiosAdapter

                }
            }
        }
    }
    private fun addListeners(animeId: Int){
        binding.floatingBar.addToFavorite.apply {
            setOnClickListener{
                when(inFavoriteStatus){
                    true -> {
                        extraInfoModel.deleteFromFavorite(animeId)
                        this.setImageResource(R.drawable.add_to_favorite_activate_svg)
                    }
                    else -> {
                        extraInfoModel.addToFavorite(animeId)
                        this.setImageResource(R.drawable.add_to_favorite_svg)
                    }
                }

            }
        }
    }
}