package ro.pub.cs.systems.eim.practicaltest01var03

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.withStarted
import kotlin.toString

class PracticalTest01Var03MainActivity : AppCompatActivity() {
    private lateinit var firstNumberEditText: EditText
    private lateinit var secondNumberEditText: EditText
    private lateinit var resultTextView: TextView

    private val messageBroadcastReceiver = MessageBroadcastReceiver()

    private inner class MessageBroadcastReceiver: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val message = intent?.getStringExtra(Constants.DATA_KEY)

            Log.d(Constants.TAG, "${intent?.action}: $message")

        }
    }

    private val intentFilter = IntentFilter().apply{
        addAction(Constants.ACTION_1)
        addAction(Constants.ACTION_2)
    }

    private var serviceStarted = false

    private fun startServiceIfNeeded(first: Int, second: Int){
        if(!serviceStarted){
            val intent = Intent(this, PracticalTest01Var03Service::class.java)
            intent.putExtra(Constants.FIRST_NUMBER, first)
            intent.putExtra(Constants.SECOND_NUMBER, second)
            startService(intent)
            serviceStarted = true
        }
    }

    private val secondActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result->
        when(result.resultCode){
            Activity.RESULT_OK ->{
                Toast.makeText(this,"Correct ${result.data?.getStringExtra(Constants.RESULT_KEY)}",Toast.LENGTH_LONG).show()
            }

            Activity.RESULT_CANCELED ->{
                Toast.makeText(this,"Incorrect",Toast.LENGTH_LONG).show()
            }
        }
    }
    private val buttonClickListener = ButtonClickListener()

    private inner class ButtonClickListener: View.OnClickListener{
        override fun onClick(view:View){
            when(view.id){
                R.id.plus_button ->{
                    val firstNumber = firstNumberEditText.text.toString().toInt()
                    val secondNumber = secondNumberEditText.text.toString().toInt()
                    val result = firstNumber + secondNumber
                    resultTextView.text = result.toString()
                    startServiceIfNeeded(firstNumber, secondNumber)
                }
                R.id.minus_button ->{
                    val firstNumber = firstNumberEditText.text.toString().toInt()
                    val secondNumber = secondNumberEditText.text.toString().toInt()
                    val result = firstNumber - secondNumber
                    resultTextView.text = result.toString()
                    startServiceIfNeeded(firstNumber, secondNumber)
                }
                R.id.navigate_to_activity_button ->{
                    val intent = Intent(
                        this@PracticalTest01Var03MainActivity,
                        PracticalTest01Var03SecondaryActivity::class.java
                    )
                    intent.setPackage("ro.pub.cs.systems.eim.practicaltest02")
                    intent.putExtra(Constants.RESULT_KEY, firstNumberEditText.text.toString() + "+" + secondNumberEditText.text.toString()+ "=" + resultTextView.text.toString())
                    secondActivityLauncher.launch(intent)
                    }
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_var03_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firstNumberEditText = findViewById(R.id.first_number_edit_text)
        secondNumberEditText = findViewById(R.id.second_number_edit_text)
        resultTextView = findViewById(R.id.result_text_view)

        findViewById<Button>(R.id.plus_button).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.minus_button).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.navigate_to_activity_button).setOnClickListener(buttonClickListener)
    }

    override fun onSaveInstanceState(outState:Bundle){
        super.onSaveInstanceState(outState)
        outState.putString(Constants.FIRST_NUMBER, firstNumberEditText.text.toString())
        outState.putString(Constants.SECOND_NUMBER, secondNumberEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState:Bundle){
        super.onRestoreInstanceState(savedInstanceState)
//        firstNumberEditText.setText(savedInstanceState.getString(Constants.FIRST_NUMBER))
        val firstNumber = savedInstanceState.getString(Constants.FIRST_NUMBER)
//        secondNumberEditText.setText(savedInstanceState.getString(Constants.SECOND_NUMBER))
        val secondNumber = savedInstanceState.getString(Constants.SECOND_NUMBER)
        Toast.makeText(this@PracticalTest01Var03MainActivity, " $firstNumber | $secondNumber ", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy(){
        stopService(Intent(this, PracticalTest01Var03Service::class.java))
        super.onDestroy()
    }

    override fun onResume(){
        super.onResume()
        ContextCompat.registerReceiver(this, messageBroadcastReceiver, intentFilter,ContextCompat.RECEIVER_EXPORTED)
    }

    override fun onPause(){
        unregisterReceiver(messageBroadcastReceiver)
        super.onPause()
    }
}


