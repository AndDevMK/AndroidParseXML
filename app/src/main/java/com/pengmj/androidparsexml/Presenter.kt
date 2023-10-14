package com.pengmj.androidparsexml

import android.util.Log
import android.view.View

/**
 * @author MinKin
 * @date 2023/5/8
 * @desc
 */
class Presenter {

    companion object {
        private val tag: String = Presenter::class.java.simpleName
    }

    fun onParseXML(view: View) {
        val inputStream = view.context.assets.open("animal.xml")
        val list = AnimalXMLParser().parse(inputStream)
        Log.e(tag, list.toString())
    }

}