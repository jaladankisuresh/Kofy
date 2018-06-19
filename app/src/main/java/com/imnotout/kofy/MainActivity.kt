package com.imnotout.kofy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        lyt_email.setErrorEnabled(true)
        lyt_password.setPasswordVisibilityToggleEnabled(true)
        val kofy = kofyValidator {
            validator(lyt_email) {
                notEmpty("Email cannot be empty")
                email("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
            }
            validator(lyt_username) {
                minLength()
                maxLength(6, "Username should not exceed beyond 6 characters")
                alphaNumeric()
            }
            validator(lyt_password) {
                minLength(6, "Should atleast be 6 characters long")
                password("Password should contain special characters")
            }
            validator(lyt_dob) {
                date()
            }
            validator(lyt_gender) {
                notEmpty("Gender is mandatory")
            }
            validator(chk_agree_terms) {
                checked()
            }
        }
        btn_sign_up.setOnClickListener {
            kofy.validate()
//            kofy.validate(txt_email)
        }
    }
}
