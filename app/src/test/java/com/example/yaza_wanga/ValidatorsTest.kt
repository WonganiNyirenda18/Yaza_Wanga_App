package com.example.yaza_wanga

import com.example.yaza_wanga.validation.Validator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidatorsTest {

    // --- LOGIN VALIDATION TESTS ---

    @Test
    fun login_emptyFields_returnsError() {
        // AUTH_LOGIN_001
        val err1 = Validator.validateLogin("", "")
        assertEquals("Please fill all fields", err1)

        val err2 = Validator.validateLogin("student@unima.ac.mw", "")
        assertEquals("Please fill all fields", err2)

        val err3 = Validator.validateLogin("", "password123")
        assertEquals("Please fill all fields", err3)
    }

    @Test
    fun login_badEmailFormat_returnsError() {
        // AUTH_LOGIN_002
        val err = Validator.validateLogin("invalid-email-format", "password123")
        assertEquals("Please enter a valid email address", err)
    }

    @Test
    fun login_nonSchoolEmail_returnsError() {
        // AUTH_LOGIN_003
        val err = Validator.validateLogin("student@gmail.com", "password123")
        assertEquals("Please use your approved school email (@unima.ac.mw)", err)
    }

    @Test
    fun login_validCredentials_returnsNull() {
        // AUTH_LOGIN_004
        val err = Validator.validateLogin("student@unima.ac.mw", "password123")
        assertNull(err)
    }

    // --- SIGN-UP VALIDATION TESTS ---

    @Test
    fun signUp_passwordMismatch_returnsError() {
        // AUTH_SIGNUP_001
        val errStudent = Validator.validateStudentSignUp(
            fullName = "John Doe",
            email = "john@unima.ac.mw",
            pass = "Pass1234",
            confirmPass = "Pass5678",
            termsAccepted = true
        )
        assertEquals("Passwords do not match", errStudent)

        val errTutor = Validator.validateTutorSignUp(
            fullName = "Jane Doe",
            email = "jane@unima.ac.mw",
            pass = "Pass1234",
            confirmPass = "Pass5678",
            faculty = "Computer Science",
            guidelinesAccepted = true
        )
        assertEquals("Passwords do not match", errTutor)
    }

    @Test
    fun signUp_7vs8CharPassword_validatesMinLength() {
        // AUTH_SIGNUP_002
        val err7Chars = Validator.validatePassword("1234567")
        assertEquals("Password must be at least 8 characters", err7Chars)

        val err8Chars = Validator.validatePassword("12345678")
        assertNull(err8Chars)
    }

    @Test
    fun signUp_studentTermsUnticked_returnsError() {
        // AUTH_SIGNUP_003
        val err = Validator.validateStudentSignUp(
            fullName = "John Doe",
            email = "john@unima.ac.mw",
            pass = "Pass1234",
            confirmPass = "Pass1234",
            termsAccepted = false
        )
        assertEquals("You must agree to the Terms of Service & Privacy Policy", err)
    }

    @Test
    fun signUp_tutorGuidelinesUnticked_returnsError() {
        // AUTH_SIGNUP_004
        val err = Validator.validateTutorSignUp(
            fullName = "Jane Doe",
            email = "jane@unima.ac.mw",
            pass = "Pass1234",
            confirmPass = "Pass1234",
            faculty = "Science",
            guidelinesAccepted = false
        )
        assertEquals("You must agree to the Academic Integrity Code and peer tutor guidelines", err)
    }

    @Test
    fun signUp_tutorNoFaculty_returnsError() {
        // AUTH_SIGNUP_005
        val err = Validator.validateTutorSignUp(
            fullName = "Jane Doe",
            email = "jane@unima.ac.mw",
            pass = "Pass1234",
            confirmPass = "Pass1234",
            faculty = "",
            guidelinesAccepted = true
        )
        assertEquals("Please select your faculty or school", err)
    }

    @Test
    fun signUp_validStudent_returnsNull() {
        // AUTH_SIGNUP_006
        val err = Validator.validateStudentSignUp(
            fullName = "John Doe",
            email = "john@unima.ac.mw",
            pass = "Pass1234",
            confirmPass = "Pass1234",
            termsAccepted = true
        )
        assertNull(err)
    }

    @Test
    fun signUp_validTutor_returnsNull() {
        // AUTH_SIGNUP_007
        val err = Validator.validateTutorSignUp(
            fullName = "Jane Doe",
            email = "jane@unima.ac.mw",
            pass = "Pass1234",
            confirmPass = "Pass1234",
            faculty = "School of Science",
            guidelinesAccepted = true
        )
        assertNull(err)
    }

    // --- PROFILE PHONE VALIDATION TESTS ---

    @Test
    fun profilePhone_digitsPlusSpaces_validatesCorrectly() {
        // PROFILE_PHONE_001
        assertNull(Validator.validatePhone("+265995358915"))
        assertNull(Validator.validatePhone("+265 995 358 915"))
        assertNull(Validator.validatePhone("0995358915"))
        assertNull(Validator.validatePhone(""))

        val errInvalid = Validator.validatePhone("abc-123")
        assertEquals("Please enter a valid phone number", errInvalid)

        val errTooShort = Validator.validatePhone("123")
        assertEquals("Please enter a valid phone number", errTooShort)
    }

    // --- COURSE DESCRIPTION LENGTH TESTS ---

    @Test
    fun courseDescriptionLength_500vs501Chars_validatesBoundary() {
        // COURSE_DESC_001
        val desc500 = "A".repeat(500)
        assertNull(Validator.validateCourseDescription(desc500))

        val desc501 = "A".repeat(501)
        val err = Validator.validateCourseDescription(desc501)
        assertEquals("Description cannot exceed 500 characters", err)
    }

    // --- TUTOR SESSION VALIDATION TESTS ---

    @Test
    fun tutorSession_onlineMissingLink_returnsError() {
        // SESSION_VAL_001
        val futureTime = System.currentTimeMillis() + 86400000
        val err = Validator.validateSession("Online", "", "Lab 1", futureTime)
        assertEquals("Valid meeting link required for online sessions", err)
    }

    @Test
    fun tutorSession_inPersonMissingVenue_returnsError() {
        // SESSION_VAL_002
        val futureTime = System.currentTimeMillis() + 86400000
        val err = Validator.validateSession("In-Person", "https://meet.google.com/abc", "", futureTime)
        assertEquals("Venue location required for in-person sessions", err)
    }

    @Test
    fun tutorSession_dateInPast_returnsError() {
        // SESSION_VAL_003
        val now = System.currentTimeMillis()
        val pastTime = now - (2 * 86400000)
        val err = Validator.validateSession("Online", "https://meet.google.com/abc", "", pastTime, now)
        assertEquals("Session date cannot be in the past", err)
    }

    @Test
    fun tutorSession_validParameters_returnsNull() {
        // SESSION_VAL_004
        val now = System.currentTimeMillis()
        val futureTime = now + 86400000
        val onlineErr = Validator.validateSession("Online", "https://meet.google.com/abc", "", futureTime, now)
        assertNull(onlineErr)

        val inPersonErr = Validator.validateSession("In-Person", "", "Room 101", futureTime, now)
        assertNull(inPersonErr)
    }
}
