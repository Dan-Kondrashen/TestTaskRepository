package ru.kondrashen.firsttestapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kondrashen.firsttestapp.R
import ru.kondrashen.firsttestapp.databinding.MainFragmentBinding
import ru.kondrashen.firsttestapp.databinding.MenuFragmentBinding

class MenuFragment: Fragment() {
    private var _binding: MenuFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = MenuFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace( R.id.content, MainFragment())
                .commit()
        }
    }

}