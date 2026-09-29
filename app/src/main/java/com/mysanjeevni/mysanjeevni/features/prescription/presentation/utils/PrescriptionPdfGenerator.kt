package com.mysanjeevni.mysanjeevni.features.prescription.presentation.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.mysanjeevni.mysanjeevni.features.prescription.domain.model.Prescription
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val TAG = "PRESCRIPTION_PDF"

fun downloadPrescriptionPdf(
    context: Context,
    prescription: Prescription
): Boolean {


    Log.d(TAG, "========================================")
    Log.d(TAG, "PDF DOWNLOAD STARTED")
    Log.d(TAG, "Prescription ID = ${prescription.id}")
    Log.d(TAG, "Doctor = ${prescription.doctorName}")
    Log.d(TAG, "Diagnosis = ${prescription.diagnosis}")
    Log.d(TAG, "Medicines count = ${prescription.medicines.size}")
    Log.d(TAG, "Android SDK = ${Build.VERSION.SDK_INT}")
    Log.d(TAG, "========================================")

    val document = PdfDocument()

    return try {

        // -------------------------------------------------
        // CREATE PAGE
        // -------------------------------------------------

        Log.d(TAG, "STEP 1: Creating PDF page")

        val pageInfo = PdfDocument.PageInfo.Builder(
            595,
            842,
            1
        ).create()

        val page = document.startPage(pageInfo)

        Log.d(TAG, "STEP 1 SUCCESS: PDF page created")

        val canvas = page.canvas

        // -------------------------------------------------
        // PAINTS
        // -------------------------------------------------

        Log.d(TAG, "STEP 2: Creating Paint objects")

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 26f
            isFakeBoldText = true
        }

        val headingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 18f
            isFakeBoldText = true
        }

        val normalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 14f
        }

        Log.d(TAG, "STEP 2 SUCCESS: Paint objects created")

        var y = 50f

        val doctorName =
            if (prescription.doctorName.startsWith("Dr.", true)) {
                prescription.doctorName
            } else {
                "Dr. ${prescription.doctorName}"
            }

        Log.d(TAG, "Doctor name resolved = $doctorName")

        // -------------------------------------------------
        // HEADER
        // -------------------------------------------------

        Log.d(TAG, "STEP 3: Drawing prescription header")

        canvas.drawText(
            "MySanjeevni",
            40f,
            y,
            titlePaint
        )

        y += 35f

        canvas.drawText(
            "Medical Prescription",
            40f,
            y,
            headingPaint
        )

        y += 35f

        canvas.drawText(
            doctorName,
            40f,
            y,
            headingPaint
        )

        y += 25f

        canvas.drawText(
            "Registration No: ${
                prescription.doctorRegistrationNumber.ifBlank {
                    "—"
                }
            }",
            40f,
            y,
            normalPaint
        )

        Log.d(TAG, "STEP 3 SUCCESS: Header drawn")

        // -------------------------------------------------
        // DATES
        // -------------------------------------------------

        Log.d(TAG, "STEP 4: Drawing prescription dates")

        y += 35f

        canvas.drawText(
            "Consultation Date: ${
                formatPdfDate(
                    prescription.appointmentDate
                )
            }",
            40f,
            y,
            normalPaint
        )

        y += 22f

        canvas.drawText(
            "Issued: ${
                formatPdfDate(
                    prescription.issueDate
                )
            }",
            40f,
            y,
            normalPaint
        )

        y += 22f

        canvas.drawText(
            "Expires: ${
                formatPdfDate(
                    prescription.expiryDate
                )
            }",
            40f,
            y,
            normalPaint
        )

        Log.d(TAG, "STEP 4 SUCCESS: Dates drawn")

        // -------------------------------------------------
        // CONSULTATION TYPE
        // -------------------------------------------------

        y += 40f

        val consultationType =
            prescription.consultationType
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.replaceFirstChar {
                    it.uppercase()
                }
                ?: "—"

        Log.d(TAG, "Consultation type = $consultationType")

        canvas.drawText(
            "Consultation Type: $consultationType",
            40f,
            y,
            normalPaint
        )

        // -------------------------------------------------
        // SYMPTOMS
        // -------------------------------------------------

        y += 40f

        if (!prescription.symptoms.isNullOrBlank()) {

            Log.d(TAG, "STEP 5: Drawing symptoms")
            Log.d(TAG, "Symptoms present = true")

            canvas.drawText(
                "Symptoms",
                40f,
                y,
                headingPaint
            )

            y += 25f

            canvas.drawText(
                prescription.symptoms,
                40f,
                y,
                normalPaint
            )

            y += 40f

            Log.d(TAG, "STEP 5 SUCCESS: Symptoms drawn")

        } else {

            Log.d(TAG, "STEP 5: No symptoms available")
        }

        // -------------------------------------------------
        // DIAGNOSIS
        // -------------------------------------------------

        Log.d(TAG, "STEP 6: Drawing diagnosis")

        canvas.drawText(
            "Diagnosis",
            40f,
            y,
            headingPaint
        )

        y += 25f

        val diagnosis =
            prescription.diagnosis.ifBlank {
                "—"
            }

        Log.d(TAG, "Diagnosis = $diagnosis")

        canvas.drawText(
            diagnosis,
            40f,
            y,
            normalPaint
        )

        y += 40f

        Log.d(TAG, "STEP 6 SUCCESS: Diagnosis drawn")

        // -------------------------------------------------
        // MEDICINES
        // -------------------------------------------------

        Log.d(TAG, "STEP 7: Drawing medicines")
        Log.d(
            TAG,
            "Medicine count = ${prescription.medicines.size}"
        )

        canvas.drawText(
            "Medicines",
            40f,
            y,
            headingPaint
        )

        y += 30f

        if (prescription.medicines.isEmpty()) {

            Log.d(TAG, "No medicines prescribed")

            canvas.drawText(
                "No medicines prescribed",
                40f,
                y,
                normalPaint
            )

            y += 30f

        } else {

            prescription.medicines.forEachIndexed { index,
                                                    medicine ->

                Log.d(
                    TAG,
                    "Medicine[$index]: " +
                            "name=${medicine.name}, " +
                            "dosage=${medicine.dosage}, " +
                            "frequency=${medicine.frequency}, " +
                            "duration=${medicine.duration}"
                )

                canvas.drawText(
                    "${index + 1}. ${medicine.name}",
                    40f,
                    y,
                    headingPaint
                )

                y += 22f

                canvas.drawText(
                    "Dosage: ${medicine.dosage}",
                    60f,
                    y,
                    normalPaint
                )

                y += 22f

                canvas.drawText(
                    "Frequency: ${medicine.frequency}",
                    60f,
                    y,
                    normalPaint
                )

                y += 22f

                canvas.drawText(
                    "Duration: ${medicine.duration}",
                    60f,
                    y,
                    normalPaint
                )

                y += 30f
            }
        }

        Log.d(TAG, "STEP 7 SUCCESS: Medicines drawn")

        // -------------------------------------------------
        // NOTES
        // -------------------------------------------------

        if (prescription.notes.isNotBlank()) {

            Log.d(TAG, "STEP 8: Drawing additional notes")

            canvas.drawText(
                "Additional Notes",
                40f,
                y,
                headingPaint
            )

            y += 25f

            canvas.drawText(
                prescription.notes,
                40f,
                y,
                normalPaint
            )

            Log.d(TAG, "STEP 8 SUCCESS: Notes drawn")

        } else {

            Log.d(TAG, "STEP 8: No additional notes")
        }

        // -------------------------------------------------
        // FINISH PAGE
        // -------------------------------------------------

        Log.d(TAG, "STEP 9: Finishing PDF page")

        document.finishPage(page)

        Log.d(TAG, "STEP 9 SUCCESS: PDF page finished")

        // -------------------------------------------------
        // FILE NAME
        // -------------------------------------------------

        val fileName =
            "MySanjeevni_Prescription_${
                SimpleDateFormat(
                    "yyyyMMdd_HHmmss",
                    Locale.US
                ).format(Date())
            }.pdf"

        Log.d(TAG, "STEP 10: Generated file name")
        Log.d(TAG, "File name = $fileName")

        // -------------------------------------------------
        // SAVE PDF
        // -------------------------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            Log.d(
                TAG,
                "STEP 11: Android 10+ detected"
            )

            Log.d(
                TAG,
                "Calling savePdfAndroid10Plus()"
            )

            val result = savePdfAndroid10Plus(
                context = context,
                document = document,
                fileName = fileName
            )

            Log.d(
                TAG,
                "savePdfAndroid10Plus result = $result"
            )

            result

        } else {

            Log.d(
                TAG,
                "STEP 11: Android 9 or below detected"
            )

            Log.d(
                TAG,
                "Calling savePdfLegacy()"
            )

            val result = savePdfLegacy(
                document = document,
                fileName = fileName
            )

            Log.d(
                TAG,
                "savePdfLegacy result = $result"
            )

            result
        }

    } catch (e: Exception) {

        Log.e(TAG, "========================================")
        Log.e(TAG, "PDF DOWNLOAD FAILED")
        Log.e(TAG, "Exception Type = ${e.javaClass.simpleName}")
        Log.e(TAG, "Exception Message = ${e.message}")
        Log.e(TAG, "Prescription ID = ${prescription.id}")
        Log.e(TAG, "========================================", e)

        false

    } finally {

        Log.d(TAG, "Closing PdfDocument")

        document.close()

        Log.d(TAG, "PDF DOWNLOAD PROCESS FINISHED")
        Log.d(TAG, "========================================")
    }
}


// =========================================================
// ANDROID 10+
// =========================================================

@RequiresApi(Build.VERSION_CODES.Q)
private fun savePdfAndroid10Plus(
    context: Context,
    document: PdfDocument,
    fileName: String
): Boolean {

    val resolver = context.contentResolver

    val values = ContentValues().apply {

        put(
            MediaStore.Downloads.DISPLAY_NAME,
            fileName
        )

        put(
            MediaStore.Downloads.MIME_TYPE,
            "application/pdf"
        )

        put(
            MediaStore.Downloads.RELATIVE_PATH,
            Environment.DIRECTORY_DOWNLOADS
        )

        put(
            MediaStore.Downloads.IS_PENDING,
            1
        )
    }

    val uri = resolver.insert(
        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
        values
    ) ?: return false

    return try {

        resolver.openOutputStream(uri)?.use { outputStream ->

            document.writeTo(outputStream)
        }

        val completedValues = ContentValues().apply {

            put(
                MediaStore.Downloads.IS_PENDING,
                0
            )
        }

        resolver.update(
            uri,
            completedValues,
            null,
            null
        )

        true

    } catch (e: Exception) {

        resolver.delete(
            uri,
            null,
            null
        )

        e.printStackTrace()

        false
    }
}


// =========================================================
// ANDROID 9 AND BELOW
// =========================================================

@Suppress("DEPRECATION")
private fun savePdfLegacy(
    document: PdfDocument,
    fileName: String
): Boolean {

    val downloadsDirectory =
        Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )

    if (!downloadsDirectory.exists()) {
        downloadsDirectory.mkdirs()
    }

    val file = File(
        downloadsDirectory,
        fileName
    )

    FileOutputStream(file).use { outputStream ->

        document.writeTo(outputStream)
    }

    return file.exists()
}


// =========================================================
// DATE FORMAT
// =========================================================

private fun formatPdfDate(
    date: String?
): String {

    if (date.isNullOrBlank()) {
        return "—"
    }

    return try {

        val input = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.US
        ).apply {

            timeZone =
                TimeZone.getTimeZone("UTC")
        }

        val output = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.US
        ).apply {

            timeZone =
                TimeZone.getTimeZone("Asia/Kolkata")
        }

        val parsed = input.parse(date)

        if (parsed != null) {
            output.format(parsed)
        } else {
            date
        }

    } catch (e: Exception) {

        date
    }
}