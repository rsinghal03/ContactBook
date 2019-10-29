package com.example.contactbook.ui.contactdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.contactbook.R
import com.example.contactbook.ServiceLocator
import com.example.contactbook.data.model.ContactDetails
import com.example.contactbook.ui.ContactBookViewModel
import kotlinx.android.synthetic.main.contact_details_fragment.*

const val CONTACT_ID_KEY = "contact"

class ContactDetailsFragment : Fragment() {

    lateinit var model: ContactBookViewModel

    private lateinit var contact: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val dbRepository = ServiceLocator.instance(requireContext()).getRepository()
                @Suppress("UNCHECKED_CAST")
                return ContactBookViewModel(dbRepository) as T
            }
        })[ContactBookViewModel::class.java]
        contact = arguments?.get(CONTACT_ID_KEY) as String
    }

    private fun updateUi(it: ContactDetails) {
        contactId.text = contact
        stagingId.text = it.contacts.stagingId
        _context.text = it.accounts._context
        userId.text = it.accounts.userId
        status.text = it.accounts.status.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return LayoutInflater.from(inflater.context)
            .inflate(R.layout.contact_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getListOfContactDetails(contact).observe(this, Observer {
            updateUi(it)
        })
    }

    companion object {
        fun getInstance(contactId: String): ContactDetailsFragment {
            val fragment = ContactDetailsFragment()
            fragment.arguments = Bundle().apply {
                putString(CONTACT_ID_KEY, contactId)
            }
            return fragment
        }
    }
}