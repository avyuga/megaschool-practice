package ru.sample.duckapp

//import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import ru.sample.duckapp.domain.Duck
import ru.sample.duckapp.infra.Api

class MainActivity : AppCompatActivity() {
    private val api = Api.ducksApi
    private lateinit var url : String
    private lateinit var DuckImage : ImageView
    private lateinit var TextField: EditText
    private lateinit var currText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DuckImage = findViewById<View>(R.id.image) as ImageView
        val button = findViewById<View>(R.id.button) as Button
        TextField = findViewById<View>(R.id.text_field) as EditText

        button.setOnClickListener {

            currText = TextField.text.toString()
            if (currText == ""){
                getDuck()
            }
            else{
                getHttpDuck(currText.toInt())
            }
        }
    }

    private fun getDuck(){
        api.getRandomDuck().enqueue(object : Callback<Duck> {
            override fun onResponse(call: Call<Duck>, response: Response<Duck>) {
                if (response.isSuccessful){
                    val duck = response.body() as Duck
                    url = duck.url
                    Picasso.get().load(url).resize(400, 0).centerCrop().into(DuckImage)

                }

            }
            override fun onFailure(call: Call<Duck>, t: Throwable){
                Picasso.get().load(R.drawable.sad_duck).resize(400, 0).centerCrop().into(DuckImage)
                Toast.makeText(this@MainActivity, "Error while loading random duck", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getHttpDuck(code: Int){
        url = "https://random-d.uk/api/v2/http/${code}"
        Picasso.get()
            .load(url)
            .error(R.drawable.sad_duck)
            .resize(400, 0).centerCrop()
            .into(DuckImage, object: com.squareup.picasso.Callback{
                override fun onSuccess(){}
                override fun onError(e: Exception?){
                    Toast.makeText(this@MainActivity, "Wrong error code: $code", Toast.LENGTH_LONG).show()
                }
            })
    }
}