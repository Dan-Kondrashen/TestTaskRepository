package ru.kondrashen.firsttestapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.kondrashen.firsttestapp.R
import ru.kondrashen.firsttestapp.databinding.MainFragmentBinding
import ru.kondrashen.firsttestapp.presentation.adapters.SimpleAdapter
import ru.kondrashen.firsttestapp.repository.data.CheckOperation
import ru.kondrashen.firsttestapp.repository.data.mockList

class MainFragment: Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    companion object{
        const val delayShown = 3500
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SimpleAdapter(mockList, requireContext())
        binding.operationRecycle.adapter = adapter
        binding.print.setOnClickListener {
            val bar = Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(
                R.string.notImplemented
            ), delayShown)
            bar.show()
        }
        var giveCereal = 0
        var takeCereal = 0
        for (item in mockList){
            if (item.checkOperationType.equals(CheckOperation.LOAD)){
                giveCereal += item.operationWeight
            }
            else{
                takeCereal += item.operationWeight
            }
        }
        binding.topBar.menu.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content, MenuFragment())
                .commit()
        }
        binding.weightInShelter.text =
            if((giveCereal - takeCereal) > 0)
                    (giveCereal - takeCereal).toString()
            else "0"
    }

}