package com.yeungeek.avsample

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeungeek.avsample.activities.media.AudioRecordActivity
import com.yeungeek.avsample.activities.media.MediaMuxerActivity
import com.yeungeek.avsample.activities.media.MediaPlayerActivity
import com.yeungeek.avsample.activities.media.MediaRecorderActivity
import com.yeungeek.avsample.activities.opengl.airhockey.AirHockeyActivity
import com.yeungeek.avsample.activities.opengl.first.FirstOpenGLActivity
import com.yeungeek.avsample.activities.opengl.renderer.FirstOpenGLRenderer
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

private const val RC_STORAGE = 1000

class MainActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mDatas: ArrayList<Pair<String, Class<*>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()

        initViews()
        initDatas()
    }

    private fun initViews() {
        mRecyclerView = findViewById(R.id.recycler_view)
    }

    private fun initDatas() {
        mDatas = arrayListOf()
        mDatas.add(Pair("play video with MediaPlayer", MediaPlayerActivity::class.java))
        mDatas.add(Pair("media recorder", MediaRecorderActivity::class.java))
        mDatas.add(Pair("audio record", AudioRecordActivity::class.java))
        mDatas.add(Pair("media muxer", MediaMuxerActivity::class.java))

        //opengl
        mDatas.add(Pair("first opengl renderer", FirstOpenGLActivity::class.java))
        mDatas.add(Pair("opengl: air hockey", AirHockeyActivity::class.java))

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        mRecyclerView.adapter = MainAdapter(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                startActivity(Intent(this@MainActivity, mDatas[position].second))
            }
        }, mDatas)
    }

    @AfterPermissionGranted(RC_STORAGE)
    fun requestPermissions() {
        val perms = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            //
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_storage), RC_STORAGE, *perms
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //class
    class MainAdapter(
        private val itemClick: OnItemClickListener,
        private val items: List<Pair<String, Class<*>>>
    ) : RecyclerView.Adapter<MainViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            return MainViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.main_recycle_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val data = items[position]
            holder.itemTitle.text = data.first

            holder.itemView.setOnClickListener {
                itemClick.onItemClick(it, position)
            }
        }

        override fun getItemCount(): Int {
            return items?.size
        }
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.title_tv)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}