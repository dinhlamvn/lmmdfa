package vn.dl.lmmdfa.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BaseListAdapter : ListAdapter<BaseListAdapter.BaseModelView, BaseListAdapter.BaseViewHolder>(
    DiffUtilCallback()
) {

    init {
        setHasStableIds(true)
    }

    fun <T : BaseModelView> submitChange(list: List<T>) {
        submitList(list)
    }

    fun <T : BaseModelView> submitChange(singleItem: T) {
        submitChange(listOf(singleItem))
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layout()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindModel(getItem(position), position)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    abstract class BaseModelView {
        abstract fun id(): String

        @LayoutRes
        abstract fun layout(): Int

        @UiThread
        abstract fun bindView(view: View, adapterPosition: Int)

        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            val idStr = id()
            val takeSize = idStr.length.coerceAtMost(26)
            val sum = idStr.take(takeSize).sumBy { it.toInt() }
            return (sum * 26) / 3
        }
    }

    class BaseViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindModel(modelView: BaseModelView, position: Int) {
            modelView.bindView(view, position)
        }
    }

    private class DiffUtilCallback : DiffUtil.ItemCallback<BaseModelView>() {
        override fun areItemsTheSame(oldItem: BaseModelView, newItem: BaseModelView): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: BaseModelView, newItem: BaseModelView): Boolean {
            return oldItem == newItem
        }
    }
}