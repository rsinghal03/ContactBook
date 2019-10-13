package com.example.contactbook.data

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.contactbook.data.database.Contacts
import com.example.contactbook.data.database.ContactsDatabase
import com.example.contactbook.data.database.MockDataProvider
import com.example.contactbook.data.model.ContactDetails
import java.util.concurrent.Executors

class DbRepository(context: Context) {

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
        val mediatorLiveData = MediatorLiveData<List<String>>()
        var list = MutableLiveData<List<String>>()
        diskIoExecutor.execute {
            db.runInTransaction {
              list = db.getContactsDao().getContactId() as MutableLiveData<List<String>>
            }
        }
//            mediatorLiveData.addSource(list, Observer {
//                mediatorLiveData.postValue(it)
//            })

        return list
    }

    fun getDetailsOfContactId(contactId: String): LiveData<ContactDetails> {
        val mediatorLiveData = MediatorLiveData<ContactDetails>()
        diskIoExecutor.execute {
            val contactDetails = db.getContactsDao().getContactIdDetails(contactId)
            mediatorLiveData.addSource(contactDetails, Observer {
                mediatorLiveData.postValue(it)
            })
        }
        return mediatorLiveData
    }
}