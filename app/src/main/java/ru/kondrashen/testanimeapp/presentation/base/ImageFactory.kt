package ru.kondrashen.testanimeapp.presentation.base

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.kondrashen.testanimeapp.R

object ImageFactory {
    fun setPreview(view: View, url: String, context: Context){
        if (CatchException.hasInternetCheck(context)) {
            var previewImage = view.findViewById<ImageView>(R.id.preview)
            Glide.with(view)
                .asDrawable()
                .load(url)
                .error(Glide.with(view).load(R.drawable.anim))
                .into(previewImage)
        }
    }
    fun setAnimPoster(view: View, url: String, context: Context){
        if (CatchException.hasInternetCheck(context)) {
            var poster = view.findViewById<ImageView>(R.id.poster)
            Glide.with(view)
                .asDrawable()
                .load(url)
                .error(Glide.with(view).load(R.drawable.anim))
                .into(poster)
        }
    }
}