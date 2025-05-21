package com.example.formatmobil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Call
import okhttp3.Response

import org.json.JSONArray
import java.io.IOException

// Import de la librairie bcrypt
import at.favre.lib.crypto.bcrypt.BCrypt

class LoginActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        val url = "https://orangered-spider-446441.hostingersite.com/Userapi?email=$email"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Erreur réseau", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Erreur serveur", Toast.LENGTH_SHORT).show()
                        }
                        return
                    }

                    val body = it.body?.string()
                    if (body.isNullOrEmpty()) {
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Réponse vide", Toast.LENGTH_SHORT).show()
                        }
                        return
                    }

                    try {
                        val jsonArray = JSONArray(body)
                        val json = jsonArray.getJSONObject(0)
                        val storedHashedPassword = json.getString("mot_de_passe") // Hash bcrypt stocké

                        // Vérification bcrypt du mot de passe saisi
                        val result = BCrypt.verifyer().verify(password.toCharArray(), storedHashedPassword)

                        runOnUiThread {
                            if (result.verified) {
                                // Mot de passe correct -> go FormulaireActivity
                                val intent = Intent(this@LoginActivity, FormulaireActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "Mot de passe incorrect", Toast.LENGTH_SHORT).show()
                            }
                        }

                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Erreur lors de la lecture des données", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }
}
