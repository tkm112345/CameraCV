package com.example.cameracv

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_parameter.*

class ParameterActivity : AppCompatActivity() {
    var paramGKH: Double? = null //GaussianKernelheight
    var paramGKW:Double? = null  //GaussianKernelwidth
    var paramGSX:Double? = null  //GaussianSigmaX
    var paramGSY:Double? = null   //GaussianSigmaY
    var paramMKS:Double? = null  //MedianKernelsize(height&width)
    var paramBKH:Double? = null   //BoxKernelheight
    var paramBKW:Double? = null  //BoxKernelwidth
    var paramTB:Double? = null  //threshold of Binalization


    var gaussianFlag = false
    var medianFlag = false
    var boxFlag = false
    var bilateralFlag = false
    var bitwiseGrayFlag = false
    var binalizationFlag = false
    var detectionFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameter)

        //戻るバー
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //seekbar 初期値
        paramGKH = intent.getDoubleExtra("GaussianKernelheight",15.0)
        paramGKW = intent.getDoubleExtra("GaussianKernelwidth",15.0)
        paramGSX = intent.getDoubleExtra("GaussianSigmaX",1.0)
        paramGSY = intent.getDoubleExtra("GaussianSigmaY",1.0)
        paramMKS = intent.getDoubleExtra("MedianKernel",15.0)
        paramBKH = intent.getDoubleExtra("BoxKernelheight",15.0)
        paramBKW = intent.getDoubleExtra("BoxKernelwidth",15.0)
        paramTB = intent.getDoubleExtra("Binalization",128.0)


        seekBarGaussianKW.progress = ((paramGKW!!-1) /2).toInt()
        seekBarGaussianKH.progress = ((paramGKH!! -1 ) /2).toInt()
        seekBarGaussianSX.progress = paramGSX!!.toInt()
        seekBarGaussianSY.progress = paramGSY!!.toInt()
        seekBarMedian.progress = ((paramMKS!! - 1) / 2).toInt()
        seekBarBoxKW.progress = ((paramBKW!! - 1) / 2).toInt()
        seekBarBoxKH.progress = ((paramBKH!! - 1) / 2).toInt()
        seekBarBinalization.progress = paramTB!!.toInt()

        //seekbar 最大値
        seekBarGaussianKW.max = 20
        seekBarGaussianKH.max = 20
        seekBarMedian.max = 20
        seekBarBoxKW.max = 20
        seekBarBoxKH.max = 20
        seekBarBinalization.max = 255

        //表記
        GaussianKsizeW.text  = paramGKW!!.toInt().toString()
        GaussianKsizeH.text  = paramGKH!!.toInt().toString()
        GaussianSigmaX.text = paramGSX!!.toString()
        GaussianSigmaY.text = paramGSY!!.toString()
        MedianKsize.text = paramMKS!!.toString()
        BoxKsizeW.text = paramBKW!!.toString()
        BoxKsizeH.text = paramBKH!!.toString()
        threshold.text = paramTB!!.toInt().toString() + " / " + seekBarBinalization.max

        //パラメータ
        gaussianFlag = intent.getBooleanExtra("gaussianFlag",false)
        medianFlag = intent.getBooleanExtra("medianFlag",false)
        boxFlag = intent.getBooleanExtra("boxFlag",false)
        bilateralFlag = intent.getBooleanExtra("bilateralFlag",false)
        bitwiseGrayFlag = intent.getBooleanExtra("bitwiseGrayFlag",false)
        binalizationFlag = intent.getBooleanExtra("binalizationFlag",false)
        detectionFlag = intent.getBooleanExtra("detectionFlag",false)



        //シークバーの操作処理
        seekBarGaussianKH.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val str = ((progress * 2) + 1).toString()
                GaussianKsizeH.text = str
                paramGKH = ((progress * 2) + 1).toDouble()
            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        seekBarGaussianKW.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val str = ((progress * 2) + 1).toString()
                GaussianKsizeW.text = str
                paramGKW = ((progress * 2) + 1).toDouble()
                //MainActivity().GaussianKernelwidth = ((progress * 2) + 1).toDouble()
            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        seekBarGaussianSX.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val str = progress.toDouble().toString()
                GaussianSigmaX.text = str
                paramGSX = progress.toDouble()
            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        seekBarGaussianSY.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val str = progress.toDouble().toString()
                GaussianSigmaY.text = str
                paramGSY = progress.toDouble()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        seekBarMedian.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val str = progress.toDouble().toString()
                MedianKsize.text = str
                paramMKS = progress.toDouble()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        seekBarBoxKH.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val str = progress.toDouble().toString()
                BoxKsizeH.text = str
                paramBKH = progress.toDouble()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        seekBarBoxKW.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val str = progress.toDouble().toString()
                BoxKsizeW.text = str
                paramBKW = progress.toDouble()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        seekBarBinalization.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val str = progress.toString()
                threshold.text = str + " / " + seekBarBinalization.max
                paramTB = progress.toDouble()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //選択されたメニューが「戻る」の場合、アクティビティを終了
        if(item.itemId == android.R.id.home){
            finish()
        }

        //親クラスの同名メソッドを呼び出し、その戻り値を返却
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    fun onResetClick(view:View){

        //値の初期化
        paramGKH = 15.0
        paramGKW =15.0
        paramGSX = 1.0
        paramGSY = 1.0
        paramMKS = 15.0
        paramBKH = 15.0
        paramBKW = 15.0
        paramTB = 128.0

        //プログレスバーの初期化
        seekBarGaussianKW.progress = ((paramGKW!!-1) /2).toInt()
        seekBarGaussianKH.progress = ((paramGKH!! -1 ) /2).toInt()
        seekBarGaussianSX.progress = paramGSX!!.toInt()
        seekBarGaussianSY.progress = paramGSY!!.toInt()
        seekBarMedian.progress = ((paramMKS!! - 1) / 2).toInt()
        seekBarBoxKW.progress = ((paramBKW!! - 1) / 2).toInt()
        seekBarBoxKH.progress = ((paramBKH!! - 1) / 2).toInt()
        seekBarBinalization.progress = paramTB!!.toInt()

        //数値表記の初期化
        GaussianKsizeH.text = paramGKH.toString()
        GaussianKsizeW.text = paramGKW.toString()
        GaussianSigmaX.text = paramGSX.toString()
        GaussianSigmaY.text = paramGSY.toString()
        MedianKsize.text = paramMKS.toString()
        BoxKsizeH.text = paramBKH.toString()
        BoxKsizeW.text = paramBKW.toString()
        threshold.text = paramTB.toString() + " / " + seekBarBinalization.max
    }

    fun onButtonClick(view: View) {
        //インテントオブジェクトを用意
        val intent = Intent(applicationContext,MainActivity::class.java)

        intent.putExtra("GaussianKernelheight",paramGKH)
        intent.putExtra("GaussianKernelwidth",paramGKW)
        intent.putExtra("GaussianSigmaX",paramGSX)
        intent.putExtra("GaussianSigmaY",paramGSY)
        intent.putExtra("MedianKernel",paramMKS)
        intent.putExtra("BoxKernelheight",paramBKH)
        intent.putExtra("BoxKernelwidth",paramBKW)
        intent.putExtra("Binalization",paramTB)

        intent.putExtra("gaussianFlag",gaussianFlag)
        intent.putExtra("medianFlag",medianFlag)
        intent.putExtra("boxFlag",boxFlag)
        intent.putExtra(" bilateralFlag", bilateralFlag)
        intent.putExtra("bitwiseGrayFlag",bitwiseGrayFlag)
        intent.putExtra("binalizationFlag",binalizationFlag)
        intent.putExtra("detectionFlag",detectionFlag)

        //アクティビティを起動
        startActivity(intent)
    }
}