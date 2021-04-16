package io.hindbrain.todo.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.hindbrain.todo.R
import io.hindbrain.todo.extensions.inflate
import io.hindbrain.todo.model.Priority
import io.hindbrain.todo.model.TodoItem
import timber.log.Timber
import java.lang.Exception
import java.time.format.DateTimeFormatter
interface TodoItemListener{
   fun itemClicked(item:TodoItem)
   fun itemLongClicked(item:TodoItem)
}
class TodoItemAdapter(private var items: List<TodoItem>, @Nullable private val listener:TodoItemListener? = null) :
    RecyclerView.Adapter<TodoItemAdapter.TodoItemHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoItemAdapter.TodoItemHolder {
        val inflatedView = parent.inflate(R.layout.adapter_todo_item, false)
        return TodoItemHolder(inflatedView,listener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TodoItemAdapter.TodoItemHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    private fun getItem(position: Int): TodoItem = items[position]

    override fun getItemCount() = items.size
    fun updateItems(items: List<TodoItem>){
        this.items = items
        notifyDataSetChanged()
    }
    class TodoItemHolder(v: View, private val listener:TodoItemListener?) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {
        //2
        private var view: View = v
        private var item: TodoItem? = null

        //3
        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            Timber.d("Item CLICK!")
            item?.let { listener?.itemClicked(it) }
        }
        //5
        override fun onLongClick(p0: View?): Boolean {
            Timber.d("Item Long CLICK!")
            item?.let {
                listener?.itemLongClicked(it) }
            return true
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItem(item: TodoItem) {
            this.item = item
            val description = view.findViewById<TextView>(R.id.item_description)
            description.text = item.name
            if (item.done) {
                description.setTextColor(Color.GRAY)
            } else {
                description.setTextColor(Color.BLACK)
            }
            val date =  view.findViewById<TextView>(R.id.item_date)
            try {
               date.text = item.createdAt.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
               )
            }catch (e:Exception){
                date.text = "-"
            }
            val drawable =
                ContextCompat.getDrawable(view.context, R.drawable.circle_shape)?.mutate()

            val priority = view.findViewById<View>(R.id.item_priority)
            when (item.priority) {
                Priority.urgent -> drawable?.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(view.context, R.color.red),
                    PorterDuff.Mode.SRC_ATOP
                )
                Priority.important -> drawable?.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(view.context, R.color.yellow),
                    PorterDuff.Mode.SRC_ATOP
                )
                else -> drawable?.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(view.context, R.color.green),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
            priority.background = drawable

        }
    }
}
