package com.github.kr328.ibr.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.github.kr328.ibr.R

class SettingButton @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defaultStyleAttr: Int = 0, defaultStyleRes: Int = 0) :
        FrameLayout(context, attributeSet, defaultStyleAttr, defaultStyleRes) {
    var title: CharSequence
        get() = titleView.text
        set(value) {
            titleView.text = value
        }
    var summary: CharSequence
        get() = summaryView.text
        set(value) {
            summaryView.text = value
            if (value.isEmpty())
                summaryView.visibility = View.GONE
        }
    var icon: Drawable?
        get() {
            throw IllegalArgumentException("Unsupported")
        }
        set(value) {
            iconView.setImageDrawable(value)
        }

    override fun setOnClickListener(l: OnClickListener?) = clickable.setOnClickListener(l)

    private val titleView: TextView
    private val summaryView: TextView
    private val iconView: ImageView
    private val clickable: View
    private val root: View = LayoutInflater.from(context).inflate(R.layout.view_settings_button, this, true)

    init {
        clickable = root.findViewById(R.id.view_settings_button_clickable)
        titleView = root.findViewById(R.id.view_settings_button_title)
        summaryView = root.findViewById(R.id.view_settings_button_summary)
        iconView = root.findViewById(R.id.view_settings_button_icon)

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.custom, defaultStyleAttr, defaultStyleRes).apply {
            title = getString(R.styleable.custom_title) ?: ""
            summary = getString(R.styleable.custom_summary) ?: ""
            icon = getDrawable(R.styleable.custom_icon)
        }
    }
}