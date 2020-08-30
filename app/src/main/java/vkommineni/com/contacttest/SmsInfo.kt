package vkommineni.com.contacttest

data class SmsInfo(
    val smsId: String,
    val address: String,
    val smsBody: String,
    val date: String,
    val threadId: String,
    val folder: String )