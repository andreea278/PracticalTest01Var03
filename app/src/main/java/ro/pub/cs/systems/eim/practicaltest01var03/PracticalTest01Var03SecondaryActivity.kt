package ro.pub.cs.systems.eim.practicaltest01var03

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01Var03SecondaryActivity: AppCompatActivity() {

    private lateinit var resultTextView: TextView

    private val buttonClickListener = ButtonClickListener()

    private inner class ButtonClickListener: View.OnClickListener{
        override fun onClick(view:View){
            when(view.id){
               R.id.correct_button -> {
                   setResult(RESULT_OK)
               }

               R.id.incorrect_button ->{
                   setResult(RESULT_CANCELED)
               }
            }
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_var03_secondary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        resultTextView = findViewById(R.id.result_exp_edit_text)
        val exp = intent.getStringExtra(Constants.RESULT_KEY)
        resultTextView.setText(exp)
        findViewById<Button>(R.id.correct_button).setOnClickListener(buttonClickListener)
        findViewById<Button>(R.id.incorrect_button).setOnClickListener(buttonClickListener)
    }
}