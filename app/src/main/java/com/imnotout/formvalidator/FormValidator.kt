package com.imnotout.formvalidator

import android.graphics.Color
import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.view.View
import android.widget.*
import java.util.regex.Pattern


fun formValidator(func: FormValidator.() -> Unit) = FormValidator().apply { func() }
enum class RuleType {
    NOT_EMPTY,
    EMAIL,
    MIN_LENGTH,
    PASSWORD,
    CHECKED,
    DATE
}

class FormValidator {
    val children = arrayListOf<ViewValidator<View>>()

    fun validator(view: View, func: ViewValidator<View>.() -> Unit) = children.add( ViewValidator(view).apply { func() } )
    fun validate(): Boolean {
        val failedViews = children.filter { !it.validate() }
        if(failedViews.size == 0) return true

        failedViews.first().view.requestFocus()
        return false
    }
    fun validate(view: View): Boolean = children.filter { it.view == view }.first().validate()

    inner class Rule(var errString: String, val fireRule: ()-> Boolean)
    inner class ViewValidator<T: View>(val view: T) {
        val rules = arrayListOf<Rule>()

        val value: CharSequence
            get() = when(view) {
                is RadioGroup -> {
                    val id = view.checkedRadioButtonId
                    if(id == -1) "" else view.findViewById<RadioButton>(id).text
                }
                is TextInputLayout -> view.editText!!.text
                else -> (view as TextView).text
            }

        fun validate(): Boolean {
            val failedRule = rules.filter { !it.fireRule() }.firstOrNull()
            when(view) {
                is RadioGroup -> {
                    val child = view.getChildAt(0) as TextView
                    child.error = failedRule?.errString
                }
                is TextInputLayout -> {
//                val child = view.editText!!
                    view.error = failedRule?.errString
                }
                is TextView -> view.error = failedRule?.errString
            }
//        failedRule?.let { view.requestFocus() }
            return failedRule == null
        }

        private fun addRule(type: RuleType, errMessage: String?) {
            val errorState = when(type) {
                RuleType.NOT_EMPTY -> Rule("Cannot be empty", { !TextUtils.isEmpty(value) })
                RuleType.EMAIL -> Rule("Not a valid email", { value.contains("@") })
                RuleType.MIN_LENGTH -> Rule("Should atleast be 4 characters long", { value.length >=  4 })
                RuleType.PASSWORD -> Rule("Not a valid password", { !Pattern.matches("^[a-zA-Z0-9]*\$", value) })
                RuleType.CHECKED -> Rule("Checkbox needs to be checked",
                        { if(view is Checkable) view.isChecked else false })
                RuleType.DATE -> Rule("must be in the formate of dd/mmm/yyyy",{!Pattern.matches("^[1-31]-[a-z][a-z][a-z]-[1-9]{4}",value)})
            }
            errMessage?.let { errorState.errString = errMessage }
            rules.add(errorState)
        }

        fun notEmpty(errString: String? = null) = addRule(RuleType.NOT_EMPTY, errString)
        fun email(errString: String? = null) = addRule(RuleType.EMAIL, errString)
        fun minLength(errString: String? = null) = addRule(RuleType.MIN_LENGTH, errString)
        fun password(errString: String? = null) = addRule(RuleType.PASSWORD, errString)
        fun checked(errString: String? = null) = addRule(RuleType.CHECKED, errString)
        fun date(errString: String? = null) = addRule(RuleType.DATE,errString)
    }
}