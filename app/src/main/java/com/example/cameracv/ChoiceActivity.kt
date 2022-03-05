package com.example.cameracv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_choice.*

class ChoiceActivity : AppCompatActivity() {
    var gaussianFlag = false
    var medianFlag = false
    var boxFlag = false
    var bilateralFlag = false
    var bitwiseGrayFlag = false
    var binalizationFlag = false
    var detectionFlag = false

    var paramGKH: Double? = null //GaussianKernelheight
    var paramGKW:Double? = null  //GaussianKernelwidth
    var paramGSX:Double? = null  //GaussianSigmaX
    var paramGSY:Double? = null   //GaussianSigmaY
    var paramMKS:Double? = null  //MedianKernelsize(height&width)
    var paramBKH:Double? = null   //BoxKernelheight
    var paramBKW:Double? = null  //BoxKernelwidth
    var paramTB:Double? = null  //threshold of Binalization


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)

        //戻るバー
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        paramGKH = intent.getDoubleExtra("GaussianKernelheight",15.0)
        paramGKW = intent.getDoubleExtra("GaussianKernelwidth",15.0)
        paramGSX = intent.getDoubleExtra("GaussianSigmaX",1.0)
        paramGSY = intent.getDoubleExtra("GaussianSigmaY",1.0)
        paramMKS = intent.getDoubleExtra("MedianKernel",15.0)
        paramBKH = intent.getDoubleExtra("BoxKernelheight",15.0)
        paramBKW= intent.getDoubleExtra("BoxKernelwidth",15.0)
        paramTB= intent.getDoubleExtra("Binalization",128.0)



        gaussianFlag = intent.getBooleanExtra("gaussianFlag",false)
        medianFlag = intent.getBooleanExtra("medianFlag",false)
        boxFlag = intent.getBooleanExtra("boxFlag",false)
        bilateralFlag = intent.getBooleanExtra("bilateralFlag",false)
        bitwiseGrayFlag = intent.getBooleanExtra("bitwiseGrayFlag",false)
        binalizationFlag = intent.getBooleanExtra("binalizationFlag",false)
        detectionFlag = intent.getBooleanExtra("detectionFlag",false)

        //ラジオボタンの状態を取得(ガウシアンフィルタ)
        val RadioGroupGaussian = findViewById<RadioGroup>(R.id.RadioGroupGaussian)
        if (gaussianFlag){
            RadioGroupGaussian.check(R.id.radioButtonGaussianOn)
        } else {
            RadioGroupGaussian.check(R.id.radioButtonGaussianOff)
        }

        //ラジオボタンの状態を取得(メディアンフィルタ)
        val RadioGroupMedian = findViewById<RadioGroup>(R.id.RadioGroupMedian)
        if (medianFlag ){
            RadioGroupMedian.check(R.id.radioButtonMedianOn)
        } else {
            RadioGroupMedian.check(R.id.radioButtonMedianOff)
        }

        //ラジオボタンの状態を取得(ボックスフィルタ)
        val RadioGroupBox = findViewById<RadioGroup>(R.id.RadioGroupBox)
        if (boxFlag ){
            RadioGroupBox.check(R.id.radioButtonBoxOn)
        } else {
            RadioGroupBox.check(R.id.radioButtonBoxOff)
        }

        //ラジオボタンの状態を取得(バイラテラルフィルタ)
        val RadioGroupBilateral = findViewById<RadioGroup>(R.id.RadioGroupBilateral)
        if (bilateralFlag ){
            RadioGroupBilateral.check(R.id.radioButtonBilateralOn)
        } else {
            RadioGroupBilateral.check(R.id.radioButtonBilateralOff)
        }

        //ラジオボタンの状態を取得(バイナリゼーション)
        val RadioGroupBinalization = findViewById<RadioGroup>(R.id.RadioGroupBinalization)
        if (binalizationFlag ){
            RadioGroupBinalization.check(R.id.radioButtonBinalizationOn)
        } else {
            RadioGroupBinalization.check(R.id.radioButtonBinalizationOff)
        }

        //ラジオボタンの状態を取得(BitwizeGray)
        val RadioGroupBitwiseGray = findViewById<RadioGroup>(R.id.RadioGroupBitwiseGray)
        if (bitwiseGrayFlag ){
            RadioGroupBitwiseGray.check(R.id.radioButtonBitwiseGrayOn)
        } else {
            RadioGroupBitwiseGray.check(R.id.radioButtonBitwiseGrayOff)
        }

        //ラジオボタンの状態を取得(Detection)
        val RadioGroupDetection = findViewById<RadioGroup>(R.id.RadioGroupDetection)
        if (detectionFlag ){
            RadioGroupDetection.check(R.id.radioButtonDetectionOn)
        } else {
            RadioGroupDetection.check(R.id.radioButtonDetectionOff)
        }




    }

    override fun onResume() {
        super.onResume()


    }

    //リセットボタンクリック
    fun onResetClicked(view: View){

        //変数初期化
        gaussianFlag = false
        medianFlag = false
        boxFlag = false
        bilateralFlag = false
        bitwiseGrayFlag = false
        binalizationFlag = false
        detectionFlag = false

        //ラジオボタンすべてOFF
        RadioGroupGaussian.check(R.id.radioButtonGaussianOff)
        RadioGroupMedian.check(R.id.radioButtonMedianOff)
        RadioGroupBox.check(R.id.radioButtonBoxOff)
        RadioGroupBilateral.check(R.id.radioButtonBilateralOff)
        RadioGroupBinalization.check(R.id.radioButtonBinalizationOff)
        RadioGroupBitwiseGray.check(R.id.radioButtonBitwiseGrayOff)
        RadioGroupBitwiseGray.check(R.id.radioButtonBitwiseGrayOff)
        RadioGroupDetection.check(R.id.radioButtonDetectionOff)

    }

    //決定ボタンクリック
    fun onClicked(view: View){

        val intent = Intent(applicationContext,MainActivity::class.java)
        intent.putExtra("gaussianFlag",gaussianFlag)
        intent.putExtra("medianFlag",medianFlag)
        intent.putExtra("boxFlag",boxFlag)
        intent.putExtra(" bilateralFlag", bilateralFlag)
        intent.putExtra("bitwiseGrayFlag",bitwiseGrayFlag)
        intent.putExtra("binalizationFlag",binalizationFlag)
        intent.putExtra("detectionFlag",detectionFlag)

        intent.putExtra("GaussianKernelheight",paramGKH)
        intent.putExtra("GaussianKernelwidth",paramGKW)
        intent.putExtra("GaussianSigmaX",paramGSX)
        intent.putExtra("GaussianSigmaY",paramGSY)
        intent.putExtra("MedianKernel",paramMKS)
        intent.putExtra("BoxKernelheight",paramBKH)
        intent.putExtra("BoxKernelwidth",paramBKW)
        intent.putExtra("Binalization",paramTB)

        startActivity(intent)
    }

    //Gaussianのラジオボタン
    fun onRadiobuttonGaussianClicked(view: View){
        when(view.getId()){
            R.id.radioButtonGaussianOn -> {
                gaussianFlag = true
            }
            R.id.radioButtonGaussianOff ->{
                gaussianFlag = false
            }
        }
    }

    //Medianのラジオボタン
    fun onRadiobuttonMedianClicked(view: View){
        when(view.getId()){
            R.id.radioButtonMedianOn -> {
                medianFlag = true
            }
            R.id.radioButtonMedianOff ->{
                medianFlag = false
            }
        }
    }

    //Boxのラジオボタン
    fun onRadiobuttonBoxClicked(view: View){
        when(view.getId()){
            R.id.radioButtonBoxOn -> {
                boxFlag = true
            }
            R.id.radioButtonBoxOff ->{
                boxFlag = false
            }
        }
    }

    //Birateralのラジオボタン
    fun onRadiobuttonBilateralClicked(view: View){
        when(view.getId()){
            R.id.radioButtonBilateralOn -> {
                bilateralFlag = true
            }
            R.id.radioButtonBilateralOff ->{
                bilateralFlag = false
            }
        }
    }

    //Binalizationのラジオボタン
    fun onRadiobuttonBinalizationClicked(view: View){
        when(view.getId()){
            R.id.radioButtonBinalizationOn -> {
                binalizationFlag = true
            }
            R.id.radioButtonBinalizationOff ->{
                binalizationFlag = false
            }
        }
    }

    //BitwizeGrayのラジオボタン
    fun onRadiobuttonBitwizeGrayClicked(view: View){
        when(view.getId()){
            R.id.radioButtonBitwiseGrayOn -> {
                bitwiseGrayFlag = true
            }
            R.id.radioButtonBitwiseGrayOff ->{
                bitwiseGrayFlag = false
            }
        }
    }

    //Detectionのラジオボタン
    fun onRadiobuttonDetectionClicked(view: View){
        when(view.getId()){
            R.id.radioButtonDetectionOn -> {
                detectionFlag = true

            }
            R.id.radioButtonDetectionOff ->{
                detectionFlag = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //選択されたメニューが「戻る」の場合、アクティビティを終了
        if(item.itemId == android.R.id.home){
            finish()
        }

        //親クラスの同名メソッドを呼び出し、その戻り値を返却
        return super.onOptionsItemSelected(item)
    }
}