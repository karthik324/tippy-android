package com.example.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var tpSeekBar: SeekBar
    private lateinit var tvPercentLabel: TextView
    private lateinit var tvTipText: TextView
    private lateinit var tvTotalText: TextView
    private lateinit var tvTipAction: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        tpSeekBar = findViewById(R.id.tpSeekBar)
        tvPercentLabel = findViewById(R.id.tvPercentLabel)
        tvTipText = findViewById(R.id.tvTipText)
        tvTotalText = findViewById(R.id.tvTotalText)
        tvTipAction = findViewById(R.id.tvAction)
        tpSeekBar.progress = INITIAL_TIP_PERCENT
        tvPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateAction(INITIAL_TIP_PERCENT)

        tpSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateAction(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i(
                    TAG,
                    "On text changed -> charsequence $s, start -> $start, before -> $before, count -> $count"
                )
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "After text changed $s")
                if (s != null) {
                    computeTipAndTotal()
                }
            }

        })
    }

    private fun updateAction(progress: Int) {
        val tipDescription = when (progress){
            in 0..9 -> "Fury"
            in 10..15 -> "Hmm. Nice"
            in 16..20 -> "Man of culture"
            in 21..30 -> "W"
            else -> "Elon Musk?"
        }
        tvTipAction.text = tipDescription

        val color = ArgbEvaluator().evaluate(
            progress.toFloat() / tpSeekBar.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this,R.color.color_best_tip)
        ) as Int
        tvTipAction.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if(etBaseAmount.text.isNotEmpty()){
            val baseAmount = etBaseAmount.text.toString().toDouble()
            val tipPercent = tpSeekBar.progress

            val tipAmount = baseAmount * tipPercent / 100
            val totalAmount = baseAmount + tipAmount

            tvTipText.text = "%.2f".format(tipAmount)
            tvTotalText.text = "%.2f".format(totalAmount)
        } else {
            tvTipText.text = ""
            tvTotalText.text = ""
        }
    }
}