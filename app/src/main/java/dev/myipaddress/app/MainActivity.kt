package dev.myipaddress.app


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri;
import java.net.URL;
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    lateinit var etIP: EditText
    lateinit var btnLookup: Button
    lateinit var pbLoading: ProgressBar
    lateinit var tvResults: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etIP = findViewById(R.id.etIP)
        btnLookup = findViewById(R.id.btnLookup)
        pbLoading = findViewById(R.id.pbLoading)
        tvResults = findViewById(R.id.tvResults)

        btnLookup.setOnClickListener {
            tvResults.text = ""
            pbLoading.visibility = View.VISIBLE
            lookup()
        }
        lookup()
    }

    private fun lookup() {
        val uriBuilder = Uri.parse("https://myipaddress.dev/").buildUpon().path("json")
        val ip = etIP.text.toString().trim()
        if (ip.isNotEmpty()) {
            uriBuilder.appendQueryParameter("ip", ip)
        }
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val request = JsonObjectRequest(Request.Method.GET, uriBuilder.toString(), null, { response ->
            pbLoading.visibility = View.GONE
            try {
                etIP.setText(response.getString("ip"))
                tvResults.text = response.toString(4);
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, { error ->
            pbLoading.visibility = View.GONE
            Toast.makeText(this@MainActivity, "Fail to get response", Toast.LENGTH_SHORT).show()
        })
        queue.add(request)
    }
}