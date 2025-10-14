package org.impactindiafoundation.iifllemeddocket.Activity


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintJob
import android.print.PrintManager

import java.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityMain1Binding
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityPrintBinding
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException


class PrintActivity1 : AppCompatActivity() {

    lateinit var binding: ActivityPrintBinding
    private lateinit var printJob: PrintJob
    lateinit var sessionManager: SessionManager
    var gson = Gson()
    var printOutJobName:String?=null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPrintBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        sessionManager= SessionManager(this)

        val refractiveError: RefractiveError? = sessionManager.getRefractiveError()

        Log.d(ConstantsApp.TAG,"re_prism_unit_left=>"+refractiveError!!.re_prism_unit_left)
        Log.d(ConstantsApp.TAG,"re_prism_unit_right=>"+refractiveError!!.re_prism_unit_right)

        if (refractiveError != null) {
            binding.TextViewPatientID.text= refractiveError!!.patient_id.toString()
            binding.TextViewDate.text=refractiveError.createdDate.toString()
            when(refractiveError.re_distant_vision_sphere_right) {
                "select"-> {
                    binding.TextViewDistanceRightSph.text=""
                }else-> {
                    binding.TextViewDistanceRightSph.text=refractiveError.re_distant_vision_sphere_right
                }
            }

            when(refractiveError.re_distant_vision_sphere_left) {
                "select"-> {
                    binding.TextViewDistanceLeftSph.text=""
                }else-> {
                    binding.TextViewDistanceLeftSph.text=refractiveError.re_distant_vision_sphere_left
                }
            }

            when(refractiveError.re_distant_vision_cylinder_right) {
                "cylinder"-> {
                    binding.TextViewDistanceRightCyl.text=""
                }else-> {
                    binding.TextViewDistanceRightCyl.text=refractiveError.re_distant_vision_cylinder_right
                }
            }

            when(refractiveError.re_distant_vision_cylinder_left) {
                "cylinder"-> {
                    binding.TextViewDistanceLeftCyl.text=""
                }else-> {
                    binding.TextViewDistanceLeftCyl.text=refractiveError.re_distant_vision_cylinder_left
                }
            }

            when(refractiveError.re_distant_vision_axis_right) {
                "axis"-> {
                    binding.TextViewDistanceRightAxis.text=""
                }else-> {
                    binding.TextViewDistanceRightAxis.text=refractiveError.re_distant_vision_axis_right
                }
            }

            when(refractiveError.re_distant_vision_axis_left) {
                "axis"-> {
                    binding.TextViewDistanceLeftAxis.text=""
                }else-> {
                    binding.TextViewDistanceLeftAxis.text=refractiveError.re_distant_vision_axis_left
                }
            }

            when(refractiveError.re_pupipllary_distance) {
                "select"-> {
                    binding.TextViewPD.text=""
                }
                else-> {
                    binding.TextViewPD.text=refractiveError.re_pupipllary_distance
                }
            }

            when(refractiveError.re_bvd) {
                "select"-> {
                    binding.TextViewBVD.text=""
                }else-> {
                    binding.TextViewBVD.text=refractiveError.re_bvd
                }
            }

            when(refractiveError.re_prism_right) {
                "prism"-> {
                    binding.TextViewPrismRight.text=""
                }else-> {
                    binding.TextViewPrismRight.text=refractiveError.re_prism_right
                }
            }

            when(refractiveError.re_prism_left) {
                "prism"-> {
                    binding.TextViewPrismRight.text=""
                }else-> {
                    binding.TextViewPrismLeft.text=refractiveError.re_prism_left
                }
            }

            when(refractiveError.re_prism_unit_right) {
                "select"-> {
                    binding.TextViewBaseRight.text=""
                }else-> {
                    binding.TextViewBaseRight.text=refractiveError.re_prism_unit_right
                }
            }

            when(refractiveError.re_prism_unit_left) {
                "select"-> {
                    binding.TextViewBaseLeft.text=""
                }else-> {
                    binding.TextViewBaseLeft.text=refractiveError.re_prism_unit_left
                }
            }

            when(refractiveError.re_reading_addition_right_details) {
                "select"-> {
                    binding.TextViewAddAmountRightCyl.text=""
                }else-> {
                    binding.TextViewAddAmountRightCyl.text=refractiveError.re_reading_addition_right_details
                }
            }

            when(refractiveError.re_reading_addition_left_details) {
                "select"-> {
                    binding.TextViewAddAmountLeftCyl.text=""
                }else->
            {
                    binding.TextViewAddAmountLeftCyl.text=refractiveError.re_reading_addition_left_details
                }
            }
        } else { }

        val loginData=sessionManager.getLoginData()
        val decodedText=sessionManager.getPatientData()
        val patientData = gson.fromJson(decodedText, PatientData::class.java)
        val patientID = patientData.patientId
        val firstName = patientData.patientFname
        val lastName = patientData.patientLname
        val gender = patientData.patientGen
        val age = patientData.patientAge
        val ageUnit = patientData.AgeUnit
        val campID =patientData.camp_id
        val location = patientData.location
        val registrationNumber = patientData.RegNo
        val data=buildJsonString(firstName,lastName,patientID,registrationNumber,gender,age,campID,location,ageUnit)
        val base64Data = encodeToBase64(data)
        val bitmap = generateQRCode(base64Data)
        val imageView: ImageView = findViewById(R.id.ImageViewQRCode)
        imageView.setImageBitmap(bitmap)
        printOutJobName=patientData.patientId.toString()+"_"+patientData.patientFname+"_"+patientData.patientLname
        // Set up the PrinterJob
        val printManager = getSystemService(PRINT_SERVICE) as PrintManager
        val printAdapter = PrintableDocumentAdapter(findViewById(R.id.mainLayout),printOutJobName!!)
        binding.TextViewPatientName.text=patientData.patientFname+" "+patientData.patientLname
        binding.TextViewPatientID.text= patientData.patientId.toString()
        for (data in loginData!!) {
            binding.TextViewCampName.text=data.camp_name
        }

        printJob = printManager.print(printOutJobName!!, printAdapter, null)
    }
    fun buildJsonString(
        patientFname: String,
        patientLname: String,
        patientId: Int,
        RegNo: String,
        patientGen: String,
        patientAge: Int,
        camp_id: Int,
        location: String,
        AgeUnit: String
    ): String {
        return """{"patientFname":"$patientFname","patientLname":"$patientLname","patientId":$patientId,"RegNo":"$RegNo","patientGen":"$patientGen","patientAge":$patientAge,"camp_id":$camp_id,"location":"$location","AgeUnit":"$AgeUnit"}"""
    }

    fun encodeToBase64(data: String): String {
        return try {
            val byteData = data.toByteArray(Charsets.UTF_8)
            Base64.getEncoder().encodeToString(byteData)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            ""
        }
    }

    private fun generateQRCode(data: String): Bitmap? {
        return try {
            val multiFormatWriter = MultiFormatWriter()
            val bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 500, 500)
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.createBitmap(bitMatrix)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        printJob.cancel()
    }
}

class PrintableDocumentAdapter(private val rootView: View, private val printOutJobName: String) : PrintDocumentAdapter() {

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback,
        extras: Bundle?
    ) {
        val info = PrintDocumentInfo.Builder(printOutJobName)
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .build()

        callback.onLayoutFinished(info, true)
    }

    override fun onWrite(
        pages: Array<PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback
    ) {
        try {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(
                rootView.width,
                rootView.height,
                1
            ).create()

            val page = document.startPage(pageInfo)

            val canvas = page.canvas
            rootView.draw(canvas)

            document.finishPage(page)

            val output = FileOutputStream(destination.fileDescriptor)
            document.writeTo(output)

            document.close()
            output.close()

            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        } catch (e: IOException) {
            e.printStackTrace()
            callback.onWriteFailed(e.message)
        }
    }
}
