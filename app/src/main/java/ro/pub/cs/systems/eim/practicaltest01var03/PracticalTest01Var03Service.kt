package ro.pub.cs.systems.eim.practicaltest01var03

import android.app.Service
import android.content.Intent
import android.os.IBinder

class PracticalTest01Var03Service: Service() {

    private var thread: ProcessingThread?=null

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val first = intent?.getIntExtra(Constants.FIRST_NUMBER, 0) ?: 0
        val second = intent?.getIntExtra(Constants.SECOND_NUMBER, 0) ?: 0

        thread?.stopThread()
        thread = ProcessingThread(this, first, second)
        thread?.start()
        return START_REDELIVER_INTENT
    }

    override fun onDestroy(){
        thread?.stopThread()
        super.onDestroy()
    }
}
