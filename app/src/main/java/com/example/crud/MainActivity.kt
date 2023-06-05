package com.example.crud


import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud.adapter.RVAdapter
import com.example.crud.databinding.ActivityMainBinding
import com.example.crud.models.StudentModel
import com.example.crud.retrofit.ApiInterface
import com.example.crud.statelistener.UIState
import com.example.crud.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RVAdapter.OnItemsClickListener {

    @Inject
    lateinit var api: ApiInterface

    private lateinit var rvAdapter: RVAdapter
    lateinit var binding: ActivityMainBinding
    private val mViewModel: MainViewModel by viewModels()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.usersRv.layoutManager = LinearLayoutManager(this@MainActivity)

        /* no need for all these coz we are using Hilt now
            api = RetrofitObj.getRetrofitInstance().create(ApiInterface::class.java)
            repository = MainRepository(api)
            mViewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]
        */
        setUI()
    }

    private fun setUI() {
        if (mViewModel.studentList.value?.isEmpty() == true) setData()
        else putData(mViewModel.studentList.value)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data?.getBooleanExtra("REFRESH_PAGE", false) == true) {
                        Log.e("Main", "fetching")
                        setData()
                    }
                }
            }

        binding.fabAddStud.setOnClickListener {
            resultLauncher.launch(Intent(this@MainActivity, MainActivity2::class.java))
        }
    }

    private fun setData() {
        lifecycleScope.launch {
            mViewModel.fetchStudents().collect {
                when (it) {
                    is UIState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is UIState.Error -> {
                        binding.nodataTv.text = "Error getting data"
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is UIState.Success -> {
                        mViewModel.studentList.postValue(it.data)
                        binding.progressBar.visibility = View.GONE
                        putData(it.data!!)
                    }
                }
            }
        }
    }

    private fun putData(list: List<StudentModel>?) {
        if(list!!.isEmpty()){
            binding.nodataTv.visibility = View.VISIBLE

        }else {
            binding.nodataTv.visibility = View.GONE
        }

        rvAdapter = RVAdapter(list, this@MainActivity)
        binding.usersRv.adapter = rvAdapter
        rvAdapter.submitList(list)
    }

    override fun onEditBtnClick(position: Int) {
        val stud = mViewModel.studentList.value?.get(position)
        val intent = Intent(this@MainActivity, MainActivity2::class.java)
        intent.putExtra("name", stud?.name)
        intent.putExtra("age", stud?.age.toString())
        intent.putExtra("college", stud?.collegeName)
        intent.putExtra("batch", stud?.batch)
        intent.putExtra("phone", stud?.phone)
        intent.putExtra("id", stud?.id)
        resultLauncher.launch(intent)
    }

    override fun onDeleteBtnClick(position: Int) {
        val build: AlertDialog.Builder = AlertDialog.Builder(this)
        build.setTitle("Deleting...")
        build.setMessage("${mViewModel.studentList.value?.get(position)?.name} will be deleted")
        var alertDialog: AlertDialog? = null

        build.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            alertDialog?.cancel()

            lifecycleScope.launch {
                deleteStudent(position)
            }
        })

        build.setNegativeButton("No") { _, _ ->
            alertDialog?.cancel()
        }

        alertDialog = build.create()
        alertDialog?.setCancelable(false)
        alertDialog?.show()

    }

    private suspend fun deleteStudent(position: Int) {
        lifecycleScope.launch {
            mViewModel.deleteStudent(
                mViewModel.studentList.value?.get(position)?.id.toString(),
                object : Check2 {
                    override fun onSuccess(response: Unit?) {
                        setData()
                        Toast.makeText(this@MainActivity, "student deleted", Toast.LENGTH_SHORT)
                            .show()

                    }

                    override fun onFailure(response: Unit?) {
                        Toast.makeText(
                            this@MainActivity,
                            "student delete Failed",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }

                })
        }
    }
}


