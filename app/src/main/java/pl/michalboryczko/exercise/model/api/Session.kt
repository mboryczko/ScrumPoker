package pl.michalboryczko.exercise.model.api

import java.io.Serializable


data class Session(
        val sessionId: String = "",
        val managerId: String = "",
        val name: String = "",
        val password: String = "",
        //todo to be removed
        val currentStory: String? = null,
        val options: List<String>? = null
): Serializable

data class Story(
        val storyId: String,
        val sessionId: String,
        val story: String,
        val description: String,
        val estimations: HashMap<String, Estimation>?
){
    //needed for firestore mapping
    constructor(): this("", "", "", "",  null)
}

data class Estimation(
        val storyId: String,
        val points: String,
        val username: String,
        val userId: String
){
    constructor(): this("", "",  "", "")
}
