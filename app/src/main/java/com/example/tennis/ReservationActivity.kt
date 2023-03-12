package com.example.tennis

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class ReservationActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var saveDataButton: Button
    private lateinit var timePicker1: TimePicker
    private lateinit var calendarView1: CalendarView
    private lateinit var radioGroup: RadioGroup



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        val database = Firebase.database.reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val database2 = Firebase.database
        val reservationsRef = database2.getReference("users")

        currentUser?.let {
            val email = currentUser.email
            val uid = currentUser.uid
            database.child("users").child(uid).child("email").setValue(email)
        }

        //recuperation des elements de la vue
        calendarView1 = findViewById(R.id.terrain1)
        timePicker1 = findViewById(R.id.timePicker1)
        saveDataButton = findViewById(R.id.saveData)


        //definition des limites du calendrier
        val calendar = Calendar.getInstance()
        calendarView1.minDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        calendarView1.maxDate = calendar.timeInMillis

        // Définition des limites d'heure pour le TimePicker
        timePicker1.setIs24HourView(true)
        timePicker1.hour = 22
        timePicker1.minute = 0
        timePicker1.setOnTimeChangedListener { _, hourOfDay, _ ->
            if (hourOfDay < 7) {
                timePicker1.hour = 7
                timePicker1.minute = 0
            } else if (hourOfDay > 22) {
                timePicker1.hour = 22
                timePicker1.minute = 0
            }
        }
        var selectedDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendarView1.minDate)
        Log.d("date", selectedDate)
        calendarView1.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val textView4 = findViewById<TextView>(R.id.textView4)
            textView4.setText("")
            // Mise à jour de la date dans le calendrier
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            selectedDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            Log.d("date", selectedDate)
            val query = reservationsRef.orderByChild("date").equalTo("$selectedDate")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Récupération du TextView par son ID


                    val reservations =
                        snapshot.children.mapNotNull { it.getValue(Resa::class.java) }
                    val heuresReservees = reservations.map { it.hour }
                    val dureeReservee = reservations.map { it.duration }
                    val terrainNum = reservations.map { it.terrain }

                    for (i in 0 until heuresReservees.size) {
                        if (terrainNum[i] == "terrain1") {
                            val heureEtDuree = "${heuresReservees[i]} - ${
                                dureeReservee[i]?.let {
                                    heuresReservees[i]?.plus(it)
                                }
                            }"
                            // Ajout d'un texte à la fin du contenu existant du TextView
                            val contenuExistant = textView4.text.toString()
                            val nouveauContenu = "$contenuExistant\r\n $heureEtDuree"
                            textView4.setText(nouveauContenu)


                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Gestion de l'erreur
                }
            })

        }


        saveDataButton.setOnClickListener {
            // Vérification de la date sélectionnée


            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val hour = timePicker1.hour
            val terrain = "terrain1"


            val query = reservationsRef.orderByChild("date").equalTo("$selectedDate")

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val reservations =
                        snapshot.children.mapNotNull { it.getValue(Resa::class.java) }
                    val heuresReservees = reservations.map { it.hour }
                    val dureeReservee = reservations.map { it.duration }
                    val terrainNum = reservations.map{it.terrain}
                    var ok = false
                    var pb = false
                    val size=heuresReservees.size
                    for (i in 0 until size) {
                        if (terrainNum[i]=="terrain1") {

                            Log.d("Click", "heure reserve ${heuresReservees[i]}")
                            Log.d("Click", "duree ${dureeReservee[i]}")
                            Log.d("Click", "hour $hour")
                            val total = (heuresReservees[i] ?: 0) + (dureeReservee[i] ?: 0)
                            Log.d("Click", "total $total")

                            if (hour >= (heuresReservees[i] ?: 0) && hour < total) {
                                ok = true
                            }else if(hour + 1 == heuresReservees[i]){ pb= true}

                        }

                    }

                    if (dayOfWeek == Calendar.SATURDAY && hour >= 10 && hour < 18) {
                        Toast.makeText(
                            this@ReservationActivity,
                            "Réservation impossible le samedi entre 10h et 18h",
                            Toast.LENGTH_LONG
                        ).show()
                        intent = Intent(
                            this@ReservationActivity,
                            ReservationActivity::class.java
                        )
                        startActivity(intent)
                    } else if (hour >= 22) {
                        Toast.makeText(
                            this@ReservationActivity,
                            "Le club ferme à 22h, veuillez sélectionner une heure avant 22h",
                            Toast.LENGTH_LONG
                        ).show()
                        intent = Intent(
                            this@ReservationActivity,
                            ReservationActivity::class.java
                        )
                        startActivity(intent)
                    } else if (ok) {
                        Toast.makeText(
                            this@ReservationActivity,
                            "Ces horaires sont déja réserver merci d'en choisir d'autre",
                            Toast.LENGTH_LONG
                        ).show()
                        intent = Intent(
                            this@ReservationActivity,
                            ReservationActivity::class.java
                        )
                        startActivity(intent)
                    } else {
                        if (currentUser != null) {

                            if (hour >= 21 || dayOfWeek == Calendar.SATURDAY && hour >= 9 || pb) {
                                AlertDialog.Builder(this@ReservationActivity)
                                    .setTitle("Réservation")
                                    .setMessage("Voulez-vous réserver pour 1h ou 2h ?")
                                    .setPositiveButton("1h") { _, _ ->
                                        Toast.makeText(
                                            this@ReservationActivity,
                                            "Terrain 1 réservé pour 1 heure",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        database.child("users").child(currentUser.uid).child("date")
                                            .setValue(selectedDate)
                                        database.child("users").child(currentUser.uid).child("hour")
                                            .setValue(hour)
                                        database.child("users").child(currentUser.uid).child("terrain")
                                            .setValue(terrain)
                                        database.child("users").child(currentUser.uid)
                                            .child("duration").setValue(1)
                                        intent = Intent(
                                            this@ReservationActivity,
                                           ReservationActivity2::class.java
                                        )
                                        startActivity(intent)
                                    }
                                    .setNegativeButton("2h") { _, _ ->
                                        Toast.makeText(
                                            this@ReservationActivity,
                                            "Réservation impossible pour 2h",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        intent = Intent(
                                            this@ReservationActivity,
                                            ReservationActivity::class.java
                                        )
                                        startActivity(intent)
                                    }
                                    .show()
                            } else {
                                val hourOptions = arrayOf("1h", "2h")
                                AlertDialog.Builder(this@ReservationActivity)
                                    .setTitle("Réservation")
                                    .setItems(hourOptions) { _, index ->
                                        val selectedHour = index + 1
                                        Log.d(
                                            "selectedHour",
                                            selectedHour.toString()
                                        ) // afficher la valeur de selectedHour dans les logs
                                        database.child("users").child(currentUser.uid)
                                            .child("duration").setValue(selectedHour)
                                        database.child("users").child(currentUser.uid).child("date")
                                            .setValue(selectedDate)
                                        database.child("users").child(currentUser.uid).child("hour")
                                            .setValue(hour)
                                        database.child("users").child(currentUser.uid).child("terrain")
                                            .setValue(terrain)
                                        Toast.makeText(
                                            this@ReservationActivity,
                                            "Terrain 1 réservé pour $selectedHour heure(s)",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        intent = Intent(
                                            this@ReservationActivity,
                                            HomeActivity::class.java
                                        )
                                        startActivity(intent)
                                    }
                                    .show()
                            }
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Gestion de l'erreur
                    Log.d("Click","errreur" )
                }

            })


        }
    }
}