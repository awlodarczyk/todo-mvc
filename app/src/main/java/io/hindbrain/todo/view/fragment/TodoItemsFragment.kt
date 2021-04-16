package io.hindbrain.todo.view.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import io.hindbrain.todo.R
import io.hindbrain.todo.adapter.TodoItemAdapter
import io.hindbrain.todo.adapter.TodoItemListener
import io.hindbrain.todo.model.Priority
import io.hindbrain.todo.model.TodoItem
import io.hindbrain.todo.repository.DataManagerService

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TodoItemsFragment : Fragment(), TodoItemListener {

    private lateinit var recycler: RecyclerView
    private lateinit var noItemsView: ConstraintLayout
    private lateinit var textInput: TextInputEditText
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapter: TodoItemAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            addItemIfAvailable()
        }
        this.recycler = view.findViewById<RecyclerView>(R.id.recycler)
        this.textInput = view.findViewById<TextInputEditText>(R.id.text_input)
        this.linearLayoutManager = LinearLayoutManager(requireContext())
        this.recycler.layoutManager = linearLayoutManager
        this.noItemsView = view.findViewById<ConstraintLayout>(R.id.no_items)
        this.noItemsView.visibility = View.GONE
        prepareItems()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareItems() {
        this.noItemsView.visibility = View.VISIBLE
        this.recycler.visibility = View.GONE
        DataManagerService(requireContext()).getItems()?.let {
            val filtered = it.filter { todoItem -> !todoItem.done }

            if (filtered.isNotEmpty()) {
                if(this.adapter==null) {
                    this.adapter = TodoItemAdapter(filtered, this)
                    this.recycler.adapter = adapter
                }else{
                    adapter!!.updateItems(filtered)
                }
                this.noItemsView.visibility = View.GONE
                this.recycler.visibility = View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addItemIfAvailable() {
        if(textInput.text.isNullOrBlank()){
            requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator)?.let { Snackbar.make(it, getString(R.string.need_fill_input),Snackbar.LENGTH_SHORT).show() }
        }else{
            val item = TodoItem(textInput.text.toString())
            DataManagerService(requireContext()).addItem(item)
            prepareItems()
        }

        textInput.setText("")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun itemClicked(item: TodoItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.change_priority))
        builder.setMessage(getString(R.string.select_new_priority))

        builder.setPositiveButton(R.string.urgent) { dialog, which ->
            DataManagerService(requireContext()).changePriority(Priority.urgent,item)
            prepareItems()
            requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator)?.let { Snackbar.make(it, getString(R.string.item_updated),Snackbar.LENGTH_SHORT).show() }
            dialog.dismiss()
        }

        builder.setNegativeButton(R.string.important) { dialog, which ->

            DataManagerService(requireContext()).changePriority(Priority.important,item)
            prepareItems()
            requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator)?.let { Snackbar.make(it, getString(R.string.item_updated),Snackbar.LENGTH_SHORT).show() }
            dialog.dismiss()
        }

        builder.setNeutralButton(R.string.normal) { dialog, which ->
            DataManagerService(requireContext()).changePriority(Priority.normal,item)
            prepareItems()
            requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator)?.let { Snackbar.make(it, getString(R.string.item_updated),Snackbar.LENGTH_SHORT).show() }
            dialog.dismiss()
        }
        builder.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun itemLongClicked(item: TodoItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.change_state))
        builder.setMessage(getString(R.string.do_you_want_to_close))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            DataManagerService(requireContext()).changeDoneStatus(item)
            prepareItems()
            requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator)?.let { Snackbar.make(it, getString(R.string.item_updated),Snackbar.LENGTH_SHORT).show() }
            dialog.dismiss()
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()

    }
}