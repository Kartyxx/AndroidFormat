package com.example.formatmobil

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.net.URLEncoder
import at.favre.lib.crypto.bcrypt.BCrypt

class LoginActivitytest : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logintest)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val encodedEmail = URLEncoder.encode(email, "UTF-8")
            val url = "https://orangered-spider-446441.hostingersite.com/Userapi?email=$encodedEmail"

            val request = Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        resultTextView.text = "Erreur réseau : ${e.message}"
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!it.isSuccessful) {
                            runOnUiThread {
                                resultTextView.text = "Erreur serveur : ${response.code}"
                            }
                            return
                        }

                        val body = it.body?.string()
                        if (body == null) {
                            runOnUiThread {
                                resultTextView.text = "Réponse vide du serveur"
                            }
                            return
                        }

                        try {
                            val jsonArray = JSONArray(body)
                            val user = jsonArray.getJSONObject(0)

                            val nom = user.getString("nom")
                            val prenom = user.getString("prenom")
                            val statut = user.getString("status")
                            val ville = user.getString("ville")

                            val storedHashedPassword = user.getString("mot_de_passe")

                            // Vérification bcrypt
                            val result = BCrypt.verifyer().verify(password.toCharArray(), storedHashedPassword)

                            val message = """
                                Nom : $nom
                                Prénom : $prenom
                                Statut : $statut
                                Ville : $ville
                                
                                Mot de passe stocké (bcrypt) : $storedHashedPassword
                                Mot de passe saisi correspond : ${result.verified}
                            """.trimIndent()

                            runOnUiThread {
                                resultTextView.text = message
                            }

                        } catch (e: Exception) {
                            runOnUiThread {
                                resultTextView.text = "Erreur parsing JSON : ${e.message}"
                            }
                        }
                    }
                }
            })
        }
    }
}
