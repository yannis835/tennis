package com.example.tennis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.tennis.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signInButton = binding.signInButton
        signUpButton = binding.signUpButton

        signInButton.setOnClickListener {
            val intentToSignInActivity = Intent(this, SignInActivity::class.java)
            startActivity(intentToSignInActivity)
        }

        signUpButton.setOnClickListener {
            val intentToSignUpActivity = Intent(this, SignUpActivity::class.java)
            startActivity(intentToSignUpActivity)
        }
    }
}
