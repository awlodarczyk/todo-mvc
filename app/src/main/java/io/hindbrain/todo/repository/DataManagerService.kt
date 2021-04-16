package io.hindbrain.todo.repository

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.hindbrain.todo.model.Priority
import io.hindbrain.todo.model.TodoItem
import timber.log.Timber
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
class DataManagerService(context: Context) {
    private val TODO_KEY = "io.hindbrain.todo.user-todos"
    private val TODO_ITEMS = "_items_"
    private val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter().nullSafe()).create()
    private var items: ArrayList<TodoItem>? = null
    private var context: Context? = null
    private var preferences: SharedPreferences? = null

    companion object{
        var nextId = 1
    }
    init {
        this.context = context
        this.preferences = context.getSharedPreferences(TODO_KEY, Context.MODE_PRIVATE)
        prepareItems()
    }

    fun changeDoneStatus(item: TodoItem) {
        this.items?.find { item.id == it.id }?.let {
            it.done = !it.done
        }
        storeItems()
    }

    fun changePriority(priority: Priority, item: TodoItem) {
        this.items?.find { item.id == it.id }?.priority = priority
        storeItems()
    }

    fun addItem(item: TodoItem) {
        this.items?.add(item)
        storeItems()
    }

    fun removeItem(index: Int) {
        this.items?.removeAt(index)
        storeItems()
    }

    fun getItems(): ArrayList<TodoItem>? {
        return this.items
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareItems() {
        val it = preferences?.getString(TODO_ITEMS, "")
        if (it!!.isNotEmpty()) {

            val listOfItems: Type = object : TypeToken<ArrayList<TodoItem?>?>() {}.type
            try {
                this.items = gson.fromJson(it, listOfItems)
                nextId = this.items?.size ?: 1
            } catch (ex: JsonSyntaxException) {
                Timber.e(ex)
                this.items = ArrayList()
            }
        } else {
            this.items = ArrayList()
        }
//        this.items?.add(TodoItem(1, "test", false, Priority.normal, LocalDateTime.now()))
//        this.items?.add(
//            TodoItem(
//                2,
//                "test testtest testtest testtest testtest testtest testtest test"+
//                "test testtest testtest testtest testtest testtest testtest test",
//                false,
//                Priority.urgent,
//                LocalDateTime.now()
//            )
//        )
//        this.items?.add(TodoItem(3, "test", false, Priority.important, LocalDateTime.now()))
//        this.items?.add(TodoItem(4, "test", true, Priority.important, LocalDateTime.now()))
    }

    private fun storeItems() {
        val str = gson.toJson(this.items)
        preferences?.edit()?.putString(TODO_ITEMS, str)?.apply()
    }


    class LocalDateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {

        override fun write(out: JsonWriter, value: LocalDateTime) {
            out.value(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value))
        }

        override fun read(input: JsonReader): LocalDateTime = LocalDateTime.parse(input.nextString())
    }
}