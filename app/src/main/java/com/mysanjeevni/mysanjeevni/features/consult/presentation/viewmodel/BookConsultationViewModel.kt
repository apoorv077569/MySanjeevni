package com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.BookConsultationRequest
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Doctor
import com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase.BookConsultationUseCase
import com.mysanjeevni.mysanjeevni.features.consult.domnain.usecase.GetDoctorUseCase
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.BookConsultationUiState
import com.mysanjeevni.mysanjeevni.utils.FcmHelper
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookConsultationViewModel @Inject constructor(
    private val getDoctorUseCase: GetDoctorUseCase,
    private val bookConsultationUseCase: BookConsultationUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    companion object {
        private const val TAG = "BookConsultationVM"
    }

    private val _state = MutableStateFlow(BookConsultationUiState())

    val state: StateFlow<BookConsultationUiState> =
        _state.asStateFlow()

    val userId = sessionManager.getUserId()


    // ---------------------------------------------------------
    // Doctor
    // ---------------------------------------------------------

    fun updateDoctor(doctor: Doctor) {
        _state.update {
            it.copy(
                doctor = doctor,
                error = null
            )
        }
    }


    // ---------------------------------------------------------
    // Patient
    // ---------------------------------------------------------

    fun onPatientNameChange(name: String) {
        _state.update {
            it.copy(patientName = name)
        }
    }

    fun onPhoneChange(phone: String) {
        _state.update {
            it.copy(phone = phone)
        }
    }

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(email = email)
        }
    }


    // ---------------------------------------------------------
    // Appointment
    // ---------------------------------------------------------

    fun onDateChange(date: String) {
        _state.update {
            it.copy(appointmentDate = date)
        }
    }

    fun onConsultationTypeChange(type: String) {
        _state.update {
            it.copy(consultationType = type)
        }
    }

    fun onSymptomChange(symptom: String) {
        _state.update {
            it.copy(symptoms = symptom)
        }
    }


    // ---------------------------------------------------------
    // Load Doctor
    // ---------------------------------------------------------

    fun loadDoctor(doctorId: String) {

        Log.d(TAG, "loadDoctor() called")
        Log.d(TAG, "doctorId = $doctorId")

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            getDoctorUseCase()
                .onSuccess { doctors ->

                    Log.d(
                        TAG,
                        "Doctors fetched successfully. count=${doctors.size}"
                    )

                    val doctor = doctors.find {
                        it.id == doctorId
                    }

                    if (doctor != null) {

                        Log.d(
                            TAG,
                            "Doctor found: id=${doctor.id}, name=${doctor.name}"
                        )

                    } else {

                        Log.e(
                            TAG,
                            "Doctor not found: $doctorId"
                        )
                    }

                    _state.update {
                        it.copy(
                            doctor = doctor,
                            isLoading = false,
                            error = if (doctor == null) {
                                "Doctor not found"
                            } else {
                                null
                            }
                        )
                    }
                }
                .onFailure { exception ->

                    Log.e(
                        TAG,
                        "Failed to load doctor: ${exception.message}",
                        exception
                    )

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.localizedMessage
                                ?: "Failed to load doctor"
                        )
                    }
                }
        }
    }
    fun onBookAppointment() {

        val currentState = _state.value

        Log.d(TAG, "onBookAppointment() called")

        if (currentState.doctor == null) {

            Log.e(TAG, "Booking failed: doctor is null")

            _state.update {
                it.copy(
                    error = "Doctor information is missing"
                )
            }

            return
        }

        if (currentState.patientName.isBlank()) {

            Log.e(TAG, "Booking failed: patient name empty")

            _state.update {
                it.copy(
                    error = "Please enter patient name"
                )
            }

            return
        }

        if (currentState.appointmentDate.isBlank()) {

            Log.e(TAG, "Booking failed: appointment date empty")

            _state.update {
                it.copy(
                    error = "Please select appointment date"
                )
            }

            return
        }


        // ---------------------------------------------
        // Doctor
        // ---------------------------------------------

        val doctor = currentState.doctor

        Log.d(TAG, "doctorId = ${doctor.id}")
        Log.d(TAG, "doctorName = ${doctor.name}")


        // ---------------------------------------------
        // Build Request
        // ---------------------------------------------

        val request = BookConsultationRequest(

            userId = userId,

            doctorId = doctor.id,

            patientName = currentState.patientName,

            patientPhone = currentState.phone
                .takeIf { it.isNotBlank() },

            patientEmail = currentState.email
                .takeIf { it.isNotBlank() },

            appointmentDate = currentState.appointmentDate,

            consultationType = currentState.consultationType,

            symptoms = currentState.symptoms
                .takeIf { it.isNotBlank() }
        )


        Log.d(
            TAG,
            """
            Booking request:
            doctorId=${request.doctorId}
            patientName=${request.patientName}
            appointmentDate=${request.appointmentDate}
            consultationType=${request.consultationType}
            hasPhone=${!request.patientPhone.isNullOrBlank()}
            hasEmail=${!request.patientEmail.isNullOrBlank()}
            hasSymptoms=${!request.symptoms.isNullOrBlank()}
            """.trimIndent()
        )


        // ---------------------------------------------
        // API
        // ---------------------------------------------

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    isBookingSuccessful = false
                )
            }

            Log.d(
                TAG,
                "Calling BookConsultationUseCase..."
            )

            bookConsultationUseCase(request)
                .onSuccess { response ->

                    Log.d(
                        TAG,
                        "Consultation booked successfully"
                    )

                    Log.d(
                        TAG,
                        "message = ${response.message}"
                    )

                    Log.d(
                        TAG,
                        "consultationId = ${response.consultation?.id}"
                    )
                    FcmHelper.sendNotification(
                        userId.toString(),
                        "Consultation Booked",
                        "Your consultation with Dr. ${doctor.name} has been booked successfully for ${currentState.appointmentDate}."
                    )
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            isBookingSuccessful = true
                        )
                    }
                }
                .onFailure { exception ->

                    Log.e(
                        TAG,
                        "Booking consultation failed",
                        exception
                    )

                    Log.e(
                        TAG,
                        "Error = ${exception.message}"
                    )

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to book consultation",
                            isBookingSuccessful = false
                        )
                    }
                }
        }
    }

    fun resetBookingState() {

        Log.d(
            TAG,
            "resetBookingState()"
        )

        _state.update {
            it.copy(
                isBookingSuccessful = false,
                error = null
            )
        }
    }
    fun loadPatientFromSession() {

        Log.d(TAG, "Loading patient details from SessionManager")

        val userId = sessionManager.getUserId()
        val name = sessionManager.getUserName()
        val phone = sessionManager.getPhone()
        val email = sessionManager.getUserEmail()

        Log.d(TAG, "Session userId = $userId")
        Log.d(TAG, "Session patientName = $name")
        Log.d(TAG, "Session phone = $phone")
        Log.d(TAG, "Session email = $email")

        _state.update {
            it.copy(
                patientName = name.orEmpty(),
                phone = phone.orEmpty(),
                email = email.orEmpty()
            )
        }
    }
}