package com.bugevil.mhelmi.weatherforecast.utils.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<BINDING : ViewBinding> : Fragment() {
  lateinit var binding: BINDING
  lateinit var navController: NavController

  protected abstract fun onBind(inflater: LayoutInflater, container: ViewGroup?): BINDING

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = onBind(inflater, container)
    return binding.root
  }

  @CallSuper
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    navController = Navigation.findNavController(binding.root)
  }

  fun onBackButtonPressed() {
  }
}