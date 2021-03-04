package se.ju.student.group16.androidproject

data class User(
        val uid: String,
        val displayname: String,
        val email: String
){
    override fun toString() = displayname
}
