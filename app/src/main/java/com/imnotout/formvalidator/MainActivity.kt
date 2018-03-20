package com.imnotout.formvalidator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lyt_email.setErrorEnabled(true)
        lyt_password.setPasswordVisibilityToggleEnabled(true)
        val validator = formValidator {
            validator(lyt_email) {
                notEmpty("Email cannot be empty")
                email()
            }
//            validator(txt_email) {
//                notEmpty("Email cannot be empty")
//                email()
//            }
            validator(lyt_password) {
                minLength()
                password("Password should contain special characters")
            }
            validator(lyt_gender) {
                notEmpty("Gender is mandatory")
            }
            validator(chk_agree_terms) {
                checked()
            }
        }
        btn_sign_up.setOnClickListener {
            validator.validate()
//            validator.validate(txt_email)
        }
    }
}
