package com.bugevil.mhelmi.weatherforecast.features.home.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bugevil.mhelmi.weatherforecast.databinding.ItemCityBinding
import com.bugevil.mhelmi.weatherforecast.features.home.data.models.City
import com.bugevil.mhelmi.weatherforecast.utils.layoutInflater

class CitiesAdapter(private val cityActionsListener: CityActionsListener) :
  ListAdapter<City, CitiesAdapter.SearchViewHolder>(callback) {

  companion object {
    private val callback = object : DiffUtil.ItemCallback<City>() {
      override fun areItemsTheSame(oldItem: City, newItem: City) = oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: City, newItem: City) = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
    val binding = ItemCityBinding.inflate(parent.layoutInflater, parent, false)
    return SearchViewHolder(binding)
  }

  override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class SearchViewHolder(private val binding: ItemCityBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(city: City) {
      binding.tvTitle.text = city.toString()
      binding.checkboxFavorite.isChecked = city.isFavorite
      binding.checkboxFavorite.setOnCheckedChangeListener { _, isChecked ->
        city.apply { isFavorite = isChecked }
          .also { cityActionsListener.onUpdateCityFavorite(city) }
      }
      binding.cardViewCity.setOnClickListener { cityActionsListener.onCityClick(city) }
    }
  }

  interface CityActionsListener {
    fun onCityClick(city: City)
    fun onUpdateCityFavorite(city: City)
  }
}