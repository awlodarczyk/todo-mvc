package io.hindbrain.todo.view.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.hindbrain.todo.R
import io.hindbrain.todo.adapter.TodoItemAdapter
import io.hindbrain.todo.repository.DataManagerService

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ArchivedItemsFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var noItemsView: ConstraintLayout
    private lateinit var adapter: TodoItemAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_archived_items, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.recycler = view.findViewById<RecyclerView>(R.id.recycler)
        this.linearLayoutManager = LinearLayoutManager(requireContext())
        this.recycler.layoutManager = linearLayoutManager
        this.noItemsView = view.findViewById<ConstraintLayout>(R.id.no_items)
        prepareItems()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareItems() {
        this.noItemsView.visibility = View.VISIBLE
        this.recycler.visibility = View.GONE
        DataManagerService(requireContext()).getItems()?.let {
            val filtered = it.filter { todoItem -> todoItem.done }
            if (filtered.isNotEmpty()) {
                adapter = TodoItemAdapter(filtered)
                this.recycler.adapter = adapter
                this.noItemsView.visibility = View.GONE
                this.recycler.visibility = View.VISIBLE
            }
        }
    }
}