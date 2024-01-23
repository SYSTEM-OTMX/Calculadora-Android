package com.system_otmx.calculadora

import android.content.Context
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.system_otmx.calculadora.Adapters.AdaptadorHistorial
import com.system_otmx.calculadora.ModeloHistorial.ModeloHistorial
import com.system_otmx.calculadora.databinding.ActivityMainBinding
import kotlin.math.exp

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var expresion : String = ""
    var historial = Constantes.test_historial.size
    var resultado : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarHistorial(historial)
        binding.BtnSYSTEMOTMX.setOnClickListener {
            Toast.makeText(this, "HOLA", Toast.LENGTH_SHORT).show()
        }
        binding.Btn1.setOnClickListener {
            generarExpresion("1").toString()
        }
        binding.Btn2.setOnClickListener {
            generarExpresion("2").toString()
        }
        binding.Btn3.setOnClickListener {
            generarExpresion("3").toString()
        }
        binding.Btn4.setOnClickListener {
            generarExpresion("4").toString()
        }
        binding.Btn5.setOnClickListener {
            generarExpresion("5").toString()
        }
        binding.Btn6.setOnClickListener {
            generarExpresion("6").toString()
        }
        binding.Btn7.setOnClickListener {
            generarExpresion("7").toString()
        }
        binding.Btn8.setOnClickListener {
            generarExpresion("8").toString()
        }
        binding.Btn9.setOnClickListener {
            generarExpresion("9").toString()
        }
        binding.Btn0.setOnClickListener {
            generarExpresion("0").toString()
        }
        binding.BtnDivision.setOnClickListener {
            generarExpresion("/")
        }
        binding.BtnSumar.setOnClickListener {
            generarExpresion("+")
        }
        binding.BtnResta.setOnClickListener {
            generarExpresion("-")
        }
        binding.BtnMultiplicar.setOnClickListener {
            generarExpresion("*")
        }
        binding.BtnPorciento.setOnClickListener {
            generarExpresion("%")
        }
        binding.BtnResultado.setOnClickListener {
            binding.AMTVExpresion.text = resultado
            binding.AMTVResultado.text = ""
            expresion = resultado
        }
        binding.BtnClear.setOnClickListener {
            binding.AMRVHistorial.visibility = LinearLayout.GONE
            expresion = ""
            resultado = ""
            binding.AMTVResultado.text = ""
            binding.AMTVExpresion.text = ""
        }
        binding.BtnBack.setOnClickListener {
            expresion = removeLastChar(expresion)
            binding.AMTVExpresion.text = expresion
            generarExpresion("")
        }
        binding.BtnPoint.setOnClickListener {
            generarExpresion(".")
        }

    }

    private fun removeLastChar(str: String): String {
        return str.replaceFirst(".$".toRegex(), "")
    }
    private fun cargarHistorial(historial:Int){
        val historialArrayList = ArrayList<ModeloHistorial>()
        for(i in 0  until historial){
            val modeloHistorial = ModeloHistorial(Constantes.test_historial[i])
            historialArrayList.add(modeloHistorial)
        }
        val adaptadorHistorial = AdaptadorHistorial(
            this,
            historialArrayList,
            object  : RvListenerHistorial{
                override fun onHistorialClick(modeloHistorial: ModeloHistorial) {
                    super.onHistorialClick(modeloHistorial)
                }
            }
        )

        binding.AMRVHistorial.adapter = adaptadorHistorial
    }
    private fun generarExpresion(numero: String){
            expresion = expresion + numero
            if(numero=="+" || numero == "-" || numero == "*" || numero == "/" || numero == "%"){
                binding.AMTVExpresion.text = expresion
            }
            else{
                binding.AMTVExpresion.text = expresion
                resultado = eval(expresion).toString()
                binding.AMTVResultado.text = resultado
            }

    }

    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0
            fun nextChar() {
                ch = if (++pos < str.length) str[pos].toInt() else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.toInt()) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.toInt())) x += parseTerm() // addition
                    else if (eat('-'.toInt())) x -= parseTerm() // subtraction
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.toInt())) x *= parseFactor() // multiplication
                    else if (eat('/'.toInt())) x /= parseFactor() // division
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.toInt())) return parseFactor() // unary plus
                if (eat('-'.toInt())) return -parseFactor() // unary minus
                var x: Double
                val startPos = pos
                if (eat('('.toInt())) { // parentheses
                    x = parseExpression()
                    eat(')'.toInt())
                } else if (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) { // numbers
                    while (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else if (ch >= 'a'.toInt() && ch <= 'z'.toInt()) { // functions
                    while (ch >= 'a'.toInt() && ch <= 'z'.toInt()) nextChar()
                    val func = str.substring(startPos, pos)
                    x = parseFactor()
                    x = if (func == "sqrt") Math.sqrt(x) else if (func == "sin") Math.sin(Math.toRadians(x)) else if (func == "cos") Math.cos(Math.toRadians(x)) else if (func == "tan") Math.tan(Math.toRadians(x)) else throw RuntimeException("Unknown function: $func")
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                if (eat('^'.toInt())) x = Math.pow(x, parseFactor()) // exponentiation
                return x
            }
        }.parse()
    }
}