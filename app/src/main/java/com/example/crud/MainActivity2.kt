package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.crud.databinding.ActivityMain2Binding
import com.example.crud.models.StudentModelPost
import com.example.crud.repository.MainRepo
import com.example.crud.retrofit.ApiInterface
import com.example.crud.retrofit.RetrofitObj
import com.example.crud.viewmodel.MainViewModel
import com.example.crud.viewmodel.MainViewModelFactory
import com.example.crud.viewmodel.ViewModel2
import com.example.crud.viewmodel.ViewModel2Factory
import java.util.*

class MainActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityMain2Binding

    private lateinit var api: ApiInterface
    private lateinit var repo: MainRepo
    private lateinit var viewModel2: ViewModel2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.text = "Enter New Student Details"

        api = RetrofitObj.getRetrofitInstance().create(ApiInterface::class.java)
        repo = MainRepo(api)
        viewModel2 = ViewModelProvider(this, ViewModel2Factory(repo)).get(ViewModel2::class.java)

        if (intent.hasExtra("name")) {
            val name = intent.getStringExtra("name")
            val age = intent.getStringExtra("age")
            val college = intent.getStringExtra("college")
            val batch = intent.getStringExtra("batch")
            val phone = intent.getStringExtra("phone")

            binding.title.text = "Edit Student Details"
            binding.ageEt.setText(age.toString())
            binding.batchEt.setText(batch)
            binding.nameEt.setText(name)
            binding.collegeEt.setText(college)
            binding.phoneEt.setText(phone)
        }

        binding.btnSaveUser.setOnClickListener {

            val name = binding.nameEt.text?.toString()
            val age = binding.ageEt.text.toString().toInt()
            val college = binding.collegeEt.text?.toString()
            val batch = binding.batchEt.text?.toString()
            val phone = binding.phoneEt.text?.toString()

            val stud = StudentModelPost(name, age, college, batch, phone)

            if (intent.hasExtra("name")) {
                val id = intent.getStringExtra("id")
                editUser(stud, id.toString())
            } else {
                addNewUser(stud)
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

    }

    private fun editUser(stud: StudentModelPost, id: String) {
        viewModel2.updateStudent(id, stud, object : Check2 {
            override fun onSuccess(response: Unit?) {
                Toast.makeText(this@MainActivity2, "student updated", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity2, MainActivity::class.java)
                intent.putExtra("REFRESH_PAGE", true)
                setResult(RESULT_OK, intent)
                finish()

            }

            override fun onFailure(response: Unit?) {
                Toast.makeText(this@MainActivity2, "student update failed", Toast.LENGTH_SHORT)
                    .show()

            }
        })
    }

    private fun addNewUser(newStudent: StudentModelPost) {
        viewModel2.postStudent(newStudent, object : Check {
            override fun onSuccess(response: Objects?) {

                Toast.makeText(this@MainActivity2, "student added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity2, MainActivity::class.java)
                intent.putExtra("REFRESH_PAGE", true)
                setResult(RESULT_OK, intent)
                finish()

            }

            override fun onFailure(response: Objects?) {
                Toast.makeText(this@MainActivity2, "failed to add", Toast.LENGTH_SHORT)
                    .show()
            }

        })

    }
}