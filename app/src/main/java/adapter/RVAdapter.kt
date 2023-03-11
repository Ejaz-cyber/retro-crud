package adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.R
import models.StudentModel

class RVAdapter(
    var studentList : ArrayList<StudentModel>,
    val listener : OnItemsClickListener
) :
    RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val lInflator = parent.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = lInflator.inflate(R.layout.user_row, parent, false)

        Log.e("rv", studentList.size.toString())
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int  = studentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.nameTv.text = studentList.get(position).name
        holder.ageTv.text = studentList[position].age.toString()
        holder.collegeTv.text = studentList[position].collegeName
        holder.batchTv.text = studentList[position].batch
        holder.phoneTv.text = studentList[position].phone.toString()

        holder.btnEditStudent.setOnClickListener{
            listener.onEditBtnClick(position)
        }

        holder.btnDeleteStudent.setOnClickListener{
            listener.onDeleteBtnClick(position)
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nameTv : TextView = itemView.findViewById(R.id.name_tv)
        val ageTv : TextView = itemView.findViewById(R.id.age_tv)
        val collegeTv : TextView = itemView.findViewById(R.id.college_tv)
        val batchTv : TextView = itemView.findViewById(R.id.batch_tv)
        val phoneTv : TextView = itemView.findViewById(R.id.phone_tv)

        val btnEditStudent : Button = itemView.findViewById(R.id.edit_btn)
        val btnDeleteStudent : Button = itemView.findViewById(R.id.delete_btn)

    }

    interface OnItemsClickListener{
        fun onEditBtnClick(position: Int)
        fun onDeleteBtnClick(position: Int)
    }

}