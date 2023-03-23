package com.example.crud.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.crud.models.StudentModel

class DiffutilAdapter(
    var studentListOld : MutableList<StudentModel>,
    var studentListNew : MutableList<StudentModel>
    ) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = studentListOld.size

    override fun getNewListSize(): Int = studentListNew.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return studentListOld[oldItemPosition].id == studentListNew[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = studentListOld[oldItemPosition]
        val newItem = studentListNew[newItemPosition]

        return oldItem.hashCode() == newItem.hashCode()
    }




}