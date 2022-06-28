package com.project.mvvm_booksearch

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.project.mvvm_booksearch.adapter.BookListAdapter
import com.project.mvvm_booksearch.databinding.ActivityMainBinding
import com.project.mvvm_booksearch.network.BookListModel
import com.project.mvvm_booksearch.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    private lateinit var bookListAdapter: BookListAdapter

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initSearchBox()
        initRecyclerView()

    }

    private fun initSearchBox() = with(binding) {
        inputBookName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loadAPIData(s.toString())
            }
        })
    }

    private fun initRecyclerView() = with(binding) {
        recyclerView.apply {
            val layoutManager = LinearLayoutManager(this@MainActivity)
            val decoration = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(decoration)

            bookListAdapter = BookListAdapter()

            adapter = bookListAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadAPIData(input: String) {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getBookListObserver().observe(this, Observer<BookListModel> {
            if (it != null) {
                // update adapter
                bookListAdapter.bookListData = it.items
                bookListAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Error in fetching data", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.makeApiCall(input)
    }
}