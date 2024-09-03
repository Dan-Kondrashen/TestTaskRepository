package ru.kondrashen.testanimeapp.presentation.adapters

import android.app.Activity
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.kondrashen.testanimeapp.R
import ru.kondrashen.testanimeapp.databinding.ListItemAnimeBinding
import ru.kondrashen.testanimeapp.databinding.ListItemAnimeExpendedBinding
import ru.kondrashen.testanimeapp.presentation.viewmodels.MainInfoViewModel
import ru.kondrashen.testanimeapp.domain.usecase.ImageFactory
import ru.kondrashen.testanimeapp.presentation.base.PublicConstants
import ru.kondrashen.testanimeapp.domain.data_class.info_classes.AnimeBase
import ru.kondrashen.testanimeapp.domain.data_class.info_classes.AnimeFullInfo
import java.util.Locale

class MainAnimeAdapter(infoList: MutableList<AnimeBase>, private var activity: Activity, private var navigation: NavController, private var isExpendedFavorite: Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var anime: MutableList<AnimeBase>
    private val formater = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)
    init {
        anime = infoList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding = when(isExpendedFavorite){
            false -> ListItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            else -> ListItemAnimeExpendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }

        return AnimeHolder(binding)
    }

    override fun getItemCount() = anime.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderLoc = holder as AnimeHolder
        val item = anime[position]
        holderLoc.setAnimeInfo(item)
    }

    fun setAdapterItems(newItemsList: MutableList<AnimeBase>){
        this.anime = newItemsList
        notifyDataSetChanged()
    }
    fun addItems(newItemsList: MutableList<AnimeBase>){
        this.anime.addAll(newItemsList)
        notifyItemInserted(itemCount -1)
    }
    fun clearAdapter(){
        this.anime.clear()
        notifyDataSetChanged()
    }

    inner class AnimeHolder(private val binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
        private lateinit var animeInfo: AnimeBase
        fun setAnimeInfo(info: AnimeBase){
            this.animeInfo = info
            if (!isExpendedFavorite){
                val bind = binding as ListItemAnimeBinding
                bind.title.text = animeInfo.anime.title
                bind.score.text = animeInfo.anime.score.toString()
                bind.shortDescription.text = animeInfo.anime.description
                bind.popularityIndicator.progress = (animeInfo.anime.score * 10).toInt()
                bind.type.text = animeInfo.anime.type
                val storyId = animeInfo.anime.id
                ImageFactory.setPreview(bind.preview, animeInfo.anime.url, activity)
                bind.root.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt(PublicConstants.STORY_ID, storyId)
                    navigation.navigate(R.id.action_to_chosenAnimeInfoFragment, bundle)
                }
            }
            else{
                val bind = binding as ListItemAnimeExpendedBinding
                animeInfo as AnimeFullInfo
                bind.title.text = animeInfo.anime.title
                bind.dateAddInfo.text = formater.format((animeInfo as AnimeFullInfo).favorite?.date)
                bind.score.text = animeInfo.anime.score.toString()
                bind.shortDescription.text = animeInfo.anime.description
                bind.popularityIndicator.progress = (animeInfo.anime.score * 10).toInt()
                bind.type.text = animeInfo.anime.type
                bind.seasonInfo.text = if (animeInfo.anime.season != "" && animeInfo.anime.year != null){
                    activity.getString(R.string.season, animeInfo.anime.season, animeInfo.anime.year.toString())} else activity.getString(
                    R.string.notDefinedYet
                )
                bind.deleteBtn.setOnClickListener{
                    var dataModel = ViewModelProvider(activity as AppCompatActivity).get(
                        MainInfoViewModel::class.java)
                    dataModel.deleteAnimeFromFavoriteRoom(animeInfo.anime.id)
                    val removedItemIndex = adapterPosition
                    anime.removeAt(removedItemIndex)
                    notifyItemRemoved(removedItemIndex)
                }
                val storyId = animeInfo.anime.id
                ImageFactory.setPreview(bind.preview, animeInfo.anime.url, activity)
                bind.root.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt(PublicConstants.STORY_ID, storyId)
                    navigation.navigate(R.id.action_to_chosenAnimeInfoFragment, bundle)
                }
            }


        }
    }
}