package by.esas.tools.screens.recycler.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainRecyclerBinding
import by.esas.tools.screens.recycler.RecyclerLists

class RecyclerFragment : AppFragment<RecyclerVM, FMainRecyclerBinding>() {

    override val fragmentDestinationId: Int = R.id.recyclerFragment

    override fun provideLayoutId(): Int {
        return R.layout.f_main_recycler
    }

    override fun provideViewModel(): RecyclerVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(RecyclerVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fMainRecyclerCaseOne.adapter = viewModel.adapter
        binding.fMainRecyclerCaseOne.layoutManager = LinearLayoutManager(context)
        binding.fMainRecyclerCaseOne.setHasFixedSize(true)

        viewModel.adapter.setItems(RecyclerLists.firstList)
    }
}