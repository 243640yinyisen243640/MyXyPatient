/*
 * Copyright (c) 2020. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.lyd.baselib.base.viewbinding;

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class FragmentBindingDelegate<VB : ViewBinding>(
        private val clazz: Class<VB>
) : ReadOnlyProperty<Fragment, VB> {

    private var isInitialized = false
    private var _binding: VB? = null
    private val binding: VB get() = _binding!!

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (!isInitialized) {
            thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroyView() {
                    _binding = null
                }
            })
            _binding = clazz.getMethod("bind", View::class.java)
                    .invoke(null, thisRef.requireView()) as VB
            isInitialized = true
        }
        return binding
    }
}