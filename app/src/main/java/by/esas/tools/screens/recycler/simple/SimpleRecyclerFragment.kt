package by.esas.tools.screens.recycler.simple

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainSimpleRecyclerBinding
import by.esas.tools.recycler.simpleItemAdapter.SimpleItemModel
import by.esas.tools.screens.recycler.RecyclerLists
import by.esas.tools.screens.recycler.mapToSimple

class SimpleRecyclerFragment : AppFragment<SimpleRecyclerVM, FMainSimpleRecyclerBinding>() {

    override val fragmentDestinationId: Int = R.id.simpleRecyclerFragment

    override fun provideLayoutId(): Int {
        return R.layout.f_main_simple_recycler
    }

    override fun provideViewModel(): SimpleRecyclerVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(SimpleRecyclerVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fMainSimpleRecyclerCaseOne.adapter = viewModel.adapter
        binding.fMainSimpleRecyclerCaseOne.layoutManager = LinearLayoutManager(context)
        binding.fMainSimpleRecyclerCaseOne.setHasFixedSize(false)

        val lastIndex = RecyclerLists.firstList.lastIndex
        viewModel.adapter.setItems(RecyclerLists.firstList.mapIndexed { index, entity ->
            entity.mapToSimple(lastIndex == index, viewModel.currentAlignment)
        })
        binding.fMainSimpleRecyclerAlignLeft.setOnClickListener {
            viewModel.currentAlignment = View.TEXT_ALIGNMENT_TEXT_START
            resetCurrentList()
        }
        binding.fMainSimpleRecyclerAlignCenter.setOnClickListener {
            viewModel.currentAlignment = View.TEXT_ALIGNMENT_CENTER
            resetCurrentList()
        }
        binding.fMainSimpleRecyclerAlignRight.setOnClickListener {
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
        binding.fMainSimpleRecyclerCaseOne.adapter = viewModel.adapter
    }
}