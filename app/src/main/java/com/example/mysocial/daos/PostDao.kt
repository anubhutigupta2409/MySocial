package com.example.mysocial.daos

import com.example.mysocial.models.Post
import com.example.mysocial.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    val auth = Firebase.auth

    fun addPost(text : String)
    {
        //double exclamation ensures that yes user exists and we are required to extract it's uid
        //and in case someone tries to add post without signing in, our app should crash

        /*
        * here, we wanted user's data, so we made a method in the user dao that gives us a user id, and returns a task
        * snapshot, but that snapshot just gives us an id, but we want full user object from it, so basically we will
        * use kotlin coroutine here, so that we can .await() for the task to extract the full user object from the id
        * */
        GlobalScope.launch {

            val currentUserId = auth.currentUser!!.uid
            val userDao = UserDao()
            val user = userDao.getUserById(currentUserId).await().toObject(User::class.java)!!//again to ensure that this is not null

            val currentTime = System.currentTimeMillis()
            val post = Post(text, user, currentTime )//liked by none as of now, so let it be
            postCollection.document().set(post)
        }

    }

    fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postCollection.document(postId).get()
    }

    fun updateLikes(postId: String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)

            if(isLiked) {
                post.likedBy.remove(currentUserId)
            } else {
                post.likedBy.add(currentUserId)
            }
            postCollection.document(postId).set(post)
        }

    }


}