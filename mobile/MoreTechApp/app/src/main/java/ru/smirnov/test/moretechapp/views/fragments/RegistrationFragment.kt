package ru.smirnov.test.moretechapp.views.fragments

import android.R
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.smirnov.test.moretechapp.databinding.FragmentRegistrationBinding
import ru.smirnov.test.moretechapp.network.services.AuthorisationService
import ru.smirnov.test.moretechapp.network.services.LoginRequest
import ru.smirnov.test.moretechapp.network.services.RegisterResponse
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : DialogFragment() {

    private var _binding: FragmentRegistrationBinding? = null

    private val binding get() = _binding!!

    private lateinit var progress: CircularProgressIndicator
    private lateinit var status: TextView

    private val errorText = "Ошибка запроса. Проверьте соединение с интернетом или повторите позже"

    @Inject
    lateinit var authorisationService: AuthorisationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,
            R.style.Theme_Material_Light_NoActionBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        progress = binding.progressIndicatorAuth
        status = binding.registerStatus

        binding.closeDialogAuth.setOnClickListener {
            dismiss()
        }

        binding.continueRegister.setOnClickListener {
            val login = binding.userLogin.text.toString()
            val pass = binding.userPassword.text.toString()
            val passCheck = binding.userPasswordCheck.text.toString()

            if (pass != passCheck) {
                binding.userPasswordInputLayoutCheck.error = "Пароли не совпадают"
                return@setOnClickListener
            }

            status.text = ""
            binding.userPasswordInputLayoutCheck.error = null
            progress.visibility = View.VISIBLE
            register(login, pass)
        }

        return binding.root
    }

    private fun register(login: String, pass: String) {
        val registerRequest = authorisationService.register(LoginRequest(login, pass))
        registerRequest.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {
                if (!response.isSuccessful) {
                    val msg = if (response.code() == 400) "Логин занят" else ""
                    onFailure(call, InternalError(msg))
                    return
                }

                val registerResponse = response.body()

                if (registerResponse == null) {
                    onFailure(call, InternalError())
                    return
                }

                progress.visibility = View.GONE
                status.text = "Успешно зарегистрирован"
                status.setTextColor(Color.BLACK)

                Handler(Looper.getMainLooper()).postDelayed({dismiss()}, 2000)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.e()
                progress.visibility = View.GONE
                status.text = if (t.message == "") errorText else t.message
                status.setTextColor(Color.parseColor("#FF7F7F"))
            }

        })
    }
}