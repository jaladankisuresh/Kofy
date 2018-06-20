# Kofy
Android Mini Framework for Validating User Input Forms/Views using Kotlin's Kotlin's [Type-Safe Builders](https://kotlinlang.org/docs/reference/type-safe-builders.html)  

What does this framework have to offer?
- All the validation rules stay in a single place making your code REUSABLE and MAINTAINABLE.
- Defining View Validations is more concise making your code READABLE
- Validate either a specific view or the complete form
- Supports the standard Input Views like TextView, EditText, RadioButtons, CheckBoxes and TextInputLayout, and is EXTENSIBLE 

# Usage
### Kofy Markup syntax

**kofyValidator** is the root markup element for any form validations. It is usually defined at the Activity/Fragment level where there are bunch of views that need to be validated.  

**kofyValidator.validate()** without no arguments validates the complete form, and on passing the View as an argument validates that specific View.
```kotlin
val kofy = kofyValidator {
            ...
            ...
}
kofy.validate() // validates the complete form
kofy.validate(lyt_email) // validates lyt_email view
```

**validator** is the view level markup element for validating the view in context. All the **validator**'s share **kofyValidator** as their parent and are nested inside the **kofyValidator**  

**_rule_** is a child element and can be defined under any view level **validator**. One could have multiple rules under any view   **validator** and any **_rule_** could be under multiple view level **validator**'s. Unlike **kofyValidator** and **validator**, **_rule_** is not a keyword, but a App defined custom rule in the rulesDictionary of [KofyValidator.kt](/app/src/main/java/com/imnotout/kofy/KofyValidator.kt)  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ex: notEmpty(), minLength()  
- **_rule_**'s come with default validation criteria and error message to show on failure. thus, they could be called without any arguments
- At the calling site, any *rule* can be overriden by passing arguments to it. arguments vary based on the rule, checkout the [rulesDictionary](/app/src/main/java/com/imnotout/kofy/KofyValidator.kt)  


#### Hello Me Example:
![Kofy Form Validation](/kofy_form_validation.png "Kofy Form Validation")
```kotlin
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
//      Validate on the form when clicking btn_sign_up  
        btn_sign_up.setOnClickListener {
            kofy.validate()
//            kofy.validate(lyt_email)
        }
```  
```
validator(lyt_email)
- not empty
- validate email by passing regular expression as an argument

validator(lyt_username)
- min length
- max length by passing size and custom error message as args
- only alphaNumeric charactes are supported  

validator(lyt_password)
- min length by passing size and custom error message as args
- validate password for special characters

validator(lyt_dob)
- validate date is valid and in format dd/MM/yyyy format  

validator(lyt_gender)
- not empty

validator(chk_agree_terms)  
- is checked
```

# Contribute
You are welcome to do a pull request. It would greatly help this module if it could find more contributors to build it and test it.

# License
open sourced with [MIT](./LICENSE) license

NOTE : SOFTWARE IS PROVIDED 'AS IS', WITHOUT ANY WARRANTY.


