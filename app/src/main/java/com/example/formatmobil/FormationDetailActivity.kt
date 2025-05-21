package com.example.formatmobil

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FormationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formation_detail)

        val titre = intent.getStringExtra("titre")
        val description = intent.getStringExtra("description")
        val cout = intent.getStringExtra("cout")
        val participants = intent.getStringExtra("nombre_max_participants")
        val lieu = intent.getStringExtra("lieu")
        val publicConcerne = intent.getStringExtra("public_concerne")
        val objectifs = intent.getStringExtra("objectifs")
        val contenu = intent.getStringExtra("contenu")

        findViewById<TextView>(R.id.titreTextView).text = titre
        findViewById<TextView>(R.id.descriptionTextView).text = "Description : $description"
        findViewById<TextView>(R.id.coutTextView).text = "Coût : $cout €"
        findViewById<TextView>(R.id.participantsTextView).text = "Max participants : $participants"
        findViewById<TextView>(R.id.lieuTextView).text = "Lieu : $lieu"
        findViewById<TextView>(R.id.publicConcerneTextView).text = "Public concerné : $publicConcerne"
        findViewById<TextView>(R.id.objectifsTextView).text = "Objectifs : $objectifs"
        findViewById<TextView>(R.id.contenuTextView).text = "Contenu : $contenu"
    }
}
