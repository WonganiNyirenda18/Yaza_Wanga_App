package com.example.yaza_wanga.validation

object Validator {
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val PHONE_REGEX = Regex("^\\+?[0-9\\s]{7,15}$")

    fun validateEmail(email: String): String? {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) {
            return "Please fill all fields"
        }
        if (!EMAIL_REGEX.matches(trimmed)) {
            return "Please enter a valid email address"
        }
        if (!trimmed.lowercase().endsWith("@unima.ac.mw")) {
            return "Please use your approved school email (@unima.ac.mw)"
        }
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isEmpty()) {
            return "Please fill all fields"
        }
        if (password.length < 8) {
            return "Password must be at least 8 characters"
        }
        return null
    }

    fun validateLogin(email: String, pass: String): String? {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isEmpty() || pass.isEmpty()) {
            return "Please fill all fields"
        }
        val emailErr = validateEmail(trimmedEmail)
        if (emailErr != null) return emailErr
        return null
    }

    fun validateStudentSignUp(
        fullName: String,
        email: String,
        pass: String,
        confirmPass: String,
        termsAccepted: Boolean
    ): String? {
        if (fullName.isBlank() || email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
            return "Please fill all fields"
        }
        val emailErr = validateEmail(email)
        if (emailErr != null) return emailErr

        val passErr = validatePassword(pass)
        if (passErr != null) return passErr

        if (pass != confirmPass) {
            return "Passwords do not match"
        }
        if (!termsAccepted) {
            return "You must agree to the Terms of Service & Privacy Policy"
        }
        return null
    }

    fun validateTutorSignUp(
        fullName: String,
        email: String,
        pass: String,
        confirmPass: String,
        faculty: String,
        guidelinesAccepted: Boolean
    ): String? {
        if (fullName.isBlank() || email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
            return "Please fill all fields"
        }
        val emailErr = validateEmail(email)
        if (emailErr != null) return emailErr

        val passErr = validatePassword(pass)
        if (passErr != null) return passErr

        if (pass != confirmPass) {
            return "Passwords do not match"
        }
        if (faculty.isBlank()) {
            return "Please select your faculty or school"
        }
        if (!guidelinesAccepted) {
            return "You must agree to the Academic Integrity Code and peer tutor guidelines"
        }
        return null
    }

    fun validateStudentProfile(
        department: String,
        programme: String,
        phone: String
    ): String? {
        if (department.isBlank() || programme.isBlank()) {
            return "Please select your department and programme"
        }
        if (phone.isNotBlank() && !PHONE_REGEX.matches(phone.trim())) {
            return "Please enter a valid phone number"
        }
        return null
    }

    fun validatePhone(phone: String): String? {
        if (phone.isBlank()) return null
        if (!PHONE_REGEX.matches(phone.trim())) {
            return "Please enter a valid phone number"
        }
        return null
    }

    fun validateCourseDescription(description: String): String? {
        if (description.length > 500) {
            return "Description cannot exceed 500 characters"
        }
        return null
    }

    fun validateSession(
        format: String,
        meetingLink: String,
        venue: String,
        dateMillis: Long,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): String? {
        if (format == "Online" && meetingLink.isBlank()) {
            return "Valid meeting link required for online sessions"
        }
        if (format == "In-Person" && venue.isBlank()) {
            return "Venue location required for in-person sessions"
        }
        if (dateMillis < currentTimeMillis - 86400000) {
            return "Session date cannot be in the past"
        }
        return null
    }
}
