package ru.sample.duckapp


import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sample.duckapp.domain.Duck
import ru.sample.duckapp.infra.Api


class MainActivity : AppCompatActivity() {
    private val api = Api.ducksApi
    private lateinit var url : String
    private lateinit var duckImage : ImageView
    private lateinit var textField: EditText
    private lateinit var button: Button
    private lateinit var currText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        duckImage = findViewById<View>(R.id.image) as ImageView
        button = findViewById<View>(R.id.button) as Button
        textField = findViewById<View>(R.id.text_field) as EditText

        button.setOnClickListener {
            currText = textField.text.toString()
            if (currText == ""){
                getDuck()
            }
            else{
                getHttpDuckAsync("https://random-d.uk/api/v2/http/${currText.toInt()}")
                textField.text.clear()
            }
        }
    }

    private fun getDuck(){
        api.getRandomDuck().enqueue(object : Callback<Duck> {
            override fun onResponse(call: Call<Duck>, response: Response<Duck>) {
                if (response.isSuccessful){
                    val duck = response.body() as Duck
                    url = duck.url
                    Picasso.get().load(url).resize(400, 400).centerCrop().into(duckImage)
                }
            }
            override fun onFailure(call: Call<Duck>, t: Throwable){
                Picasso.get().load(R.drawable.sad_duck).resize(400, 0).centerCrop().into(duckImage)
                Toast.makeText(this@MainActivity, "Error while loading random duck", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Метод без асинхронной обработки
    private fun getHttpDuck(code: Int){
        url = "https://random-d.uk/api/v2/http/${code}"
        Picasso.get()
            .load(url)
            .error(R.drawable.sad_duck)
            .resize(400, 0).centerCrop()
            .into(duckImage, object: com.squareup.picasso.Callback{
                override fun onSuccess(){}
                override fun onError(e: Exception?){
                    Toast.makeText(this@MainActivity, "Wrong error code: $code", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun getHttpDuckAsync(url: String){
        api.getHttpDuckApi(url).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("RESPONSE", response.toString())
                if (response.isSuccessful){
                    if (response.body() != null){
                        val bmp = BitmapFactory.decodeStream(
                            response.body()!!.byteStream()
                        )
                        duckImage.setImageBitmap(bmp)
                    }
                }
                else{
                    Picasso.get().load(R.drawable.sad_duck).resize(400, 0).centerCrop().into(duckImage)
                    Toast.makeText(this@MainActivity, "Wrong error code", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable){
                Picasso.get().load(R.drawable.sad_duck).resize(400, 0).centerCrop().into(duckImage)
                Toast.makeText(this@MainActivity, "Wrong error code", Toast.LENGTH_LONG).show()
            }
        })
    }

}