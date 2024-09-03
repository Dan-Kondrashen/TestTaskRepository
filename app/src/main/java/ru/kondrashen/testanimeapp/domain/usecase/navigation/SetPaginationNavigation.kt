package ru.kondrashen.testanimeapp.domain.usecase.navigation

import android.view.View
import ru.kondrashen.testanimeapp.databinding.BottomNavBarWithPaginationBinding

class SetPaginationNavigation {

    fun setPaginationNavigation(view: BottomNavBarWithPaginationBinding, curPageIndex: Int, maxPageIndex: Int?, onClick: View.OnClickListener){
        view.paginationBar.visibility = View.VISIBLE

        maxPageIndex?.let {
            view.apply {
                lastPossiblePage.text = maxPageIndex.toString()
                when(curPageIndex){
                    in (3.. (it -2 )) -> {
                        BottomNavigationBase.setNavNumbers(view, curPageIndex, 0)
                        separator.visibility = View.VISIBLE
                    }
                    2 -> {
                        BottomNavigationBase.setNavNumbers(view, curPageIndex, 1)
                        pagePrevious.text = curPageIndex.toString()
                        separator.visibility = View.VISIBLE
                    }
                    1 ->{
                        BottomNavigationBase.setNavNumbers(view, curPageIndex, 2)
                        lastVisiblePrevious.text = curPageIndex.toString()
                        if (maxPageIndex != curPageIndex) {
                            separator.visibility = View.VISIBLE

                        }
                        else {
                            separator.visibility = View.GONE
                            lastPossiblePage.visibility = View.GONE
                        }
                    }
                    it ->{
                        BottomNavigationBase.setNavNumbers(view, curPageIndex, -3)
                        separator.visibility = View.GONE
                    }
                    it-1 ->{
                        BottomNavigationBase.setNavNumbers(view, curPageIndex, -2)
                        separator.visibility = View.GONE
                    }
                    it-2->{
                        BottomNavigationBase.setNavNumbers(view, curPageIndex, -1)
                        separator.visibility = View.GONE
                    }


                }
                pageNext.setOnClickListener(onClick)
                pagePrevious.setOnClickListener(onClick)
                lastVisiblePrevious.setOnClickListener(onClick)
                lastVisibleNext.setOnClickListener(onClick)
                pageMiddle.setOnClickListener(onClick)
                lastPossiblePage.setOnClickListener(onClick)
            }
        }
    }
}