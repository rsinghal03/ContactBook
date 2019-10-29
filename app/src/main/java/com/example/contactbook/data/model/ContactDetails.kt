package com.example.contactbook.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.contactbook.data.database.Accounts
import com.example.contactbook.data.database.Contacts
import com.example.contactbook.data.database.Extensions

data class ContactDetails(
    @Embedded val contacts: Contacts,
    @Relation(associateBy = Junction(Extensions::class),
        parentColumn = "_id",
        entityColumn = "_context",
        entity = Accounts::class)
   val accounts: Accounts
)