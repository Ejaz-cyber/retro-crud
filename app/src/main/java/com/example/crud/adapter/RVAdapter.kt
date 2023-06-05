package com.example.crud.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.databinding.UserRowBinding
import com.example.crud.models.StudentModel
import javax.inject.Inject

class RVAdapter @Inject constructor(
    var studentList: List<StudentModel>?,
    val listener: OnItemsClickListener
) :
    ListAdapter<StudentModel, RVAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding: UserRowBinding =
            UserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Log.e("rv", studentList?.size.toString())
        return ViewHolder(binding)
    }

    //override fun getItemCount(): Int  = studentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.model = getItem(position)
        //holder.binding.executePendingBindings()

        holder.binding.editBtn.setOnClickListener {
            listener.onEditBtnClick(position)
        }

        holder.binding.deleteBtn.setOnClickListener {
            listener.onDeleteBtnClick(position)
        }
    }

    class ViewHolder(val binding: UserRowBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemsClickListener {
        fun onEditBtnClick(position: Int)
        fun onDeleteBtnClick(position: Int)
    }

    fun updateList(newList: List<StudentModel>) {
//        studentList = newList
//        notifyDataSetChanged()


        val diffResult = DiffUtil.calculateDiff(
            DiffutilAdapter(
                studentList as MutableList<StudentModel>,
                newList as MutableList<StudentModel>
            )
        )
        studentList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<StudentModel>() {

            override fun areItemsTheSame(oldItem: StudentModel, newItem: StudentModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StudentModel, newItem: StudentModel): Boolean {
                return oldItem == newItem
            }
        }
    }


}