package com.luanasilva.aulathreadscoroutines

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.luanasilva.aulathreadscoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var pararThread = false
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnAbrirTela.setOnClickListener {
            finish()
        }

        binding.btnParar.setOnClickListener {
            job?.cancel()
            binding.btnIniciar.text = "Reiniciar execução"
            binding.btnIniciar.isEnabled = true
        }


        binding.btnIniciar.setOnClickListener {

            /*  job = CoroutineScope(Dispatchers.IO).launch {*//*

                val tempo = measureTimeMillis {


                    //Mais uma coroutine. Async roda assincrono
                    val resultado1 = async {tarefa1()} // Thread Maria
                    //Outra coroutina funcionando em simultâneo
                    val resultado2 = async {tarefa2()} // Thread Pedro

                    withContext(Dispatchers.Main) {
                        binding.btnIniciar.text = "${resultado1.await()}"
                        binding.btnParar.text = "${resultado2.await()}"

                    }

                    //Await é para só quando a coroutine finaliza a execução
                    Log.i("info_coroutine", "resultado1: ${resultado1.await()}")
                    Log.i("info_coroutine", "resultado1: ${resultado2.await()}")
                }



                Log.i("info_coroutine", "Tempo: $tempo")
            }*//*
        }*/

            //CoroutineScope(Dispatchers.Main).launch{}
            // CoroutineScope(Dispatchers.IO).launch{}
            //MainScope.launch{}
            //GlobalScope.launch{}

            //lifecyclescope é do jetpack
            lifecycleScope.launch {
                repeat(15) { indice ->
                    binding.btnIniciar.text = "Executando $indice"
                    Log.i("info_coroutine", "Executando: $indice Thread:{${Thread.currentThread().name}}")
                    delay(1000L)
                }
            }
        }
    }

    private suspend fun tarefa1():String {
        repeat(3) {indice ->
            Log.i("info_coroutine", "Executando tarefa 1: $indice Thread:{${Thread.currentThread().name}}")
            delay(1000)
        }
        return "Executou tarefa 1"
    }

    private suspend fun tarefa2():String {
        repeat(3) {indice ->
            Log.i("info_coroutine", "Executando tarefa 2: $indice Thread:{${Thread.currentThread().name}}")
            delay(1000)
        }
        return "Executou tarefa 2"
    }

    private suspend fun executar() {
        repeat(15) { indice ->
            Log.i("info_coroutine", "Executando: $indice T: ${Thread.currentThread().name}")

            //Isso será executado no contexto da Thread Principal
            withContext(Dispatchers.Main) {
                binding.btnIniciar.text = "Executando: $indice T: ${Thread.currentThread().name}"
                binding.btnIniciar.isEnabled = false
            }
            delay(1000)
        }
    }

    private suspend fun dadosUsuario() {
        val usuario = recuperarUsuarioLogado()
        val postagens = recuperarPostagensPeloID(usuario.id)
    }

    private suspend fun recuperarPostagensPeloID(idUsuario: Int): List<String> {
        delay(2000)
        return listOf(
            "Viagem Europa",
            "Estudando Android",
            "Jantando restaurante"
        )
    }

    //TODA função suspend só pode ser usado dentro de uma Coroutine
    private suspend fun recuperarUsuarioLogado() :Usuario{
        delay(2000)
        return Usuario(1011, "Luana Silva")
    }

    inner class MinhaRunnable: Runnable {
        override fun run() {
            repeat(30) { indice ->
                if (pararThread) {
                    pararThread = false
                    return
                }
                Log.i(
                    "info_thread",
                    "MinhaThread: $indice CurrentThread: ${Thread.currentThread().name}"
                )
                runOnUiThread {
                    binding.btnIniciar.text =
                        "Executando: $indice Thread=: ${Thread.currentThread().name}"
                    binding.btnIniciar.isEnabled = false
                    if (indice == 29) {
                        binding.btnIniciar.text = "Reiniciar execução"
                        binding.btnIniciar.isEnabled = true
                    }
                }

                Thread.sleep(1000)
            }
        }

    }

}




