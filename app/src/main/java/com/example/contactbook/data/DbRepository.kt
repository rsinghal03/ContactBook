package com.example.contactbook.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.contactbook.data.database.ContactsDatabase
import com.example.contactbook.data.database.MockDataProvider
import com.example.contactbook.data.model.ContactDetails
import java.util.concurrent.Executors

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
        return db.getContactsDao().getContactId()
    }

    fun getDetailsOfContactId(contactId: String): LiveData<ContactDetails> {
        return db.getContactsDao().getContactIdDetails(contactId)
    }
}