package ru.kondrashen.testanimeapp.domain.usecase

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import ru.kondrashen.testanimeapp.R
import ru.kondrashen.testanimeapp.databinding.ListItemAnimeBinding
import ru.kondrashen.testanimeapp.databinding.ListItemAnimeExpendedBinding

object ImageFactory {
    fun setPreview(view: View, url: String, context: Context){
        if (CatchInternetException.hasInternetCheck(context)) {
            val previewImage = view.findViewById<ImageView>(R.id.preview)
            Glide.with(view)
                .asDrawable()
                .load(url)
                .error(Glide.with(view).load(R.drawable.anim))
                .into(previewImage)
        }
    }
    fun setAnimPoster(view: View, url: String, context: Context){
        if (CatchInternetException.hasInternetCheck(context)) {
            val poster = view.findViewById<ImageView>(R.id.poster)
            Glide.with(view)
                .asDrawable()
                .load(url)
                .error(Glide.with(view).load(R.drawable.anim))
                .into(poster)
        }
    }
}