package com.example.tennis


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var textView: TextView
    private lateinit var signOutButton: Button
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reservation1Button: Button
    private lateinit var reservation2Button: Button
    private lateinit var infosButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        firebaseAuth = FirebaseAuth.getInstance()

        //on veut afficher le mail de l'utilisateur connecté
        val email = intent.getStringExtra("email")
        val textView = findViewById<TextView>(R.id.textView3)
        textView.text = "Welcome $email"

        firebaseUser = firebaseAuth.currentUser!!

        textView.text = "Bienvenue " + firebaseUser.email.toString()


        reservation1Button = findViewById(R.id.reservation1)
        reservation2Button = findViewById(R.id.reservation2)



        reservation1Button.setOnClickListener {
            val intent = Intent(this, ReservationActivity::class.java)

            startActivity(intent)
        }
        reservation2Button.setOnClickListener {
            val intent = Intent(this, ReservationActivity2::class.java)
            startActivity(intent)
        }

        infosButton = findViewById(R.id.infos)
        infosButton.setOnClickListener {
            val intent = Intent(this, InfosActivity::class.java)
            startActivity(intent)
        }


        //bouton de déconnexion puis redirection vers activitée de debut
        signOutButton = findViewById(R.id.LogOut)
        signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}