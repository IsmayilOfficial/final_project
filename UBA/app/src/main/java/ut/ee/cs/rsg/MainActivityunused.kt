package ut.ee.cs.rsg

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View

import android.content.Intent
import android.os.Handler
import com.google.android.gms.common.util.CollectionUtils.listOf
import kotlinx.android.synthetic.main.activity_mainunused.*

class MainActivityunused : AppCompatActivity() {



    val rock = R.drawable.c3
    val paper = R.drawable.g6
    val scis = R.drawable.e5
    var wins:Int = 0
    var loser:Int = 0
    var draws:Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainunused)

        fun visi(){

            your_choise.visibility=View.VISIBLE
            app_choise.visibility=View.VISIBLE

        }



        buttonrock.setOnClickListener {
            visi()

            val list = listOf(paper,rock,scis)


            val rand = list.random()

            imageView2.setImageResource(R.drawable.c3)
            image_you_ch.setImageResource(rand)
            if (rand == rock){
                draws=draws+1
                drawed.text = "Draws: $draws"


            }
            else if(rand == paper){


                loser = loser+1
                lose.text = "Lose: $loser"

                imageView2.setImageResource(R.drawable.clipart89105)
                justplay.setImageResource(R.drawable.loser)
            }
            else if(rand == scis){


                wins=wins+1
                victory.text = "Wins: $wins"
                imageView2.setImageResource(R.drawable.clipart94960)
                justplay.setImageResource(R.drawable.vicotryismine)


            }
        }

        button2.setOnClickListener {

       visi()

            val list = listOf(paper,rock,scis)


            val rand = list.random()

            imageView2.setImageResource(R.drawable.g6)
            image_you_ch.setImageResource(rand)
            if (rand == paper){
                draws=draws+1
                drawed.text = "Draws: $draws"


            }
            else if(rand == scis){


                loser = loser+1
                lose.text = "Lose: $loser"

                imageView2.setImageResource(R.drawable.download)
                justplay.setImageResource(R.drawable.loser)
            }
            else if(rand == rock){


                wins=wins+1
                victory.text = "Wins: $wins"
                imageView2.setImageResource(R.drawable.rff6)
                justplay.setImageResource(R.drawable.vicotryismine)



            }
        }
        button3.setOnClickListener {
            visi()

            val list = listOf(paper,rock,scis)


            val rand = list.random()

            imageView2.setImageResource(R.drawable.e5)
            image_you_ch.setImageResource(rand)
            if (rand == scis){
                draws=draws+1
                drawed.text = "Draws: $draws"


            }
            else if(rand == rock){


                loser = loser+1
                lose.text = "Lose: $loser"

                imageView2.setImageResource(R.drawable.b2)
                justplay.setImageResource(R.drawable.loser)
            }
            else if(rand == paper){


                wins=wins+1
                victory.text = "Wins: $wins"
                imageView2.setImageResource(R.drawable.d4)
                justplay.setImageResource(R.drawable.vicotryismine)


            }
        }



        when {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    !== PackageManager.PERMISSION_GRANTED
            -> {
                ActivityCompat.requestPermissions( this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1 )






            }
        }


          Handler().postDelayed({



              val intent = Intent(this, SRUNDS::class.java)


              startActivity(intent)
          },10000)














    }














}
