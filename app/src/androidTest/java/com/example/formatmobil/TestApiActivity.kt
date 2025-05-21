package com.example.formatmobil

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class TestApiActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_api)

        val buttonFetch = findViewById<Button>(R.id.buttonFetch)
        val textViewResult = findViewById<TextView>(R.id.textViewResult)

        buttonFetch.setOnClickListener {
            fetchUserData(textViewResult)
        }
    }

    private fun fetchUserData(textView: TextView) {
        val url = "https://orangered-spider-446441.hostingersite.com/Userapi?email=A.St%C3%A9phanie@gmail.com"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@TestApiActivity, "Erreur réseau: ${e.message}", Toast.LENGTH_SHORT).show()
                    textView.text = "Erreur réseau: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@TestApiActivity, "Erreur serveur: ${it.code}", Toast.LENGTH_SHORT).show()
                            textView.text = "Erreur serveur: ${it.code}"
                        }
                        return
                    }

                    val body = it.body?.string()

                    if (body == null) {
                        runOnUiThread {
                            Toast.makeText(this@TestApiActivity, "Réponse vide", Toast.LENGTH_SHORT).show()
                            textView.text = "Réponse vide"
                        }
                        return
                    }

                    try {
                        // On suppose que la réponse est un tableau JSON
                        val jsonArray = JSONArray(body)

                        // Pour afficher proprement, on itère sur chaque objet
                        val sb = StringBuilder()
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            sb.append("Utilisateur #${i+1}:\n")
                            sb.append("ID Utilisateur: ${obj.getString("id_utilisateur")}\n")
                            sb.append("Nom: ${obj.getString("nom")}\n")
                            sb.append("Prénom: ${obj.getString("prenom")}\n")
                            sb.append("Email: ${obj.getString("email")}\n")
                            sb.append("Status: ${obj.getString("status")}\n")
                            sb.append("Localisation: ${obj.getString("localisation")}\n")
                            sb.append("Code Postal: ${obj.getString("codeP")}\n")
                            sb.append("Ville: ${obj.getString("ville")}\n")
                            sb.append("Fonction: ${obj.getString("fonction")}\n")
                            sb.append("\n----------------------\n\n")
                        }

                        runOnUiThread {
                            textView.text = sb.toString()
                        }

                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@TestApiActivity, "Erreur JSON: ${e.message}", Toast.LENGTH_SHORT).show()
                            textView.text = "Erreur JSON: ${e.message}"
                        }
                    }
                }
            }
        })
    }
}
