package com.example.flavourfolio.tabs.steps

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException


object ImageRetriever {
    fun retrieveImageLink(str: String): String {
        var link: String? = null
        val agent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"
        val default = "https://t3.ftcdn.net/jpg/04/34/72/82/240_F_434728286_OWQQvAFoXZLdGHlObozsolNeuSxhpr84.jpg"
        val item = str.replace(" ", "%20")

        try {
            val url = ("https://www.istockphoto.com/search/2/image?mediatype=photography&phrase=$item")
            val doc: Document = Jsoup.connect(url).userAgent(agent).get()
            val element = doc.select("img")[3]
            link = element?.attr("src")

        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        if (link.isNullOrEmpty()) {
            return default
        }
        return link
    }
}