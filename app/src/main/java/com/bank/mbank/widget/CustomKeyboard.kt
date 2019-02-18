package com.bank.mbank.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout

import com.bank.mbank.R

class CustomKeyboard @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var button1: Button? = null
    private var button2: Button? = null
    private var button3: Button? = null
    private var button4: Button? = null
    private var button5: Button? = null
    private var button6: Button? = null
    private var button7: Button? = null
    private var button8: Button? = null
    private var button9: Button? = null
    private var button0: Button? = null
    private var buttonDelete: Button? = null

    private val keyValues = SparseArray<String>()
    private var inputConnection: InputConnection? = null

    init {
        init(context)
    }

    //Inflate view and set listeners to buttons
    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.custom_keyboard, this, true)
        button1 = findViewById(R.id.button_1)
        button1!!.setOnClickListener(this)

        button2 = findViewById(R.id.button_2)
        button2!!.setOnClickListener(this)

        button3 = findViewById(R.id.button_3)
        button3!!.setOnClickListener(this)

        button4 = findViewById(R.id.button_4)
        button4!!.setOnClickListener(this)

        button5 = findViewById(R.id.button_5)
        button5!!.setOnClickListener(this)

        button6 = findViewById(R.id.button_6)
        button6!!.setOnClickListener(this)

        button7 = findViewById(R.id.button_7)
        button7!!.setOnClickListener(this)

        button8 = findViewById(R.id.button_8)
        button8!!.setOnClickListener(this)

        button9 = findViewById(R.id.button_9)
        button9!!.setOnClickListener(this)

        button0 = findViewById(R.id.button_0)
        button0!!.setOnClickListener(this)

        buttonDelete = findViewById(R.id.button_delete)
        buttonDelete!!.setOnClickListener(this)

        //Assign each button with a value
        keyValues.put(R.id.button_1, "1")
        keyValues.put(R.id.button_2, "2")
        keyValues.put(R.id.button_3, "3")
        keyValues.put(R.id.button_4, "4")
        keyValues.put(R.id.button_5, "5")
        keyValues.put(R.id.button_6, "6")
        keyValues.put(R.id.button_7, "7")
        keyValues.put(R.id.button_8, "8")
        keyValues.put(R.id.button_9, "9")
        keyValues.put(R.id.button_0, "0")
    }

    override fun onClick(view: View) {
        if (inputConnection == null)
            return

        if (view.id == R.id.button_delete) {
            val selectedText = inputConnection!!.getSelectedText(0)

            //If text is not empty delete last character, else just set an empty string
            if (TextUtils.isEmpty(selectedText)) {
                inputConnection!!.deleteSurroundingText(1, 0)
            } else {
                inputConnection!!.commitText("", 1)
            }
        } else {
            //Get button value and append to text view
            val value = keyValues.get(view.id)
            inputConnection!!.commitText(value, 1)
        }
    }

    fun setInputConnection(ic: InputConnection) {
        inputConnection = ic
    }
}