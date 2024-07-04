package com.mobatia.bisad.activity.school_trips

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.github.barteksc.pdfviewer.PDFView
import com.google.gson.JsonObject
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.school_trips.model.TripInvoiceResponseModel
import com.mobatia.bisad.constants.ProgressBarDialog
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.ApiClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class TripInvoiceViewActivity : AppCompatActivity() {
    lateinit var back: ImageView
    lateinit var downloadpdf: ImageView
    lateinit var context: Context
    lateinit var pdfviewer: PDFView
    var urltoshow: String = ""
    var title: String = ""
    private val STORAGE_PERMISSION_CODE: Int = 1000
    lateinit var progressDialogP: ProgressBarDialog
    lateinit var pdfprogress: ProgressBar
    var invoiceNumber = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_invoice_view)
        context = this
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        invoiceNumber = intent.getStringExtra("invoice_number").toString()
//        title = intent.getStringExtra("pdf_title").toString()
        back = findViewById(R.id.back)
        progressDialogP = ProgressBarDialog(context)
        getInvoiceReciept(invoiceNumber)
        downloadpdf = findViewById(R.id.downloadpdf)
        pdfviewer = findViewById(R.id.pdfview)
        pdfprogress = findViewById(R.id.pdfprogress)
       // Log.e("url", urltoshow)
        PRDownloader.initialize(applicationContext)

        back.setOnClickListener {
            finish()
        }
        downloadpdf.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE
                    )
                }

                val fileWithinMyDir = File(getFilepath("$title.pdf"))

                if (fileWithinMyDir.exists()) {
                    fileWithinMyDir.delete()
                    startdownloading()
                    onDownloadComplete()
                } else {
                    startdownloading()
                    onDownloadComplete()
                }
            }
        }

        invoiceNumber = intent.getStringExtra("invoice_number").toString()
    }

    fun onDownloadComplete() {
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                pdfprogress.visibility = View.GONE
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show()

            }

        }
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun startdownloading() {
        val request = DownloadManager.Request(Uri.parse(urltoshow))   //URL = URL to download
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The file is downloading...")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$title.pdf")
        pdfprogress.visibility = View.VISIBLE
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    private fun getFilepath(filename: String): String? {
        return File(
            Environment.getExternalStorageDirectory().absolutePath,
            "/Download/$filename"
        ).path
    }

    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        PRDownloader.download(
            url,
            dirPath,
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val downloadedFile = File(dirPath, fileName)

                    showPdfFromFile(downloadedFile)
                }

                override fun onError(error: com.downloader.Error?) {

                }

            })
    }

    private fun showPdfFromFile(file: File) {
        pdfviewer.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
            }
            .load()
        pdfprogress.visibility = View.GONE

    }


    private fun getInvoiceReciept(invoiceNumber: String) {
        progressDialogP.show()
        val paramObject = JsonObject()
        paramObject.addProperty("invoice_no", invoiceNumber)
        val call: Call<TripInvoiceResponseModel> = ApiClient.getClient.tripReciept(
            "Bearer " + PreferenceData().getaccesstoken(context), paramObject
        )
        call.enqueue(object : Callback<TripInvoiceResponseModel> {
            override fun onResponse(
                call: Call<TripInvoiceResponseModel>, response: Response<TripInvoiceResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.status == 100) {

                    if (response.body()!!.data.receiptUrl.isNotEmpty()) {
                        val fileName = "myFile.pdf"

                        urltoshow = response.body()!!.data.receiptUrl
                        downloadPdfFromInternet(
                            urltoshow,
                            getRootDirPath(context),
                            fileName
                        )
                    }

                } else {
                }

            }

            override fun onFailure(call: Call<TripInvoiceResponseModel>, t: Throwable) {
                progressDialogP.dismiss()

            }
        })

    }


}