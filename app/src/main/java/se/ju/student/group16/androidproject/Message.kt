package se.ju.student.group16.androidproject

data class Message(
    val user: String,
    val message: String
){
    override fun toString() = message
}