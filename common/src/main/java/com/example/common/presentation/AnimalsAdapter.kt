package com.example.common.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.common.databinding.ItemAnimalBinding
import com.example.common.utils.setImage
import com.example.common.presentation.model.UIAnimal

class AnimalsAdapter(private val animalClickListener: AnimalClickListener? = null) :
    ListAdapter<UIAnimal, AnimalsAdapter.AnimalsViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalsViewHolder {
        return AnimalsViewHolder.from(parent, animalClickListener)
    }

    override fun onBindViewHolder(holder: AnimalsViewHolder, position: Int) {
        val item: UIAnimal = getItem(position)

        holder.bind(item)
    }

    class AnimalsViewHolder(
        private val binding: ItemAnimalBinding,
        private val animalClickListener: AnimalClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup, listener: AnimalClickListener?): AnimalsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAnimalBinding.inflate(layoutInflater, parent, false)
                return AnimalsViewHolder(binding, listener)
            }
        }

        fun bind(item: UIAnimal) {
            binding.tvName.text = item.name
            binding.ivPhoto.setImage(item.photo)
            binding.root.setOnClickListener {
                animalClickListener?.onAnimalClicked(item.id)
            }
        }
    }
}

val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<UIAnimal>() {
    override fun areItemsTheSame(oldItem: UIAnimal, newItem: UIAnimal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UIAnimal, newItem: UIAnimal): Boolean {
        return oldItem == newItem
    }
}

fun interface AnimalClickListener {
    fun onAnimalClicked(animalId: Long)
}