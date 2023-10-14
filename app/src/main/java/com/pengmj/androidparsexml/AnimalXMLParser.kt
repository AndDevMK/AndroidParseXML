package com.pengmj.androidparsexml

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

/**
 * @author MinKin
 * @date 2023/5/8
 * @desc
 */
class AnimalXMLParser {

    // 不使用命名空间
    private val ns: String? = null

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<Dog> {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return readAnimal(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readAnimal(parser: XmlPullParser): List<Dog> {
        val dogs = mutableListOf<Dog>()
        parser.require(XmlPullParser.START_TAG, ns, "animal")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "dog") {
                dogs.add(readDog(parser))
            }
            else {
                skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "animal")
        return dogs
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readDog(parser: XmlPullParser): Dog {
        parser.require(XmlPullParser.START_TAG, ns, "dog")
        var name: String? = null
        var breed: String? = null
        var link: String? = null
        var profile: String? = null
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "name"    -> name = readName(parser)
                "breed"   -> breed = readBreed(parser)
                "link"    -> link = readLink(parser)
                "profile" -> profile = readProfile(parser)
                else      -> skip(parser)
            }
        }
        return Dog(name, breed, link, profile)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readProfile(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "profile")
        val profile = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "profile")
        return profile
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readLink(parser: XmlPullParser): String {
        var link = ""
        parser.require(XmlPullParser.START_TAG, ns, "link")
        val tag = parser.name
        val relType = parser.getAttributeValue(null, "rel")
        if (tag == "link") {
            if (relType == "alternate") {
                link = parser.getAttributeValue(null, "href")
                parser.nextTag()
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link")
        return link
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readBreed(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "breed")
        val breed = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "breed")
        return breed
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readName(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "name")
        val name = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "name")
        return name
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG   -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

}