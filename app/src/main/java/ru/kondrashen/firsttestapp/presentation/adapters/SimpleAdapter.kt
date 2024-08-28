package ru.kondrashen.firsttestapp.presentation.adapters

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getString
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.kondrashen.firsttestapp.R
import ru.kondrashen.firsttestapp.databinding.ListItemAddToShelterInfoBinding
import ru.kondrashen.firsttestapp.repository.data.CheckDTO
import ru.kondrashen.firsttestapp.repository.data.CheckOperation
import java.util.Locale

class SimpleAdapter(operationsList: List<CheckDTO>, private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var operationsLocal: List<CheckDTO>
    private val formater = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)
    init {
        operationsLocal = operationsList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding = ListItemAddToShelterInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OperationHolder(binding)
    }

    override fun getItemCount() = operationsLocal.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderLoc = holder as OperationHolder
        val item = operationsLocal[position]
        holderLoc.setOperation(item)
    }
    inner class OperationHolder(private val binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
        private lateinit var operationLoc: CheckDTO
        private lateinit var modName: String
        fun setOperation(operation: CheckDTO){
            this.operationLoc = operation
            val bind = binding as ListItemAddToShelterInfoBinding
            bind.weightInfo.text = "${operationLoc.operationWeight} ${getString(context, R.string.kg)}"
            if (operationLoc.checkOperationType.equals(CheckOperation.LOAD))
                bind.weightText.text = getString(context, R.string.loadedWeight)
            else
                bind.weightText.text = getString(context, R.string.unloadedWeight)
            when{
                "Комбайн:" in operationLoc.vehicleName -> {
                    bind.trackType.text = getString(context, R.string.combine)
                    modName = operationLoc.vehicleName.replace("Комбайн:", "")
                    bind.transportIcon.setImageResource(R.drawable.combine_cvg)
                }
                "Зерновоз:" in operationLoc.vehicleName -> {
                    bind.trackType.text = getString(context, R.string.truck)
                    modName = operationLoc.vehicleName.replace("Зерновоз:", "")
                    bind.transportIcon.setImageResource(R.drawable.truck_svg)
                }
            }
            bind.dateView.text = formater.format(operationLoc.operationDataTime)
            bind.trackName.text = modName
        }
    }
}