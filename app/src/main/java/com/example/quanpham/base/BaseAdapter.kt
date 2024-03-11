package com.example.quanpham.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, VB : ViewBinding> : RecyclerView.Adapter<BaseAdapter<T, VB>.ViewHolder>() {

    val listItem =  mutableListOf<T>()
    private var binding : VB? = null
    var currentPosition : Int  = RecyclerView.NO_POSITION

    abstract fun createBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int) : VB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =  LayoutInflater.from(parent.context)
        binding = createBinding(inflater, parent, viewType)
        return ViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItem[position]
        bind(holder.binding, item, position)
    }

    override fun getItemCount(): Int = listItem.size

    abstract fun bind(binding: VB, item: T, position: Int)

    fun setItems(items: List<T>) {
        this.listItem.clear()
        this.listItem.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0) {
            listItem.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addItems(item: List<T>) {
        this.listItem.addAll(item)
        notifyDataSetChanged()
    }

    fun addItem(item: T, index: Int) {
        this.listItem.add(index, item)
        notifyItemInserted(index)
    }

    fun removeItem(item: T){
        val index = listItem.indexOf(item)
        listItem.remove(item)
        notifyItemRemoved(index)
    }

    fun changeItemWithPos(index: Int, newItem : T){
        if(index < 0  || index >= listItem.size) return
        listItem[index] = newItem;
        notifyItemChanged(index)
    }

    fun getListItem() : ArrayList<T> {
        return listItem as ArrayList<T>
    }

    fun setCurrentPos(position: Int ){
        notifyItemChanged(currentPosition)
        currentPosition = position
        notifyItemChanged(position)
    }
    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}