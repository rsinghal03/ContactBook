package com.example.contactbook.ui.contactdropdown

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.contactbook.R
import com.example.contactbook.ServiceLocator
import com.example.contactbook.data.DbRepository
import com.example.contactbook.extensions.replace
import com.example.contactbook.ui.ContactBookViewModel
import com.example.contactbook.ui.contactdetails.ContactDetailsFragment
import kotlinx.android.synthetic.main.contact_drop_down_fragment.*

class ContactDropDownFragment : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var listOfContact: ArrayList<String>

    lateinit var model: ContactBookViewModel

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val dbRepository = ServiceLocator.instance(requireContext()).getRepository()
                @Suppress("UNCHECKED_CAST")
                return ContactBookViewModel(dbRepository) as T
            }
        })[ContactBookViewModel::class.java]
        model.insertData()
    }

    private fun updateAdapterList(list: ArrayList<String>) {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return LayoutInflater.from(inflater.context)
            .inflate(R.layout.contact_drop_down_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getListOfContact().observe(this,
            Observer<List<String>> {
                    t -> updateAdapterList(t as ArrayList<String>)
            })
        spinner.onItemSelectedListener = this
        setContinueButtonClick()
    }

    private fun setContinueButtonClick() {
        btn_continue.setOnClickListener {
            replace(ContactDetailsFragment.getInstance(spinner.selectedItem.toString()), R.id.container, true)
        }
    }

    companion object {
        val instance  = ContactDropDownFragment()
    }
}