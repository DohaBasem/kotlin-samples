package de.swirtz.kotlin.coroutines

import kotlinx.coroutines.experimental.*
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.ZoneId
import java.util.concurrent.atomic.AtomicInteger


suspend fun getReceiverAddressFromDatabase(): String {
    delay(1000)
    return "coroutine@kotlin.org"
}

suspend fun sendEmail(r: String, msg: String): Boolean {
    delay(2000)
    println("Sent '$msg' to $r")
    return true
}

suspend fun sendEmailSuspending(): Boolean {
    val msg = async(CommonPool) {
        delay(500)
        "The message content"
    }
    val recipient = async(CommonPool) { getReceiverAddressFromDatabase() }
    println("Waiting for email data")

    val sendStatus = async(CommonPool) {
        sendEmail(recipient.await(), msg.await())
    }
    return sendStatus.await()
}

fun main(args: Array<String>) = runBlocking(CommonPool) {
    val job = launch(CommonPool) {
        sendEmailSuspending()
        println("Email sent successfully.")
    }
    job.join()
    println("Finished")

}


