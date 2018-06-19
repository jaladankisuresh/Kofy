# Kofy
Android Mini Framework for Validating User Input Forms/Views using Kotlin's Kotlin's [Type-Safe Builders](https://kotlinlang.org/docs/reference/type-safe-builders.html)  

What does this framework have to offer?
- All the validation rules stay in a single place making your code REUSABLE and MAINTAINABLE.
- Defining View Validations is more concise making your code READABLE
- Validate either a specific view or the complete form
- Supports the standard Input Views like TextView, EditText, RadioButtons, CheckBoxes and TextInputLayout, and is EXTENSIBLE 

# Usage

kofyValidator is the 

#### Hello Me Example:
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
```

# Contribute
You are welcome to do a pull request. It would greatly help this module if it could find more contributors to build it and test it.

# License
open sourced with [MIT](./LICENSE) license

NOTE : SOFTWARE IS PROVIDED 'AS IS', WITHOUT ANY WARRANTY.


