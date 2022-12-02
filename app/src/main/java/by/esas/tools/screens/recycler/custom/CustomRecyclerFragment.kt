package by.esas.tools.screens.recycler.custom

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainCustomRecyclerBinding
import by.esas.tools.recycler.simpleItemAdapter.SimpleItemModel
import by.esas.tools.screens.recycler.RecyclerLists

class CustomRecyclerFragment : AppFragment<CustomRecyclerVM, FMainCustomRecyclerBinding>() {

    override val fragmentDestinationId: Int = R.id.simpleRecyclerFragment

    override fun provideLayoutId(): Int {
        return R.layout.f_main_custom_recycler
    }

    override fun provideViewModel(): CustomRecyclerVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(CustomRecyclerVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fMainCustomRecyclerCaseOne.adapter = viewModel.adapter
        binding.fMainCustomRecyclerCaseOne.layoutManager = LinearLayoutManager(context)
        binding.fMainCustomRecyclerCaseOne.setHasFixedSize(false)

        val lastIndex = viewModel.firstList.lastIndex
        viewModel.adapter.setItems(RecyclerLists.firstList.mapIndexed { index, entity ->
            entity.mapToSimple(lastIndex == index, viewModel.currentAlignment)
        })
        binding.fMainCustomRecyclerAlignLeft.setOnClickListener {
            viewModel.currentAlignment = View.TEXT_ALIGNMENT_TEXT_START
            resetCurrentList()
        }
        binding.fMainCustomRecyclerAlignCenter.setOnClickListener {
            viewModel.currentAlignment = View.TEXT_ALIGNMENT_CENTER
            resetCurrentList()
        }
        binding.fMainCustomRecyclerAlignRight.setOnClickListener {
            viewModel.currentAlignment = View.TEXT_ALIGNMENT_TEXT_END
            resetCurrentList()
        }
    }

    private fun resetCurrentList() {
        val newList = viewModel.adapter.itemList.map {
            SimpleItemModel(
                name = it.name,
                shortName = it.shortName,
                isChoosed = it.isChoosed,
                isLast = it.isLast,
                textAlignment = viewModel.currentAlignment
            )
        }
        viewModel.adapter.setItems(newList)
        //Total rewrite of the adapter is used because otherwise part of
        // item's text alignment is not updating correctly
        binding.fMainCustomRecyclerCaseOne.adapter = viewModel.adapter
    }
}