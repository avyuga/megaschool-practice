package ru.sample.duckapp

//import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import ru.sample.duckapp.domain.Duck
import ru.sample.duckapp.infra.Api

class MainActivity : AppCompatActivity() {
    private val api = Api.ducksApi
    private var url = "https://i.imgur.com/tGbaZCY.jpg"
    private lateinit var ivBasicImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivBasicImage = findViewById<View>(R.id.image) as ImageView
        val button = findViewById<View>(R.id.button) as Button

        button.setOnClickListener {
            getDuck()

        }
    }

    private fun getDuck(){
        api.getRandomDuck().enqueue(object : Callback<Duck> {
            override fun onResponse(call: Call<Duck>, response: Response<Duck>) {
                if (response.isSuccessful){
                    val duck = response.body() as Duck
                    url = duck.url
                    Log.v("URL", url)
                    Picasso.get().load(url).into(ivBasicImage)
                }

            }
            override fun onFailure(call: Call<Duck>, t: Throwable){
            }
        })
    }
}