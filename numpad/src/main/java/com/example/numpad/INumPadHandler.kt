package com.example.numpad

interface INumPadHandler {
    fun onNumClick(num: Int)
    fun onLeftIconClick() {}
    fun onRightClick() {}
}