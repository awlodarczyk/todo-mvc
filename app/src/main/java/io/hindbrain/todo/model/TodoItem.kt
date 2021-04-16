package io.hindbrain.todo.model

import android.os.Build
import androidx.annotation.RequiresApi
import io.hindbrain.todo.repository.DataManagerService
import java.io.Serializable
import java.time.LocalDateTime

enum class Priority{
    normal,important,urgent
}
data class TodoItem(
    var id:Int,
    var name: String,
    var done: Boolean,
    var priority: Priority,
    var createdAt:LocalDateTime
):Serializable
{

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(name:String) : this(DataManagerService.nextId++,name,false,Priority.normal,LocalDateTime.now())
}

