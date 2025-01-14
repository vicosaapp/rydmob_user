package com.sessentaservices.usuarios

import android.app.Application
import android.os.Build
import android.util.Log
import com.google.firebase.FirebaseApp
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.config.AGConnectServicesConfig

class YourApplication : Application() {
    companion object {
        private const val TAG = "YourApplication"
    }

    override fun onCreate() {
        super.onCreate()
        
        try {
            initializeFirebase()
            initializeHMS()
        } catch (e: Exception) {
            Log.e(TAG, "Erro na inicialização do app: ${e.message}", e)
        }
    }

    private fun initializeFirebase() {
        try {
            FirebaseApp.initializeApp(this)
            Log.d(TAG, "Firebase inicializado com sucesso")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao inicializar Firebase: ${e.message}", e)
        }
    }

    private fun initializeHMS() {
        if (!isHuaweiDevice()) {
            Log.d(TAG, "Dispositivo não é Huawei, pulando inicialização HMS")
            return
        }

        try {
            AGConnectServicesConfig.fromContext(applicationContext)
            AGConnectInstance.initialize(applicationContext)
            Log.d(TAG, "HMS inicializado com sucesso")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao inicializar HMS: ${e.message}", e)
        }
    }

    private fun isHuaweiDevice(): Boolean {
        val manufacturer = Build.MANUFACTURER.lowercase()
        return manufacturer.contains("huawei") || manufacturer.contains("honor")
    }
} 