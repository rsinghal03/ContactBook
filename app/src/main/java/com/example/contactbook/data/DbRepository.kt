package com.example.contactbook.data

import android.app.Activity
import android.content.Context
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.contactbook.data.database.Contacts
import com.example.contactbook.data.database.ContactsDatabase
import com.example.contactbook.data.database.MockDataProvider
import com.example.contactbook.data.model.ContactDetails
import java.util.concurrent.Executors
import java.util.logging.Handler

class DbRepository(val context: Context) {

    private val diskIoExecutor = Executors.newSingleThreadExecutor()

    private val db by lazy {
        ContactsDatabase.invoke(context)
    }

    fun insertData() {
        diskIoExecutor.execute {
            db.runInTransaction {
                db.getContactsDao().insertContact(MockDataProvider.getListOfContacts())
                db.getContactsDao().insertExtension(MockDataProvider.getListOfExtensions())
                db.getContactsDao().insertAccount(MockDataProvider.getListOfAccounts())
            }
        }
    }

    fun getListOfContactId(): LiveData<List<String>> {
        val list = MutableLiveData<List<String>>()
        diskIoExecutor.execute {
            db.runInTransaction {
                val listOfContactId = db.getContactsDao().getContactId()
                list.postValue(listOfContactId)
            }
        }
        return list
    }

    fun getDetailsOfContactId(contactId: String): LiveData<ContactDetails> {
        val contactDetailLiveData = MutableLiveData<ContactDetails>()
        diskIoExecutor.execute {
            val contactDetails = db.getContactsDao().getContactIdDetails(contactId)
            contactDetailLiveData.postValue(contactDetails)
        }

        return contactDetailLiveData
    }
}