package com.example.soundguard

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val Registerlabel: TextView = findViewById(R.id.Authentification_lable)
        val UserLogin: EditText = findViewById(R.id.user_auth_login)
        val UserPassword: EditText = findViewById(R.id.user_auth_password)
        val RegisterButton: Button = findViewById(R.id.Log_in_button)
        val LinkToReg: TextView = findViewById(R.id.link_to_reg)


        LinkToReg.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        RegisterButton.setOnClickListener {
            val intent = Intent(this, SoundTest::class.java)
            startActivity(intent)
        }


        RegisterButton.setOnClickListener {
            val login = UserLogin.text.toString().trim()
            val password = UserPassword.text.toString().trim()

            if(login == "" || password == "")
                Toast.makeText(this, "Please fill out everything including password, gmail and login!", Toast.LENGTH_LONG).show()
            else {

                val db = DbHelper(this, null)
                val areAuth = db.getUser(login, password)      // The function "getUser" outputs a boolean value(True/False), therefore initiating conditions below

                if(areAuth) {  // If the function "getUser" is true = the user was found in the DB
                    Toast.makeText(this, "User $login is successfully authorized", Toast.LENGTH_LONG).show()
                    UserLogin.text.clear()
                    UserPassword.text.clear()
                } else  // If the function "getUser" is false = the user was not found in the DB
                    Toast.makeText(this, "Login/password is incorrect, try again", Toast.LENGTH_LONG).show()

            }
        }
    }
}