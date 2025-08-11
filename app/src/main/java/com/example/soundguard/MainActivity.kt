package com.example.soundguard

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
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

            if(login == "" || gmail == "" || password == "")
                Toast.makeText(this, "Please fill out everything including password, gmail and login!", Toast.LENGTH_LONG).show()
            else {
                val user = User(login, gmail, password)
                val db = DbHelper(this, null)
                db.addUser(user)
                Toast.makeText(this, "Registration Successful! Login: $login", Toast.LENGTH_LONG).show()

                UserLogin.text.clear()
                UserGmail.text.clear()
                UserPassword.text.clear()
            }
        }
    }
}
