package com.github.kr328.ibr.model

import android.graphics.drawable.Drawable

data class AppListElement(val enable: Boolean, val packageName: String, val name: String, val ruleCount: Int, val icon: Drawable) {
    fun equalsBase(other: AppListElement) =
            packageName == other.packageName && enable == other.enable && name == other.name && ruleCount == other.ruleCount
}
