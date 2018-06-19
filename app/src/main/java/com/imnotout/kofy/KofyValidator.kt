package com.imnotout.kofy

import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.view.View
import android.widget.*
import java.util.regex.Pattern


fun kofyValidator(func: KofyValidator.() -> Unit) = KofyValidator().apply { func() }
enum class RuleType {
    NOT_EMPTY,
    EMAIL,
    MIN_LENGTH,
    MAX_LENGTH,
    PASSWORD,
    ALPHA_NUMERIC,
    CHECKED,
    DATE
}

class KofyValidator {
    val children = arrayListOf<ViewValidator<View>>()

    fun validator(view: View, func: ViewValidator<View>.() -> Unit) = children.add( ViewValidator(view).apply { func() } )
    fun validate(): Boolean {
        val failedViews = children.filter { !it.validate() }
        if(failedViews.size > 0) failedViews.first().view.requestFocus()

        return failedViews.size == 0
    }
    fun validate(view: View): Boolean = children.filter { it.view == view }.first().validate()

    inner class Rule(val errString: String, val fireRule: ()-> Boolean)
    inner class ViewValidator<T: View>(val view: T) {
        val alphaNumericPattern = Pattern.compile("^[a-zA-Z0-9]*\$", Pattern.CASE_INSENSITIVE)
        val rulesDictionary = hashMapOf(
            RuleType.NOT_EMPTY to Rule("Cannot be empty", { !TextUtils.isEmpty(value) }),
            RuleType.MIN_LENGTH to Rule("Should atleast be 4 characters long", { value.length >=  4 }),
            RuleType.MAX_LENGTH to Rule("Should not exceed beyond 8 characters", { value.length <=  8 }),
            RuleType.CHECKED to Rule("Checkbox needs to be checked",
                { if(view is Checkable) view.isChecked else false }),
            RuleType.EMAIL to Rule("Not a valid email", { value.contains("@") }),
            RuleType.PASSWORD to Rule("Not a valid password", {
                !alphaNumericPattern.matcher(value).matches()
            }),
            RuleType.ALPHA_NUMERIC to Rule("No Special characters are allowed", {
                alphaNumericPattern.matcher(value).matches()
            }),
            RuleType.DATE to Rule("Must be in the format of dd/mm/yyyy",{
                Pattern.matches("^(((0[1-9]|[12]\\d|3[01])\\/(0[13578]|1[02])\\/((19|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\/(0[13456789]|1[012])\\/((19|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\/02\\/((19|[2-9]\\d)\\d{2}))|(29\\/02\\/((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))\$", value)})
        )
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
                    view.error = failedRule?.errString
                }
                is TextView -> view.error = failedRule?.errString
            }
//        failedRule?.let { view.requestFocus() }
            return failedRule == null
        }

        private fun addRule(type: RuleType, errString: String?) {
            val defaultRule = rulesDictionary.get(type)!!
            val rule = if(errString == null) defaultRule else Rule(errString, defaultRule.fireRule)
            rules.add(rule)
        }
        private fun addRule(type: RuleType, rule: Rule) = rules.add(rule)

        fun notEmpty(errString: String? = null) = addRule(RuleType.NOT_EMPTY, errString)
        fun minLength(size: Int? = null, errString: String? = null) {
            val defaultRule = rulesDictionary.get(RuleType.MIN_LENGTH)!!
            val rule = when {
                size == null -> defaultRule
                errString == null -> Rule(defaultRule.errString, { value.length >=  size })
                else -> Rule(errString, { value.length >=  size })
            }
            addRule(RuleType.MIN_LENGTH, rule)
        }
        fun maxLength(size: Int? = null, errString: String? = null) {
            val defaultRule = rulesDictionary.get(RuleType.MAX_LENGTH)!!
            val rule = when {
                size == null -> defaultRule
                errString == null -> Rule(defaultRule.errString, { value.length <=  size })
                else -> Rule(errString, { value.length <=  size })
            }
            addRule(RuleType.MIN_LENGTH, rule)
        }
        fun email(regex: String? = null, errString: String? = null) {
            val defaultRule = rulesDictionary.get(RuleType.EMAIL)!!
            val emailPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val rule = when {
                regex == null -> defaultRule
                errString == null -> Rule(defaultRule.errString, {
                    emailPattern.matcher(value).matches()
                })
                else -> Rule(errString, {emailPattern.matcher(value).matches()})
            }
            addRule(RuleType.MIN_LENGTH, rule)
        }
        fun alphaNumeric(errString: String? = null) = addRule(RuleType.ALPHA_NUMERIC, errString)
        fun password(errString: String? = null) = addRule(RuleType.PASSWORD, errString)
        fun checked(errString: String? = null) = addRule(RuleType.CHECKED, errString)
        fun date(errString: String? = null) = addRule(RuleType.DATE,errString)
    }
}