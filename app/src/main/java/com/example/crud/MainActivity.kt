package com.example.crud

import adapter.RVAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import models.StudentModel
import models.StudentModelPost
import retrofit.ApiInterface
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), RVAdapter.OnItemsClickListener{

    lateinit var btnAddStud : FloatingActionButton
    lateinit var rView: RecyclerView

    // change this api with new crudcrud.com's api for another 24hr use
    val base_url = "https://crudcrud.com/api/338ecf5cbdf446eeb44f2e2cc8d9ce4d/"



    var studentList : ArrayList<StudentModel> = ArrayList()
    var rvAdapter : RVAdapter = RVAdapter(studentList, this)

    var retrofit : Retrofit? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // settring up recycler view
        rView = findViewById(R.id.users_rv)

        rView.setHasFixedSize(true)
        rView.layoutManager = LinearLayoutManager(this@MainActivity)
        rView.adapter = rvAdapter


        retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()



        fetchAllStudents()

        btnAddStud = findViewById(R.id.fab_add_stud)
        btnAddStud.setOnClickListener{
            addUser()
        }


    }

    private fun fetchAllStudents(){
        // retrofit instance

//        val retrofit : Retrofit = Retrofit.Builder()
//            .baseUrl(base_url)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()



        // getting all Students from api
        val apiCall : ApiInterface? = retrofit?.create(ApiInterface::class.java)
        val call : Call<ArrayList<StudentModel>>? = apiCall?.getAllStudents()
        call?.enqueue(object : Callback<ArrayList<StudentModel>?> {
            override fun onResponse(
                call: Call<ArrayList<StudentModel>?>,
                response: Response<ArrayList<StudentModel>?>
            ) {
                if(response.isSuccessful) {

                    studentList.clear()

                    for(obj in response.body()!!){
                        studentList.add(obj)
                    }

                    // setting no data textview
                    val noDataTv = findViewById<TextView>(R.id.nodata_tv)
                    if(studentList.size == 0){
                        rView.visibility = View.GONE

                        noDataTv.visibility = View.VISIBLE
                    }else{
                        rView.visibility = View.VISIBLE
                        noDataTv.visibility = View.GONE
                    }

                    rvAdapter.notifyDataSetChanged()

//                    Toast.makeText(this@MainActivity, "size = "+studentList?.size, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "Oops Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<StudentModel>?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error Getting Students", Toast.LENGTH_LONG).show()

                Log.e("MainActivity", t.localizedMessage as String)

            }
        })

    }

    private fun addUser() {
        // creating dialog box to add new user

        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.edit_user)

        var nameEt = dialog.findViewById<EditText>(R.id.name_et)
        var ageEt = dialog.findViewById<EditText>(R.id.age_et)
        var collegeEt = dialog.findViewById<EditText>(R.id.college_et)
        var batchEt = dialog.findViewById<EditText>(R.id.batch_et)
        var phoneEt = dialog.findViewById<EditText>(R.id.phone_et)
        var titleTv = dialog.findViewById<TextView>(R.id.dialog_title)

        titleTv.setText("Enter Details")

        val btnSave = dialog.findViewById<Button>(R.id.btn_save_user)

        btnSave.setOnClickListener{

            var name = nameEt.text?.toString()
            var age = ageEt.text.toString().toInt()
            var college = collegeEt.text?.toString()
            var batch = batchEt.text?.toString()
            var phone = phoneEt.text?.toString()

            val newStudent : StudentModelPost = StudentModelPost(name, age,college,batch,phone)
            addNewUser(newStudent)
            dialog.cancel()
        }

        val btnCancel : Button = dialog.findViewById(R.id.btn_cancel_dialog)
        btnCancel.setOnClickListener { dialog.cancel() }


        dialog.show()

    }

    private fun addNewUser(newStudent: StudentModelPost){


//        val retrofit : Retrofit = Retrofit.Builder()
//            .baseUrl(base_url)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()

        val apiCall : ApiInterface? = retrofit?.create(ApiInterface::class.java)
        val call : Call<StudentModelPost>? = apiCall?.postStudent(newStudent)
        call?.enqueue(object : Callback<StudentModelPost?> {
            override fun onResponse(
                call: Call<StudentModelPost?>,
                response: Response<StudentModelPost?>
            ) {
                if(response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "New student added", Toast.LENGTH_SHORT)
                        .show()

                    fetchAllStudents()

                }else
                    Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()

            }

            override fun onFailure(call: Call<StudentModelPost?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to add new student", Toast.LENGTH_SHORT).show()
                Log.e("Main", t.localizedMessage)

            }
        })


    }

    override fun onEditBtnClick(position: Int) {
        Log.e("main", "------- = $position")

        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.edit_user)

        var nameEt = dialog.findViewById<EditText>(R.id.name_et)
        var ageEt = dialog.findViewById<EditText>(R.id.age_et)
        var collegeEt = dialog.findViewById<EditText>(R.id.college_et)
        var batchEt = dialog.findViewById<EditText>(R.id.batch_et)
        var phoneEt = dialog.findViewById<EditText>(R.id.phone_et)
        var titleTv = dialog.findViewById<TextView>(R.id.dialog_title)

        titleTv.setText("Edit Details")

        // putting already existing data
        nameEt.setText(studentList[position].name)
        ageEt.setText(studentList[position].age.toString())
        collegeEt.setText(studentList[position].collegeName)
        batchEt.setText(studentList[position].batch)
        phoneEt.setText(studentList[position].phone.toString())

        val btnSave = dialog.findViewById<Button>(R.id.btn_save_user)


        btnSave.setOnClickListener{

            var name = nameEt.text?.toString()
            var age = ageEt.text.toString().toInt()
            var college = collegeEt.text?.toString()
            var batch = batchEt.text?.toString()
            var phone = phoneEt.text?.toString()

            val newStudent : StudentModelPost = StudentModelPost(name, age,college,batch,phone)
            editUser(newStudent, studentList[position].id)

//            Toast.makeText(this@MainActivity, ""+studentList[position].id, Toast.LENGTH_SHORT).show()
            dialog.cancel()
        }

        val btnCancel : Button = dialog.findViewById(R.id.btn_cancel_dialog)
        btnCancel.setOnClickListener { dialog.cancel() }



        dialog.show()


    }

    private fun editUser(student: StudentModelPost, id: String) {
        val apiCall: ApiInterface? = retrofit?.create(ApiInterface::class.java)
        val call: Call<Void>? = apiCall?.updateStudent(id, student)
        call?.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Student updated", Toast.LENGTH_SHORT).show()

                    fetchAllStudents()

                } else {

                    val errorMessage = "Failed to update student: ${response.code()}"
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.e("Main", errorMessage)
                }

                Log.e("Main", response.body()?.toString() ?: "Response body is null")
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                val errorMessage = "Failed to update student: ${t.localizedMessage}"
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                Log.e("Main", errorMessage)
            }
        })
    }


    override fun onDeleteBtnClick(position: Int) {


//        val retrofit : Retrofit = Retrofit.Builder()
//            .baseUrl(base_url)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()

        val build : AlertDialog.Builder = AlertDialog.Builder(this)
        build.setTitle("Deleting...")
        build.setMessage("Are you sure??")
        var alertDialog :AlertDialog? = null

        build?.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            alertDialog?.cancel()
            deleteStudent(position)
        })
        build?.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
            alertDialog?.cancel()
        })

        alertDialog = build?.create()
        alertDialog?.setCancelable(false)
        alertDialog?.show()



    }

    private fun deleteStudent(position : Int) {

        val apiCall : ApiInterface? = retrofit?.create(ApiInterface::class.java)
        val call : Call<Void>? = apiCall?.deleteUser(studentList[position].id)
        call?.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                Toast.makeText(this@MainActivity, "Deleted", Toast.LENGTH_SHORT).show()
                fetchAllStudents()
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to Delete", Toast.LENGTH_SHORT).show()
                Log.e("Main", t.localizedMessage)
            }
        })

    }


}