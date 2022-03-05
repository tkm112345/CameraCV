package com.example.cameracv

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader
import org.opencv.core.*
import org.opencv.core.Core.bitwise_not
import org.opencv.imgproc.Imgproc

private const val DEBUG_TAG = "Gestures"

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() ,GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {
//class MainActivity : Activity() {
    // 変数
    var scale: Int = 0 //画面切替変数
    var gaussianFlag = false
    var medianFlag = false
    var boxFlag = false
    var bilateralFlag = false
    var bitwiseGrayFlag = false
    var binalizationFlag = false
    var detectionFlag = false

    var noparam:String = "No parameter"
    var GaussianKernelwidth: Double? = null
    var GaussianKernelheight:Double? = null
    var GaussianSigmaX:Double? = null
    var GaussianSigmaY :Double? = null
    var MedianKernel:Double? = null
    var BoxKernelwidth:Double? = null
    var BoxKernelheight:Double? = null
    var threshold:Double? = null

    val message_gaussian :String = "GF"
    val message_median :String = "MF"
    val message_box : String = "BXF"
    val message_bilateral :String = "BTF"
    val message_bitwize : String = "BW"
    val message_binalization:String = "BN"
    val message_detection:String = "DT"
    var message: String? = null


    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val version:TextView = findViewById(R.id.txtversion)
        if(OpenCVLoader.initDebug()){
            version.text = "OpenCV Version : " + OpenCVLoader.OPENCV_VERSION
            TextViewParameter.text = "No"

            gaussianFlag = intent.getBooleanExtra("gaussianFlag",false)
            medianFlag = intent.getBooleanExtra("medianFlag",false)
            boxFlag = intent.getBooleanExtra("boxFlag",false)
            bilateralFlag = intent.getBooleanExtra("bilateralFlag",false)
            bitwiseGrayFlag = intent.getBooleanExtra("bitwiseGrayFlag",false)
            binalizationFlag = intent.getBooleanExtra("binalizationFlag",false)
            detectionFlag = intent.getBooleanExtra("detectionFlag",false)

            GaussianKernelwidth = intent.getDoubleExtra("GaussianKernelwidth",15.0)
            GaussianKernelheight = intent.getDoubleExtra("GaussianKernelheight",15.0)
            GaussianSigmaX = intent.getDoubleExtra("GaussianSigmaX",1.0)
            GaussianSigmaY = intent.getDoubleExtra("GaussianSigmaY",1.0)
            MedianKernel = intent.getDoubleExtra("MedianKernel",15.0)
            BoxKernelwidth = intent.getDoubleExtra("BoxKernelwidth",15.0)
            BoxKernelheight = intent.getDoubleExtra("BoxKernelheight",15.0)
            threshold = intent.getDoubleExtra("Binalization",128.0)

        } else {
            version.text = "OpenCV Version: Not found."
            return
        }

    }

    override fun onRestart() {
        super.onRestart()
    }



    override fun onResume() {
        super.onResume()


        // OpenCVライブラリ読み込み
        initCamera()

    }

    private fun initCamera(){
        textMode.text = ""
        //リスナ設定
        camera_view.run {
            camera_view.setCvCameraViewListener(object: CameraBridgeViewBase.CvCameraViewListener2{

                override fun onCameraViewStarted(width:Int,height:Int){ }

                override fun onCameraViewStopped(){ }

                @SuppressLint("SetTextI18n")
                override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat? {
                    message = ""
                    //このメソッド内で画像処理(条件分岐)
                    var mat :Mat? = null

                    when(scale){
                        0 ->{ //RGBカラー
                            mat = inputFrame?.rgba()
                            textMode.text = "Mode : COLOR"
                            TextViewParameter.text = noparam
                        }
                        1->{ //Gray
                            mat = inputFrame?.gray()
                            textMode.text = "Mode : GRAY"
                            TextViewParameter.text = noparam
                        }
                        2->{ //RGBカラー + 反転
                            mat = inputFrame?.rgba()
                            bitwise_not(mat,mat)
                            textMode.text = "Mode : COLOR (REVERSE)"
                            TextViewParameter.text = noparam
                        }
                        3->{ //Gray + 反転
                            mat = inputFrame?.gray()
                            bitwise_not(mat,mat)
                            textMode.text = "Mode : GRAY(REVERSE)"
                            TextViewParameter.text =noparam
                        }
                        4->{ //二値化
                            mat = inputFrame?.rgba()
                            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY)
                            Imgproc.threshold(mat, mat, threshold!!, 255.0, Imgproc.THRESH_BINARY)  // 明るさが128を境に白と黒へ変換
                            textMode.text = "Mode : BINALIZATION"
                            TextViewParameter.text = "Binalization : " + threshold!!.toInt() + " / 255"
                        }
                        5->{ //ガウシアンフィルタ
                            mat = inputFrame?.rgba()
                            Imgproc.GaussianBlur(mat,mat, Size(GaussianKernelwidth!!,
                                GaussianKernelheight!!
                            ),GaussianSigmaX!!,GaussianSigmaY!!)
                            textMode.text = "Mode : GAUSSIAN FILTER"
                            TextViewParameter.text =
                                """width : $GaussianKernelwidth , height : $GaussianKernelheight , SigmaX : $GaussianSigmaX , SigmaY : $GaussianSigmaY"""
                        }
                        6->{ //メディアンフィルタ
                            mat = inputFrame?.rgba()
                            Imgproc.medianBlur(mat, mat, MedianKernel!!.toInt())
                            textMode.text = "Mode : MEDIAN FILTER"
                            TextViewParameter.text = "kernel size : $MedianKernel"
                        }
                        7->{ //バイラテラルフィルタ
                            try{
                                mat = inputFrame?.rgba()
                                Imgproc.bilateralFilter(mat, mat, 3, 0.0, 0.0)
                                textMode.text = "Mode : BILATERAL FILTER"
                            } catch( e: Exception){
                            } finally{
                            }
                        }
                        8->{ //ボックスフィルタ
                            mat = inputFrame?.rgba()
                            if (mat != null) {
                                Imgproc.boxFilter(mat, mat, mat.depth(),  Size(BoxKernelwidth!!, BoxKernelheight!!))
                                textMode.text = "Mode : BOX FILTER"
                                TextViewParameter.text =
                                    "width : $BoxKernelwidth , height : $BoxKernelheight"
                            }
                        }

                        9 -> { //検知モード
                            try{
                                TextViewParameter.text = "Multiple Mode"
                                //mat = inputFrame?.gray()
                                mat = inputFrame!!.rgba()
                                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY)
                                message = "Gray"

                                if(bitwiseGrayFlag){ //反転
                                    bitwise_not(mat,mat)
                                    message = "$message → $message_bitwize"

                                }
                                if(gaussianFlag){ //ガウシアンフィルタ
                                    Imgproc.GaussianBlur(mat,mat, Size(GaussianKernelwidth!!,GaussianKernelheight!!),GaussianSigmaX!!,GaussianSigmaY!!)
                                    message = "$message　→ $message_gaussian"
                                }
                                if(medianFlag){  //メディアンフィルタ
                                    Imgproc.medianBlur(mat, mat, MedianKernel!!.toInt())
                                    message = "$message　→ $message_median"

                                }
                                if(boxFlag){  //ボックスフィルタ
                                    if (mat != null) {
                                        Imgproc.boxFilter(mat, mat, mat.depth(),  Size(BoxKernelwidth!!, BoxKernelheight!!))
                                        message = "$message　→ $message_box"
                                    }
                                }
                                if(bilateralFlag){ //バイラテラルフィルタ
                                    Imgproc.bilateralFilter(mat, mat, 3, 0.0, 0.0)
                                    message = "$message　→ $message_bilateral"
                                }
                                if(binalizationFlag){ //二値化
                                    Imgproc.threshold(mat, mat, threshold!!, 255.0, Imgproc.THRESH_BINARY)  // 明るさが128を境に白と黒へ変換
                                    message = "$message　→ $message_binalization"

                                }
                                if(detectionFlag){  //検知モード
                                    if(!binalizationFlag){ //二値化：閾値を自動で決定
                                        Imgproc.threshold(mat, mat, threshold!!, 255.0, Imgproc.THRESH_OTSU)
                                        message = "$message　→ $message_binalization"
                                    }
                                    message = "$message　→ $message_detection"

                                    //輪郭の座標を取得
                                    val contours = getContour(mat!!)

                                    mat =inputFrame?.rgba() //カラーで取り直し

                                    //輪郭画像を合成
                                    Imgproc.drawContours(
                                        mat,
                                        contours,
                                        -1,
                                        Scalar(0.0, 255.0, 255.0),
                                        5
                                    )

                                }
                                textMode.text = message

                            } catch(e:Exception){
                                println("Exception : $e")
                                textMode.text = "Combination: Error"
                            }finally{
                            }
                        }
                        else->{ //例外
                            inputFrame?.rgba()
                            textMode.text = "Mode : COLOR ( exception )"
                        }
                    }
                    return mat
                }
            })

            //プレビューを有効にする
            camera_view.setCameraPermissionGranted()
            camera_view.enableView()
        }

    }

    private fun getContour(mat: Mat): List<MatOfPoint?>? {
        val contour = ArrayList<MatOfPoint>()

        // 二値画像中の輪郭を検出
        val tmp_contours: List<MatOfPoint> = ArrayList<MatOfPoint>()
        val hierarchy = Mat.zeros(Size(15.0, 15.0), CvType.CV_8UC1)
        Imgproc.findContours(
            mat,
            tmp_contours,
            hierarchy,
            Imgproc.RETR_TREE,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        for (i in 0 until tmp_contours.size) {

            if (Imgproc.contourArea(tmp_contours[i]) < mat.size().area() / (1000 * 1)) {
                // サイズが小さいエリアは無視
                continue
            }

            val ptmat =  MatOfPoint(tmp_contours[i])
            val ptmat2 =  MatOfPoint2f()
            ptmat.convertTo(ptmat2, CvType.CV_32FC2)

            val approx = MatOfPoint2f()
            val approxf1 = MatOfPoint()

            // 輪郭線の周囲長を取得
            val arclen = Imgproc.arcLength(ptmat2, true)
            // 直線近似
            Imgproc.approxPolyDP(ptmat2, approx, 0.02 * arclen, true)
            approx.convertTo(approxf1, CvType.CV_32S)
            if (approxf1.size().area() !== 4.0) {
                // 四角形以外は無視
                continue
            }

            // 輪郭情報を登録
            contour.add(approxf1)
        }
        return contour
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //オプションメニュー用xmlファイルをインフレクト
        menuInflater.inflate(R.menu.menu_options_menu_list,menu)
        //親クラスの同名メソッドを呼び出し、その戻り値を返却
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        val intentHelp = Intent(applicationContext,HelpActivity::class.java)
        val intentParam = Intent(applicationContext,ParameterActivity::class.java)
        val intentChoice = Intent(applicationContext, ChoiceActivity::class.java)

        //変数を登録
        intentParam.putExtra("GaussianKernelheight",GaussianKernelheight)
        intentParam.putExtra("GaussianKernelwidth",GaussianKernelwidth)
        intentParam.putExtra("GaussianSigmaX",GaussianSigmaX)
        intentParam.putExtra("GaussianSigmaY",GaussianSigmaY)
        intentParam.putExtra("MedianKernel",MedianKernel)
        intentParam.putExtra("BoxKernelheight",BoxKernelheight)
        intentParam.putExtra("BoxKernelwidth",BoxKernelwidth)
        intentParam.putExtra("Binalization",threshold)
        intentParam.putExtra("gaussianFlag",gaussianFlag)
        intentParam.putExtra("medianFlag",medianFlag)
        intentParam.putExtra("boxFlag",boxFlag)
        intentParam.putExtra("bilateralFlag",bilateralFlag)
        intentParam.putExtra("bitwiseGrayFlag",bitwiseGrayFlag)
        intentParam.putExtra("binalizationFlag", binalizationFlag)
        intentParam.putExtra("detectionFlag",detectionFlag)

        //変数を登録
        intentChoice.putExtra("gaussianFlag",gaussianFlag)
        intentChoice.putExtra("medianFlag",medianFlag)
        intentChoice.putExtra("boxFlag",boxFlag)
        intentChoice.putExtra("bilateralFlag",bilateralFlag)
        intentChoice.putExtra("bitwiseGrayFlag",bitwiseGrayFlag)
        intentChoice.putExtra("binalizationFlag", binalizationFlag)
        intentChoice.putExtra("detectionFlag",detectionFlag)
        intentChoice.putExtra("GaussianKernelheight",GaussianKernelheight)
        intentChoice.putExtra("GaussianKernelwidth",GaussianKernelwidth)
        intentChoice.putExtra("GaussianSigmaX",GaussianSigmaX)
        intentChoice.putExtra("GaussianSigmaY",GaussianSigmaY)
        intentChoice.putExtra("MedianKernel",MedianKernel)
        intentChoice.putExtra("BoxKernelheight",BoxKernelheight)
        intentChoice.putExtra("BoxKernelwidth",BoxKernelwidth)
        intentChoice.putExtra("Binalization",threshold)


        when(item.itemId){
            //Color を選択した場合
            R.id.menuListOptionColor ->
                scale = 0

            //Gray を選択した場合
            R.id.menuListOptionGray ->
                scale = 1

            //Color Bitwise を選択した場合
            //R.id.menuListOptionColorBitwise ->
            //    scale = 2

            //Gray Bitwise を選択した場合
            R.id.menuListOptionGrayBitwise ->
                scale = 3

            //Binalization を選択した場合
            R.id.menuListOptionBinalization ->
                scale = 4

            //GaussianBlur を選択した場合
            R.id.menuListOptionGaussianBlur ->
                scale = 5

            //MedianBlur を選択した場合
            R.id.menuListOptionMedianBlur ->
                scale = 6

            //Bilateral Filter を選択した場合
            R.id.menuListOptionBilateralFilter ->
                scale = 7

            //Box Filter を選択した場合
            R.id.menuListOptionBoxFilter ->
                scale = 8

            //検知モードを選択した場合
            R.id.menuListOptionDetection ->
                scale = 9

            //Parameter Setting を選択した場合
            R.id.menuListOptionParameter ->

                // 変数設定の画面を表示
                startActivity(intentParam)

            R.id.menuListOptionCombination ->

                //検知モードの設定画面を表示
                startActivity(intentChoice)


            R.id.menuListOptionHelp ->
                // ヘルプの画面を表示
                startActivity(intentHelp)

        }

        return super.onOptionsItemSelected(item)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onShowPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDown(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onLongPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

}