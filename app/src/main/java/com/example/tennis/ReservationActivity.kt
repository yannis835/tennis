class ReservationActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var saveDataButton: Button
<<<<<<< Updated upstream
    private lateinit var timePicker: TimePicker
    private lateinit var calendarView: CalendarView
=======
    private lateinit var timePicker1: TimePicker
    private lateinit var calendarView1: CalendarView
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
        // Récupération des éléments de la vue
        calendarView = findViewById(R.id.terrain2)
        timePicker = findViewById(R.id.timePicker)
        saveDataButton = findViewById(R.id.saveData)

        // Définition des limites de date pour le CalendarView
=======
        //recuperation des elements de la vue
        calendarView1 = findViewById(R.id.terrain1)
        timePicker1 = findViewById(R.id.timePicker1)
        saveDataButton = findViewById(R.id.saveData)

        //definition des limites du calendrier
>>>>>>> Stashed changes
        val calendar = Calendar.getInstance()
        calendarView1.minDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        calendarView1.maxDate = calendar.timeInMillis

        // Définition des limites d'heure pour le TimePicker
<<<<<<< Updated upstream
        timePicker.setIs24HourView(true)
        timePicker.hour = 22
        timePicker.minute = 0
        timePicker.setOnTimeChangedListener { _, hourOfDay, _ ->
            if (hourOfDay < 7) {
                timePicker.hour = 7
                timePicker.minute = 0
            } else if (hourOfDay > 22) {
                timePicker.hour = 22
                timePicker.minute = 0
            }
        }

        saveDataButton.setOnClickListener {
            val date = calendarView.date
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(date))
            val hour = timePicker.hour
            val terrain = "terrain1"

            // Vérification si le jour sélectionné est un samedi
            val cal = Calendar.getInstance()
            cal.timeInMillis = date
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

            // Vérification des limites d'heure pour le samedi
            val isSaturdayBetween10and18 = dayOfWeek == Calendar.SATURDAY && hour >= 10 && hour < 18
            if (isSaturdayBetween10and18) {
                Toast.makeText(this, "Impossible de réserver entre 10h et 18h le samedi", Toast.LENGTH_SHORT).show()
            } else {
                if (currentUser != null) {
                    database.child("users").child(currentUser.uid).child("date").setValue(dateString)
                    database.child("users").child(currentUser.uid).child("hour").setValue(hour)
                    database.child("users").child(currentUser.uid).child("terrain").setValue(terrain)
                }
                Toast.makeText(this, "Terrain 1 réservé", Toast.LENGTH_SHORT).show()
=======
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

        calendarView1.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Mise à jour de la date dans le calendrier
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        }

        saveDataButton.setOnClickListener {
            // Vérification de la date sélectionnée
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val hour = timePicker1.hour
            val terrain = "terrain1"
            if (dayOfWeek == Calendar.SATURDAY && hour >= 10 && hour < 18) {
                Toast.makeText(this, "Réservation impossible le samedi entre 10h et 18h", Toast.LENGTH_SHORT).show()
            } else {
                // Récupération de la date sélectionnée sous string
                val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                Log.d("date", selectedDate)
                if (currentUser != null) {
                    database.child("users").child(currentUser.uid).child("date").setValue(selectedDate)
                    database.child("users").child(currentUser.uid).child("hour").setValue(hour)
                    database.child("users").child(currentUser.uid).child("terrain").setValue(terrain)
                }
                Toast.makeText(this, "Terrain 1 Reserved", Toast.LENGTH_SHORT).show()
>>>>>>> Stashed changes
                intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
