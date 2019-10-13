package com.example.contactbook.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contactbook.data.DbRepository
import com.example.contactbook.data.model.ContactDetails

class ContactBookViewModel(private val dbRepository: DbRepository) : ViewModel() {


    fun insertData() {
        dbRepository.insertData()
    }

    fun getListOfContact(): LiveData<List<String>> {
        return dbRepository.getListOfContactId()
    }

    fun getListOfContactDetails(contactId: String): LiveData<ContactDetails> {
        return dbRepository.getDetailsOfContactId(contactId)
    }
}