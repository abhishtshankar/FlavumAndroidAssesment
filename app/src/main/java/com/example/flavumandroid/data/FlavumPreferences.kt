package com.example.flavumandroid.data

// A class to manage Flavum preferences
class FlavumPreferences : Preferences() {

    // Property to store a demo enum value in preferences
    var demoEnumString by stringPref(FlavumPreferenceKeyEnum.DEMO_ENUM.value, "")

    // Property to store the user's type in preferences
    var userType by stringPref(FlavumPreferenceKeyEnum.USER_TYPE.value, "")

    // Property to store the doctor's name in preferences
    var doctorName by stringPref(FlavumPreferenceKeyEnum.DOCTOR_NAME.value, "")

    // Property to store the patient's name in preferences
    var patientName by stringPref(FlavumPreferenceKeyEnum.PATIENT_NAME.value, "")

    // Function to log the user out by clearing relevant preferences
    fun logout() {
        userType = ""
        doctorName = ""
        patientName = ""
    }
}
