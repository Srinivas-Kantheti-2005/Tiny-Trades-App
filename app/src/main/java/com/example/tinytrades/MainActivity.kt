package com.example.tinytrades

import AdapterClass
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterClass
    private lateinit var searchView: SearchView
    private lateinit var explorebtn: ImageButton
    private lateinit var profilebtn: ImageButton
    private lateinit var sellbtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        searchView = findViewById(R.id.searchView)
        explorebtn = findViewById(R.id.explore)
        profilebtn = findViewById(R.id.profile)
        sellbtn = findViewById(R.id.sell)

        // Set click listeners
        sellbtn.setOnClickListener {
            val sellIntent = Intent(this, SellActivity::class.java)
            startActivity(sellIntent)
        }

        profilebtn.setOnClickListener {
            val profileIntent = Intent(this, ProfileActivity::class.java)
            startActivity(profileIntent)
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = AdapterClass(DataManager.itemsList)
        recyclerView.adapter = adapter

        // Setup SearchView
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
        val filteredList = DataManager.itemsList.filter {
            it.dataTitle.contains(query, ignoreCase = true)
        }
        adapter.updateList(filteredList as ArrayList<DataClass>)
    }

    override fun onResume() {
        super.onResume()
        // Handle any intent passed to MainActivity
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        // Check if intent has extra "navigate_to" and handle accordingly
        val navigateTo = intent.getStringExtra("navigate_to")
        if (navigateTo == "explore") {
            navigateToExplore()
        }
    }

    private fun navigateToExplore() {
        // Programmatically perform click on explore button
        explorebtn.performClick()
    }
}
