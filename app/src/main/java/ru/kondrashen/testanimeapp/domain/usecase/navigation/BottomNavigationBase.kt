package ru.kondrashen.testanimeapp.domain.usecase.navigation

import android.view.View
import androidx.core.view.children
import androidx.navigation.NavController
import ru.kondrashen.testanimeapp.R
import ru.kondrashen.testanimeapp.databinding.BottomNavBarWithPaginationBinding

object BottomNavigationBase {
    fun addNavigationOnBottomBar(startDestination: String, view: BottomNavBarWithPaginationBinding, navigation : NavController){
        when(startDestination){
            "main" -> view.apply {
                homePage.setImageResource(R.drawable.home_dark_svg)
                favoritePage.setOnClickListener{
                    navigation.navigate(R.id.action_to_favoriteAnimeFragment)
                }
                searchPage.setOnClickListener {
                    navigation.navigate(R.id.action_to_searchAnimeInfoFragment)
                }
            }
            "search" -> view.apply {
                searchPage.setImageResource(R.drawable.search_dark_svg)
                favoritePage.setOnClickListener{
                    navigation.navigate(R.id.action_to_favoriteAnimeFragment)
                }
                homePage.setOnClickListener {
                    navigation.navigate(R.id.action_to_mainPageFragment)
                }
            }
            "favorite" -> view.apply {
                favoritePage.setImageResource(R.drawable.favorite_dark_svg)
                homePage.setOnClickListener {
                    navigation.navigate(R.id.action_to_mainPageFragment)
                }
                searchPage.setOnClickListener {
                    navigation.navigate(R.id.action_to_searchAnimeInfoFragment)
                }
            }
        }

    }

    fun setNavNumbers(view: BottomNavBarWithPaginationBinding, curPageIndex: Int, step: Int){
        val middle = curPageIndex + step
        view.apply {
            lastVisiblePrevious.text = (middle - 2).toString()
            pagePrevious.text = (middle - 1).toString()
            pageMiddle.text = middle.toString()
            pageNext.text = (middle + 1).toString()
            lastVisibleNext.text = (middle + 2).toString()
        }
    }
    fun setPageViewsDesign(view: BottomNavBarWithPaginationBinding, curPageIndex: Int, maxPageIndex: Int?){
        val clearViewsDesign =fun(){
            val views = view.paginationBar.children.toHashSet()
            views.map { it.setBackgroundResource(0) }
        }
        maxPageIndex?.let {
            clearViewsDesign()

            view.apply {
                when (maxPageIndex){
                    4 ->{
                        lastVisibleNext.visibility = View.GONE
                    }
                    3 ->{
                        lastVisibleNext.visibility = View.GONE
                        pageNext.visibility = View.GONE
                    }
                    2->{
                        lastVisibleNext.visibility = View.GONE
                        pageNext.visibility = View.GONE
                        pageMiddle.visibility = View.GONE
                    }
                    1->{
                        lastVisibleNext.visibility = View.GONE
                        pageNext.visibility = View.GONE
                        pageMiddle.visibility = View.GONE
                        pagePrevious.visibility = View.GONE
                    }
                }
                when {
                    (curPageIndex > 2) ->
                        if(curPageIndex < maxPageIndex - 2){
                            pageMiddle.setBackgroundResource(R.drawable.accent_back)
                        }
                        else if (curPageIndex == maxPageIndex -2){
                            pageNext.setBackgroundResource(R.drawable.accent_back)
                        }
                        else if ( curPageIndex == maxPageIndex - 1){
                            lastVisibleNext.setBackgroundResource(R.drawable.accent_back)
                        }

                    (curPageIndex == 2 && curPageIndex < maxPageIndex - 3) -> {
                        pagePrevious.setBackgroundResource(R.drawable.accent_back)
                    }

                    (curPageIndex == 1 && curPageIndex < maxPageIndex - 4) -> {
                        lastVisiblePrevious.setBackgroundResource(R.drawable.accent_back)
                    }

                }
            }
        }
    }
}