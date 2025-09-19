package com.example.soundguard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val Registerlabel: TextView = findViewById(R.id.register_lable)
        val UserLogin: EditText = findViewById(R.id.user_login)
        val UserGmail: EditText = findViewById(R.id.user_gmail)
        val UserPassword: EditText = findViewById(R.id.user_password)
        val RegisterButton: Button = findViewById(R.id.register_button)
        val LinkToAuth: TextView = findViewById(R.id.link_to_auth)

        LinkToAuth.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        RegisterButton.setOnClickListener {
            val login = UserLogin.text.toString().trim()
            val gmail = UserGmail.text.toString().trim()
            val password = UserPassword.text.toString().trim()

            if(login.isEmpty() || gmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out everything including password, gmail and login!", Toast.LENGTH_LONG).show()
            } else {
                val db = AppDatabase.getDatabase(this)
                val repository = HearingHealthRepository(db.userDao(), db.soundMeasurementDao())

                lifecycleScope.launch {
                    // Check if user already exists
                    val existingUser = repository.getUserByLogin(login)
                    if (existingUser != null) {
                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterMainActivity,
                                "User with this login already exists",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        val user = User(login = login, gmail = gmail, password = password)
                        val userId = repository.addUser(user)

                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterMainActivity,
                                "Registration Successful! Login: $login",
                                Toast.LENGTH_LONG
                            ).show()

                            UserLogin.text.clear()
                            UserGmail.text.clear()
                            UserPassword.text.clear()

                            // Go back to login screen
                            val intent = Intent(this@RegisterMainActivity, AuthActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}