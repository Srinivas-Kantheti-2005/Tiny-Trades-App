package com.example.tinytrades

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.appcompat.widget.SearchView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var adapter: AdapterClass
    private lateinit var searchView: SearchView
    private lateinit var explorebtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.searchView)
        explorebtn = findViewById(R.id.explore)

        val imageList = arrayOf(
            R.drawable.facebook_mini,
            R.drawable.google_mini,
            R.drawable.instagram_mini,
            R.drawable.twitter_mini
        )

        val titleList = arrayOf(
            "Facebook",
            "Google",
            "Instagram",
            "Twitter"
        )

        val priceList = arrayOf(
            "150",
            "200",
            "300",
            "100"
        )

        dataList = arrayListOf()
        repeat(6) { // Repeat the list six times
            for (i in imageList.indices) {
                val dataClass = DataClass(imageList[i], titleList[i], priceList[i])
                dataList.add(dataClass)
            }
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = AdapterClass(dataList)
        recyclerView.adapter = adapter

        searchView.queryHint = "Search Tiny Trades..."
        setupSearchView()

        explorebtn.setOnClickListener {
            searchView.requestFocus()
            searchView.isIconified = false
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val queryText = newText ?: ""
                filterData(queryText)
                return true
            }
        })
    }

    private fun filterData(query: String) {
        val filteredList = dataList.filter {
            it.dataTitle.contains(query, ignoreCase = true)
        }
        adapter.updateList(filteredList as ArrayList<DataClass>)
    }
}
