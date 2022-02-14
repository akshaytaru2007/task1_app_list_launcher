package com.example.package_application_task1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akshay_taru.package_library.entites.AppInfo
import com.example.package_application_task1.databinding.RowItemBinding

class AppListAdapter() :
    ListAdapter<AppInfo, AppListAdapter.AppInfoViewHolder>(AppInfoComparator()) {
    private val TAG = "AppListAdapter"
    private lateinit var context: Context
    private val installedApps: MutableList<AppInfo> = mutableListOf()

    class AppInfoComparator : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.name == newItem.name
        }

    }

    inner class AppInfoViewHolder(private val binding: RowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(appInfo: AppInfo, position: Int) {
            binding.apply {
                userName.text = appInfo.name
                userImage.setImageDrawable(appInfo.icon)
                row.setOnClickListener {
                    try {
                        val launchIntent =
                            context.packageManager.getLaunchIntentForPackage(appInfo.packageName)
                        launchIntent?.let {
                            context.startActivity(launchIntent)
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, e.localizedMessage)

                    }
                }
            }
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    fun getData(data: MutableList<AppInfo>) {
        this.installedApps.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return AppInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppInfoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }
}