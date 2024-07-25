package com.example.tinytrades

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tinytrades.database.AppDatabase
import com.example.tinytrades.database.Item
import com.example.tinytrades.database.ItemDao
import com.example.tinytrades.database.ProfileDao
import com.example.tinytrades.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), HomeRecyclerViewAdapter.OnItemClickListener {

    private lateinit var userDao: UserDao
    private lateinit var profileDao: ProfileDao
    private lateinit var itemDao: ItemDao
    private lateinit var database: AppDatabase

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    private lateinit var addToCart: ImageButton
    private lateinit var userName: TextView
    private lateinit var exploreBtn: ImageButton
    private lateinit var sellBtn: ImageButton
    private lateinit var profileBtn: ImageButton

    private lateinit var adapter: HomeRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = intent.getStringExtra("USERNAME") ?: ""

        database = AppDatabase.getDatabase(applicationContext)
        userDao = database.userDao()
        profileDao = database.profileDao()
        itemDao = database.itemDao()

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)

        addToCart = findViewById(R.id.add_to_cart)
        userName = findViewById(R.id.username)
        exploreBtn = findViewById(R.id.explore)
        sellBtn = findViewById(R.id.sell)
        profileBtn = findViewById(R.id.profile)

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = HomeRecyclerViewAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter

        searchView.queryHint = "Search Tiny Trades..."
        setupSearchView()

        userName.text = username

        addToCart.setOnClickListener {
            val cartIntent = Intent(this, AddToCart::class.java).apply {
                putExtra("USERNAME", userName.text.toString())
            }
            startActivity(cartIntent)
        }

        exploreBtn.setOnClickListener {
            searchView.requestFocus()
            searchView.isIconified = false
        }

        sellBtn.setOnClickListener {
            val sellIntent = Intent(this, SellActivity::class.java).apply {
                putExtra("USERNAME", userName.text.toString())
            }
            startActivity(sellIntent)
        }

        profileBtn.setOnClickListener {
            val profileIntent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("USERNAME", userName.text.toString())
            }
            startActivity(profileIntent)
        }

        observeItemsFromDatabase()
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
        lifecycleScope.launch {
            try {
                val filteredItems = withContext(Dispatchers.IO) {
                    if (query.isEmpty()) {
                        itemDao.getAllItems()
                    } else {
                        itemDao.searchItems("%$query%")
                    }
                }
                adapter.updateItems(filteredItems)
            } catch (e: Exception) {
                showToastMsg("Failed to fetch items: ${e.message}")
            }
        }
    }

    private fun observeItemsFromDatabase() {
        itemDao.getAllItemsLive().observe(this, Observer { items ->
            adapter.updateItems(items)
        })
    }

    override fun onResume() {
        super.onResume()
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val navigateTo = intent.getStringExtra("navigate_to")
        if (navigateTo == "explore") {
            navigateToExplore()
        }
    }

    private fun navigateToExplore() {
        exploreBtn.performClick()
    }

    private fun showToastMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(item: Item) {
        val intent = Intent(this, ItemDetailsActivity::class.java).apply {
            putExtra("itemImage", item.image)
            putExtra("itemTitle", item.title)
            putExtra("itemSize", item.size)
            putExtra("itemPrice", item.price)
            putExtra("sellerUsername", item.username)
            putExtra("buyerUsername", userName.text.toString())
        }
        startActivity(intent)
    }
}
