package models

import java.io.Serializable

data class Contacts(var id: Long, var name: String, var phones: List<Phone>): Serializable