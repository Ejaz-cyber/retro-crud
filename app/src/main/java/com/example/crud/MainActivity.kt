package com.example.crud


import android.app.Activity
import com.example.crud.adapter.RVAdapter
import android.app.AlertDialog

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud.adapter.DiffutilAdapter
import com.example.crud.databinding.ActivityMainBinding
import com.example.crud.models.StudentModel
import com.example.crud.repository.MainRepo
import com.example.crud.retrofit.ApiInterface
import com.example.crud.retrofit.RetrofitObj
import com.example.crud.viewmodel.MainViewModel
import com.example.crud.viewmodel.MainViewModelFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity(), RVAdapter.OnItemsClickListener {
    var studentList: List<StudentModel> = emptyList()
    lateinit var rvAdapter: RVAdapter

    lateinit var binding: ActivityMainBinding

    private lateinit var api: ApiInterface
    private lateinit var repo: MainRepo
    private lateinit var mainViewModel: MainViewModel

    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // setting up recycler view
        binding.usersRv.layoutManager = LinearLayoutManager(this@MainActivity)



        api = RetrofitObj.getRetrofitInstance().create(ApiInterface::class.java)
        repo = MainRepo(api)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repo))[MainViewModel::class.java]

        fetchStudents()


        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data?.getBooleanExtra("REFRESH_PAGE", false) == true) {
                        Log.e("Main", "onCreate: fetching")
                        mainViewModel.fetchStudents()
                    }
                }
            }

        binding.fabAddStud.setOnClickListener {
            resultLauncher.launch(Intent(this@MainActivity, MainActivity2::class.java))
        }

    }

    fun fetchStudents() {
        mainViewModel.studentList.observe(this, Observer {
            studentList = it
            rvAdapter = RVAdapter(it as MutableList<StudentModel>, this)
            binding.usersRv.adapter = rvAdapter
            Log.e("Main", "onCreate: studentlist = ${it.size}")
            rvAdapter.submitList(it)
        })
    }


    override fun onEditBtnClick(position: Int) {
        val stud = studentList[position]
        val intent = Intent(this@MainActivity, MainActivity2::class.java)
        intent.putExtra("name", stud.name)
        intent.putExtra("age", stud.age.toString())
        intent.putExtra("college", stud.collegeName)
        intent.putExtra("batch", stud.batch)
        intent.putExtra("phone", stud.phone)
        intent.putExtra("id", stud.id)
        resultLauncher.launch(intent)
    }

    override fun onDeleteBtnClick(position: Int) {
        val build: AlertDialog.Builder = AlertDialog.Builder(this)
        build.setTitle("Deleting...")
        build.setMessage("${studentList[position].name} will be deleted")
        var alertDialog: AlertDialog? = null

        build.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            alertDialog?.cancel()


            lifecycleScope.launch {
                deleteStudent(position)
            }

        })

        build.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
            alertDialog?.cancel()
        })

        alertDialog = build.create()
        alertDialog?.setCancelable(false)
        alertDialog?.show()

    }

    private suspend fun deleteStudent(position: Int) {
//        lifecycleScope.async {
//            mainViewModel.deleteStudent(studentList[position].id)
//        }.await()
//        fetchStudents()
//        Toast.makeText(this@MainActivity, "student deleted", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {
            mainViewModel.deleteStudent(studentList[position].id, object : Check2 {
                override fun onSuccess(response: Unit?) {
                    mainViewModel.fetchStudents()
                    Toast.makeText(this@MainActivity, "student deleted", Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(response: Unit?) {
                    Toast.makeText(this@MainActivity, "student delete Failed", Toast.LENGTH_SHORT).show()

                }

            })
        }
    }

}


