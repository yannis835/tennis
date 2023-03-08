package com.example.tennis


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TimePicker
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class ReservationActivity2 : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var saveDataButton: Button
    private lateinit var timePicker: TimePicker
    private lateinit var calendarView: CalendarView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        val database = Firebase.database.reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val email = currentUser.email
            val uid = currentUser.uid
            database.child("users").child(uid).child("email").setValue(email)
        }
        //recuperation des elements de la vue
        calendarView = findViewById(R.id.terrain2)
        timePicker = findViewById(R.id.timePicker)
        saveDataButton = findViewById(R.id.saveData)

        val calendar = Calendar.getInstance()
        calendarView.minDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        calendarView.maxDate = calendar.timeInMillis

        timePicker.setIs24HourView(true)

        saveDataButton.setOnClickListener {
            val date = calendarView.date
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(date))
            val hour = timePicker.hour
            val terrain = "terrain2"
            if (currentUser != null) {
                database.child("users").child(currentUser.uid).child("date").setValue(dateString)
                database.child("users").child(currentUser.uid).child("hour").setValue(hour)
                database.child("users").child(currentUser.uid).child("terrain").setValue(terrain)
            }
            Toast.makeText(this, "Terrain 2 Reserved", Toast.LENGTH_SHORT).show()
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}