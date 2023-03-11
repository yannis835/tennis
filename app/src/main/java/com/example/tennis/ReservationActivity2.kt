package com.example.tennis


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class ReservationActivity2 : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var saveDataButton: Button
    private lateinit var timePicker2: TimePicker
    private lateinit var calendarView2: CalendarView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation2)

        val database = Firebase.database.reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val email = currentUser.email
            val uid = currentUser.uid
            database.child("users").child(uid).child("email").setValue(email)
        }
        //recuperation des elements de la vue
        calendarView2 = findViewById(R.id.terrain2)
        timePicker2 = findViewById(R.id.timePicker2)
        saveDataButton = findViewById(R.id.saveData)

        //definition des limites du calendrier
        val calendar = Calendar.getInstance()
        calendarView2.minDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        calendarView2.maxDate = calendar.timeInMillis

        // Définition des limites d'heure pour le TimePicker
        timePicker2.setIs24HourView(true)
        timePicker2.hour = 22
        timePicker2.minute = 0
        timePicker2.setOnTimeChangedListener { _, hourOfDay, _ ->
            if (hourOfDay < 7) {
                timePicker2.hour = 7
                timePicker2.minute = 0
            } else if (hourOfDay > 22) {
                timePicker2.hour = 22
                timePicker2.minute = 0
            }
        }

        calendarView2.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Mise à jour de la date dans le calendrier
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        }

        saveDataButton.setOnClickListener {
            // Vérification de la date sélectionnée
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val hour = timePicker2.hour
            val terrain = "terrain2"

            if (dayOfWeek == Calendar.SATURDAY && hour >= 10 && hour < 18) {
                Toast.makeText(
                    this,
                    "Réservation impossible le samedi entre 10h et 18h",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (hour >= 22) {
                Toast.makeText(
                    this,
                    "Le club ferme à 22h, veuillez sélectionner une heure avant 22h",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Récupération de la date sélectionnée sous string
                val selectedDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                Log.d("date", selectedDate)

                if (currentUser != null) {
                    database.child("users").child(currentUser.uid).child("date")
                        .setValue(selectedDate)
                    database.child("users").child(currentUser.uid).child("hour").setValue(hour)

                    database.child("users").child(currentUser.uid).child("terrain")
                        .setValue(terrain)

                    if (hour >= 21 || dayOfWeek == Calendar.SATURDAY && hour >= 9 ) {
                        AlertDialog.Builder(this)
                            .setTitle("Réservation")
                            .setMessage("Voulez-vous réserver pour 1h ou 2h ?")
                            .setPositiveButton("1h") { _, _ ->
                                Toast.makeText(
                                    this,
                                    "Terrain 2 réservé pour 1 heure",
                                    Toast.LENGTH_SHORT
                                ).show()
                                intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                            }
                            .setNegativeButton("2h") { _, _ ->
                                Toast.makeText(
                                    this,
                                    "Réservation impossible pour 2h après 21h",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .show()
                    } else {
                        val hourOptions = arrayOf("1h", "2h")
                        AlertDialog.Builder(this)
                            .setTitle("Réservation")
                            .setItems(hourOptions) { _, index ->
                                val selectedHour = index + 1
                                Log.d("selectedHour", selectedHour.toString()) // afficher la valeur de selectedHour dans les logs
                                database.child("users").child(currentUser.uid).child("duration").setValue(selectedHour)
                                Toast.makeText(
                                    this,
                                    "Terrain 2 réservé pour $selectedHour heure(s)",
                                    Toast.LENGTH_SHORT
                                ).show()
                                intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                            }
                            .show()
                    }
                }
            }
        }



    }
}