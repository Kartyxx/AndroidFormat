package com.example.formatmobil

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FormationAdapter(private val formations: List<Formation>) :
    RecyclerView.Adapter<FormationAdapter.FormationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_formation, parent, false)
        return FormationViewHolder(view)
    }

    override fun onBindViewHolder(holder: FormationViewHolder, position: Int) {
        val formation = formations[position]
        holder.bind(formation)

        // Clic pour ouvrir la page détail
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, FormationDetailActivity::class.java).apply {
                putExtra("titre", formation.titre)
                putExtra("description", formation.description)
                putExtra("cout", formation.cout)
                putExtra("nombre_max_participants", formation.nombre_max_participants)
                putExtra("lieu", formation.lieu)
                putExtra("public_concerne", formation.public_concerne)
                putExtra("objectifs", formation.objectifs)
                putExtra("contenu", formation.contenu)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = formations.size

    class FormationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titreTextView: TextView = itemView.findViewById(R.id.titreTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val coutTextView: TextView = itemView.findViewById(R.id.coutTextView)
        private val participantsTextView: TextView = itemView.findViewById(R.id.participantsTextView)
        private val lieuTextView: TextView = itemView.findViewById(R.id.lieuTextView)
        private val publicConcerneTextView: TextView = itemView.findViewById(R.id.publicConcerneTextView)
        private val objectifsTextView: TextView = itemView.findViewById(R.id.objectifsTextView)
        private val contenuTextView: TextView = itemView.findViewById(R.id.contenuTextView)

        fun bind(formation: Formation) {
            titreTextView.text = formation.titre
            descriptionTextView.text = formation.description
            coutTextView.text = "Coût: ${formation.cout} €"
            participantsTextView.text = "Max participants: ${formation.nombre_max_participants}"
            lieuTextView.text = "Lieu: ${formation.lieu}"
            publicConcerneTextView.text = "Public concerné: ${formation.public_concerne}"
            objectifsTextView.text = "Objectifs: ${formation.objectifs}"
            contenuTextView.text = "Contenu: ${formation.contenu}"
        }
    }
}
