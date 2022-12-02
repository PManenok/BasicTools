package by.esas.tools.screens.recycler.sticky_case

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainStickyRecyclerBinding
import by.esas.tools.screens.recycler.RecyclerLists
import by.esas.tools.screens.recycler.sticky_case.sticky_adapter.CaseStickyHeader
import by.esas.tools.screens.recycler.sticky_case.sticky_adapter.CaseStickyItemVM

class StickyCaseFragment : AppFragment<StickyCaseVM, FMainStickyRecyclerBinding>() {

    override val fragmentDestinationId: Int = R.id.stickyCaseFragment

    override fun provideLayoutId(): Int {
        return R.layout.f_main_sticky_recycler
    }

    override fun provideViewModel(): StickyCaseVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(StickyCaseVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fMainStickyRecycler.adapter = viewModel.adapter
        binding.fMainStickyRecycler.layoutManager = LinearLayoutManager(context)
        binding.fMainStickyRecycler.setHasFixedSize(true)

        viewModel.adapter.setEntities(RecyclerLists.firstList)
        viewModel.adapter.stickyHeaderDecoration = CaseStickyHeader.create(
            parent = binding.fMainStickyRecycler,
            viewModel = CaseStickyItemVM(),
            listener = viewModel.adapter.listener
        )
        viewModel.adapter.stickyHeaderDecoration?.let {
            binding.fMainStickyRecycler.addItemDecoration(it)
        }
    }
}