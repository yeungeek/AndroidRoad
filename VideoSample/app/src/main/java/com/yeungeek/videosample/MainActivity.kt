package com.yeungeek.videosample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeungeek.videosample.opengl.RotateGLActivity
import com.yeungeek.videosample.opengl.ShapeActivity
import com.yeungeek.videosample.opengl.SimpleRenderActivity
import com.yeungeek.videosample.recycler.DividerItemDecoration
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var datas: ArrayList<Pair<String, Class<*>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initDatas()
    }

    private fun initViews() {
        mRecyclerView = findViewById(R.id.recycle_view)
    }

    private fun initDatas() {
        datas = arrayListOf()
        datas.add(Pair("OpenGL ES Simple", SimpleRenderActivity::class.java))
        datas.add(Pair("OpenGL ES Triangle", ShapeActivity::class.java))
        datas.add(Pair("Rotate GL SurfaceView", RotateGLActivity::class.java))

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, 20))

        mRecyclerView.adapter = mainAdapter(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d("DEBUG", "##### on item click")
                startActivity(Intent(this@MainActivity, datas.get(position).second))
            }
        }, datas)

    }

    class mainAdapter(
        val mItemClick: OnItemClickListener,
        val mDatas: List<Pair<String, Class<*>>>
    ) :
        RecyclerView.Adapter<mainAdapter.mainViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mainViewHolder {
            return mainViewHolder(
                LayoutInflater.from(parent?.context)
                    .inflate(R.layout.main_recycle_item, null, false)
            )
        }

        override fun getItemCount(): Int {
            return mDatas.size
        }

        override fun onBindViewHolder(holder: mainViewHolder, position: Int) {
            val data = mDatas.get(position)
            holder?.itemTitle?.text = data?.first

            holder?.itemView.setOnClickListener {
                mItemClick.onItemClick(it, position)
            }
        }

        class mainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val itemTitle: TextView

            init {
                itemTitle = itemView.findViewById(R.id.title_tv)
            }
        }


    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}
