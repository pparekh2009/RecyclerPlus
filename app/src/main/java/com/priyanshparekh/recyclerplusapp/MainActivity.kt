package com.priyanshparekh.recyclerplusapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.priyanshparekh.recyclerplus.RecyclerPlusTouchHelper
import com.priyanshparekh.recyclerplus.SwipeConfig
import com.priyanshparekh.recyclerplusapp.databinding.ActivityMainBinding
import java.util.ArrayList
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemList = ArrayList<String>().apply {
            for (i in 1..10) {
                add("Sample Item: $i")
            }
        }

        var itemViewAdapter: ItemListAdapter? = null


        val leftSwipeConfig = SwipeConfig.Builder()
            .setText("Delete")
            .setBackgroundColor("#ba280b".toColorInt())
            .setTextSize(50f)
            .setTextColor(Color.WHITE)
            .setIcon(this, R.drawable.rounded_delete_24)
            .setIconTint(Color.WHITE)
            .setIconSize(80)
            .setOnSwipeListener { position ->
                itemList.removeAt(position)
                itemViewAdapter?.notifyItemRemoved(position)
                Toast.makeText(this, "Swiped Left: $position", Toast.LENGTH_SHORT).show()
            }
            .build()

        val rightSwipeConfig = SwipeConfig.Builder()
            .setText("Edit")
            .setBackgroundColor("#1d3ee0".toColorInt())
            .setTextSize(50f)
            .setTextColor(Color.WHITE)
            .setIcon(this, R.drawable.rounded_delete_24)
            .setIconSize(80)
            .setIconTint(Color.WHITE)
            .setOnSwipeListener { position ->
                Toast.makeText(this, "Swiped Right: $position", Toast.LENGTH_SHORT).show()
            }
            .build()

        val recyclerPlusTouchHelper = RecyclerPlusTouchHelper.Builder()
            .enableDrag()
            .enableDragOnView()
            .setLeftSwipeConfig(leftSwipeConfig)
            .setRightSwipeConfig(rightSwipeConfig)
            .create()

        itemViewAdapter = ItemListAdapter(itemList, recyclerPlusTouchHelper)

        binding.itemList.adapter = itemViewAdapter
        binding.itemList.layoutManager = LinearLayoutManager(this)

        binding.itemList.setTouchHelper(recyclerPlusTouchHelper)
    }
}