package com.example.algoritmica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.algoritmica.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val englishDictionary = HashSet<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadEnglishDictionary()
        binding.corrector.setOnClickListener {


            val texto :String = binding.textoEdit.text.toString()
            val words = texto.split(Regex("\\s+|(?=[.,])|(?<=[.,])"))
            val correctedText = mutableListOf<String>()
            if(texto.isEmpty()){
                Toast.makeText(this,"Introduzca texto",Toast.LENGTH_SHORT).show()
            }
            else{
                for (word in words) {

                    var minDistance = Int.MAX_VALUE
                    var closestWord = word
                    if(word.matches(Regex(".*[a-zA-Z].*")) && checkSpelling(word)){
                        for (dictWord in englishDictionary) {
                            val distance = editDistance(word.toLowerCase(), dictWord)
                            if (distance < minDistance) {
                                minDistance = distance
                                closestWord = dictWord
                            }
                        }
                    }


                    correctedText.add(closestWord)



                }
                println(correctedText)
            }

        }


    }

    private fun editDistance(s1: String, s2: String): Int {
        val m = s1.length
        val n = s2.length
        val dp = Array(m + 1) { IntArray(n + 1) }

        for (i in 0..m) {
            for (j in 0..n) {
                if (i == 0) {
                    dp[i][j] = j
                } else if (j == 0) {
                    dp[i][j] = i
                } else if (s1[i - 1] == s2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1]
                } else {
                    dp[i][j] = 1 + min(dp[i][j - 1], min(dp[i - 1][j], dp[i - 1][j - 1]))
                }
            }
        }
        return dp[m][n]
    }

    private fun loadEnglishDictionary() {
        try {
            val inputStream = resources.openRawResource(R.raw.english_dictionary)
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.useLines { lines -> lines.forEach { englishDictionary.add(it) } }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkSpelling(word: String) : Boolean{
        return !englishDictionary.contains(word.toLowerCase())
    }
}