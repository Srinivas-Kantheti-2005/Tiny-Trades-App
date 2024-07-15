package com.example.tinytrades

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.Room
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.Item
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), HomeRecyclerViewAdapter.OnItemClickListener {

    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var database: AppDatabase

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var explorebtn: ImageButton
    private lateinit var sellbtn: ImageButton
    private lateinit var profilebtn: ImageButton

    private lateinit var adapter: HomeRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tinytrades-database"
        )
            .fallbackToDestructiveMigration()
            .build()

        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)

        explorebtn = findViewById(R.id.explore)
        sellbtn = findViewById(R.id.sell)
        profilebtn = findViewById(R.id.profile)

        // Initialize RecyclerView
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = HomeRecyclerViewAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter

        // Setup SearchView
        searchView.queryHint = "Search Tiny Trades..."
        setupSearchView()

        explorebtn.setOnClickListener {
            searchView.requestFocus()
            searchView.isIconified = false
        }

        // Set click listeners
        sellbtn.setOnClickListener {
            val sellIntent = Intent(this, SellActivity::class.java)
            startActivity(sellIntent)
        }

        profilebtn.setOnClickListener {
            val profileIntent = Intent(this, ProfileActivity::class.java)
            startActivity(profileIntent)
        }

        // Load items initially
        loadItemsFromDatabase()
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val queryText = newText.orEmpty().trim()
                filterData(queryText)
                return true
            }
        })
    }

    private fun filterData(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val filteredItems = with(Dispatchers.IO) {
                if (query.isEmpty()) {
                    itemDao.getAllItems() // Fetch all items if query is empty
                } else {
                    itemDao.searchItems("%$query%") // Search items with the query in their title
                }
            }
            adapter.updateItems(filteredItems)
        }
    }

    private fun loadItemsFromDatabase() {
        GlobalScope.launch(Dispatchers.Main) {
            val allItems = with(Dispatchers.IO) {
                itemDao.getAllItems()
            }
            adapter.updateItems(allItems)
        }
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

    private fun showToastMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(item: Item) {

    }
}
