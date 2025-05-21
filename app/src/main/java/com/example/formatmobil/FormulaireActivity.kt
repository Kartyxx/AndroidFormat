package com.example.formatmobil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class FormulaireActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FormationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulaire)

        recyclerView = findViewById(R.id.recyclerViewFormations)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchFormations()
    }

    private fun fetchFormations() {
        val url = "https://orangered-spider-446441.hostingersite.com/FormationApi.php"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@FormulaireActivity, "Erreur réseau", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@FormulaireActivity, "Erreur serveur", Toast.LENGTH_SHORT).show()
                        }
                        return
                    }

                    val body = it.body?.string()
                    if (body.isNullOrEmpty()) {
                        runOnUiThread {
                            Toast.makeText(this@FormulaireActivity, "Réponse vide", Toast.LENGTH_SHORT).show()
                        }
                        return
                    }

                    try {
                        val jsonArray = JSONArray(body)
                        val formations = mutableListOf<Formation>()

                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)
                            val formation = Formation(
                                id_formation = item.getString("id_formation"),
                                titre = item.getString("titre"),
                                description = item.getString("description"),
                                cout = item.getString("cout"),
                                nombre_max_participants = item.getString("nombre_max_participants"),
                                lieu = item.getString("lieu"),
                                public_concerne = item.getString("public_concerne"),
                                objectifs = item.getString("objectifs"),
                                contenu = item.getString("contenu")
                            )
                            formations.add(formation)
                        }

                        runOnUiThread {
                            adapter = FormationAdapter(formations)
                            recyclerView.adapter = adapter
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@FormulaireActivity, "Erreur parsing JSON", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }
}
