package com.example.snakegame.presentation.utility

object ClickDebouncer {
    private var lastClickTime = 0L
    fun canClick(debounceMillis: Long = 350): Boolean {
        val current = System.currentTimeMillis()
        return if (current - lastClickTime >= debounceMillis) {
            lastClickTime = current
            true
        } else false
    }
}