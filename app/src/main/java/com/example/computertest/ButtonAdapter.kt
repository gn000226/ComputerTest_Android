package com.example.computertest

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

class ButtonAdapter(val context: Context): RecyclerView.Adapter<ButtonAdapter.ViewHolder>() {

    private lateinit var onPress : onPressListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.button_item, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = 20

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.visibility = View.GONE
        when (position) {
            0 -> {                         // 清除
                holder.textView.text = "清除"
                holder.textView.setTextColor(Color.parseColor("#FFf28500"))
            }
            1 -> {                        // 去位數
                holder.imageView.visibility = View.VISIBLE
            }
            2 -> {                        // 百分比
                holder.textView.text = "%"
                holder.textView.setTextColor(Color.parseColor("#FFf28500"))
            }
            3 -> {                         // 除
                holder.textView.text = "/"
                holder.textView.setTextColor(Color.parseColor("#FFf28500"))
            }
            4 -> {
                holder.textView.text = "7"
            }
            5 -> {
                holder.textView.text = "8"
            }
            6 -> {
                holder.textView.text = "9"
            }
            7 -> {                          // 乘
                holder.textView.text = "X"
                holder.textView.setTextColor(Color.parseColor("#FFf28500"))
            }
            8 -> {
                holder.textView.text = "4"
            }
            9 -> {
                holder.textView.text = "5"
            }
            10 -> {
                holder.textView.text = "6"
            }
            11 -> {                         // 減
                holder.textView.text = "-"
                holder.textView.setTextColor(Color.parseColor("#FFf28500"))
            }
            12 -> {
                holder.textView.text = "1"
            }
            13 -> {
                holder.textView.text = "2"
            }
            14 -> {
                holder.textView.text = "3"
            }
            15 -> {                         // 加
                holder.textView.text = "+"
                holder.textView.setTextColor(Color.parseColor("#FFf28500"))
            }
            16 -> {
                holder.textView.text = ""
            }
            17 -> {
                holder.textView.text = "0"
            }
            18 -> {                         // 小數點
                holder.textView.text = "."
            }
            19 -> {                         // 等於
                holder.textView.text = "="
                holder.textView.setTextColor(Color.parseColor("#FFf28500"))
            }
        }

        /* 執行點擊 */
        holder.itemView.setOnClickListener {
            onPress.onItemSelected(holder.adapterPosition)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textView : AppCompatTextView = v.findViewById(R.id.buttonTextView)
        var imageView : AppCompatImageView = v.findViewById(R.id.imageView)
    }

    fun setItemSeleted(onPressListener:onPressListener) {
        this.onPress = onPressListener
    }
}

interface onPressListener {
    fun onItemSelected(position : Int)
}