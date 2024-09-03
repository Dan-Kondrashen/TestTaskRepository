package ru.kondrashen.testanimeapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.kondrashen.testanimeapp.databinding.ListItemGenreBinding
import ru.kondrashen.testanimeapp.domain.data_class.SimpleNameInterface

class LinearItemsAdapter(infoList: List<SimpleNameInterface>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var genres: List<SimpleNameInterface>
    init {
        genres = infoList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding = ListItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeHolder(binding)
    }

    override fun getItemCount() = genres.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderLoc = holder as AnimeHolder
        val item = genres[position]
        holderLoc.setGenre(item)
    }
    inner class AnimeHolder(private val binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
        private lateinit var _genre: SimpleNameInterface
        fun setGenre(genre: SimpleNameInterface){
            this._genre = genre
            val bind = binding as ListItemGenreBinding
            bind.name.text = _genre.name
        }
    }
    fun setAdapterItems(genres: List<SimpleNameInterface>){
        this.genres = genres
        notifyDataSetChanged()
    }
}