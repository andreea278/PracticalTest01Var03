package ro.pub.cs.systems.eim.practicaltest01var03


import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Date
import kotlin.random.Random

class ProcessingThread(
    private val context: Context,
    private val firstNumber: Int,
    private val secondNumber: Int
) : Thread() {

    private var sum = firstNumber + secondNumber
    private var diff = firstNumber - secondNumber

    @Volatile
    private var isRunning = false

    override fun run() {
        while (true) {
            sendMessage()
            sleepThread()
        }
    }

    private fun sleepThread() {
        try {
            sleep(Constants.SLEEP_TIME)

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun sendMessage() {
        val intent = Intent()
        intent.action= when(Random.nextInt(2)){
            0->Constants.ACTION_1
            else->Constants.ACTION_2
        }
        intent.putExtra(Constants.DATA_KEY,"${Date()} | $sum | $diff")
        intent.setPackage("ro.pub.cs.systems.eim.lab05.startedserviceactivity")
        context.sendBroadcast(intent)
        Log.d(Constants.TAG, "Broadcast sent: ${intent.action}")
    }

    fun stopThread(){
        isRunning = false
        interrupt()
    }
}