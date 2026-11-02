package id.web.assessment.man19jkt

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        
        // Konfigurasi WebView
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        // Set WebViewClient untuk menangani pembuatan/pemuatan halaman
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                
                // Injeksi otomatis skrip AI setelah halaman selesai dimuat
                // Memaksa isStudent dan isTeacher bernilai true agar fitur AI aktif untuk semua user
                val injectScript = """
                    (function() {
                        if (document.getElementById('slms-ai-injected')) return;
                        
                        window.isStudent = function() { return true; };
                        window.isTeacher = function() { return true; };
                        
                        const script = document.createElement('script');
                        script.id = 'slms-ai-injected';
                        script.src = 'https://man19jkt.assessment.web.id/school-lms-ai.js';
                        script.onload = function() {
                            if (typeof initLmsAI === 'function') {
                                initLmsAI();
                            }
                        };
                        document.body.appendChild(script);
                    })();
                """.trimIndent()

                view?.evaluateJavascript(injectScript, null)
            }
        }

        // Muat situs web LMS
        webView.loadUrl("https://man19jkt.assessment.web.id")
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
