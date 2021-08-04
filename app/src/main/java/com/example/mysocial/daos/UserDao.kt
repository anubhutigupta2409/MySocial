package com.example.mysocial.daos

import com.example.mysocial.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    //function to add user, and make it a background thread

    fun addUser(user : User?){
        //if it is not null

        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                usersCollection.document(user.uid).set(it)
            }
        }
    }

    fun getUserById(uId : String) : Task<DocumentSnapshot> {

        return usersCollection.document(uId).get()//the get function gives us a task
    }
}