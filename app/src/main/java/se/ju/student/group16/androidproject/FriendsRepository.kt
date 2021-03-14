package se.ju.student.group16.androidproject

val friendsRepository = FriendsRepository().apply{}

class FriendsRepository{
    private val friends = mutableListOf<User>()

    fun addUser(uid: String, displayName: String, email: String){
        /*
        val id = when {
            friends.count() == 0 -> 1
            else -> friends.last()
        }
         */
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
    /*
    fun updateToDoById(id: Int, newTitle: String, newContent: String){
        getToDoById(id)?.run {
            title = newTitle
            content = newContent
        }
    }

     */
}