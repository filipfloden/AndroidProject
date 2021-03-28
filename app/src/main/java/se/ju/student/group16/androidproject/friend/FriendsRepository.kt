package se.ju.student.group16.androidproject.friend

import se.ju.student.group16.androidproject.User

val friendsRepository = FriendsRepository().apply{}

class FriendsRepository{
    private val friends = mutableListOf<User>()

    fun addUser(uid: String, displayName: String, email: String){
        friends.add(
                User(
                        uid,
                        displayName,
                        email
                )
        )
    }
    fun getAllFriends() = friends

    fun getFriendById(uid: String) =
            friends.find {
                it.uid == uid
            }
    fun deleteFriendById(id: String) =
            friends.remove(
                    friends.find{
                        it.uid == id
                    }
            )
    fun clearFriends(){
        friends.clear()
    }
}