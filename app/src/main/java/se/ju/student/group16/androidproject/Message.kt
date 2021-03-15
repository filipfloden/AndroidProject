package se.ju.student.group16.androidproject

data class Message(
    val user: String,
    val message: String,
    val timestamp: Long
){
    override fun toString() = message
}