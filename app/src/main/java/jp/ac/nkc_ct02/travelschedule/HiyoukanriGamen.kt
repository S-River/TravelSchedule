package jp.ac.nkc_ct02.travelschedule

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_hiyoukanrigamen.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.floatingAddButton
import kotlinx.android.synthetic.main.activity_main.readBtn1
import kotlinx.android.synthetic.main.activity_main.saveBtn1
import kotlinx.android.synthetic.main.activity_motimonogamen.*
import java.lang.Integer.parseInt

class HiyoukanriGamen : AppCompatActivity() {
    // スピナー選択肢
    //private val spinnerItems = arrayOf("1","2","3","4","5","6","7","8","9","10")
    private val spinnerItems = arrayListOf<String>("1","2","3","4","5","6","7","8","9","10")

    private var spinnerValue = 1
    private var sumBox = 0
    private var divBox = 0
    private var val1 = 0
    private var val2 = 0
    private var val3 = 0
    private var val4 = 0
    private var val5 = 0
    private var val6 = 0
    private var val7 = 0
    private var val8 = 0
    private var val9 = 0
    private var val10 = 0

    var layoutMax = 10
    var layoutVisibilityFlg = 1 //textviewをどこまで追加したか記録


    interface CustomTextWatcher: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiyoukanrigamen)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        //デバッグ用preference削除 使い終わったらコメントアウト
//        pref.edit{
//            clear()
//        }

        for (i in 11..100){
            spinnerItems.add(i.toString())
        }

        //前回までに表示したtextview等を再表示
        setPref()

        //作成遷移
        hkoumokuButton.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        textView13.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        //スケジュール遷移
        hkoumokuButton2.setOnClickListener{
            val intent = Intent(applicationContext, RyokouSakuei::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        textView14.setOnClickListener {
            val intent = Intent(applicationContext, RyokouSakuei::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        //持ち物遷移
        hkoumokuButton3.setOnClickListener{
            val intent = Intent(applicationContext, MotimonoGamen::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        textView15.setOnClickListener {
            val intent = Intent(applicationContext, MotimonoGamen::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        val spinner = findViewById<Spinner>(R.id.spinner)

        // ArrayAdapter
        val adapter = ArrayAdapter(applicationContext,
            android.R.layout.simple_spinner_item, spinnerItems)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // spinner に adapter をセット
        // Kotlin Android Extensions
        spinner.adapter = adapter

        // リスナーを登録
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            //　アイテムが選択された時
            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?, position: Int, id: Long) {
                val spinnerParent = parent as Spinner
                val item = spinnerParent.selectedItem as String
                // Kotlin Android Extensions
                spinnerValue = Integer.parseInt(item)
                divBox = sumBox/spinnerValue
                divValue.text = divBox.toString()
                if(sumBox%spinnerValue>0){
                    findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.VISIBLE
                    amariView.text = (sumBox%spinnerValue).toString()
                }else{findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.GONE}
            }

            //　アイテムが選択されなかった
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

        //保存
        saveBtn1.setOnClickListener (View.OnClickListener {
            writePref()
        })

        //読み込み
        readBtn1.setOnClickListener (View.OnClickListener {
            setPref()
            spinnerValue = pref.getInt("HIYOU_SPINNER_VALUE",1)
            spinner.setSelection(pref.getInt("HIYOU_SPINNER_VALUE",0)-1)
        })

        spinner.setSelection(pref.getInt("HIYOU_SPINNER_VALUE",0)-1)

        //削除ボタン群
        for(i in 1..layoutMax){
            findViewById<View>(resources.getIdentifier("hiyou_removeBtn$i", "id", packageName)).setOnClickListener { deleteFun(i) }
        }

        //追加ボタン
        floatingAddButton.setOnClickListener {

            //現状Min-Max行目の範囲で操作
            for (i in 1..layoutMax) {
                //idを取得し処理
                if(findViewById<View>(resources.getIdentifier("layout$i", "id", packageName)).visibility == View.GONE){

                    findViewById<View>(resources.getIdentifier("layout$i", "id", packageName)).visibility = View.VISIBLE
                    //どこまで表示したか記録
                    layoutVisibilityFlg = i
                    if(i==layoutMax) floatingAddButton.visibility= View.GONE //Max行目を出した時追加ボタンを非表示
                    break
                }
            }

            pref.edit{
                putInt("HIYOU_VISIBILITY_STAT",layoutVisibilityFlg)
            }
        }

        hiyou_valueView1.addTextChangedListener(object: CustomTextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(isNumber(hiyou_valueView1.text.toString())){
                    val1 = Integer.parseInt(hiyou_valueView1.text.toString())
                }else{
                    val1 = 0
                }
                sumBox = val1 + val2 + val3 + val4 + val5 + val6 + val7 + val8+ val9 + val10
                divBox = sumBox/spinnerValue
                sumValue.text = sumBox.toString()
                divValue.text = divBox.toString()
                if(sumBox%spinnerValue>0){
                    findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.VISIBLE
                    amariView.text = (sumBox%spinnerValue).toString()
                }else{findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.GONE}
            }
        })

        hiyou_valueView2.addTextChangedListener(object: CustomTextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(isNumber(hiyou_valueView2.text.toString())){
                    val2 = Integer.parseInt(hiyou_valueView2.text.toString())
                }else{
                    val2 = 0
                }
                sumBox = val1 + val2 + val3 + val4 + val5 + val6 + val7 + val8+ val9 + val10
                divBox = sumBox/spinnerValue
                sumValue.text = sumBox.toString()
                divValue.text = divBox.toString()
                if(sumBox%spinnerValue>0){
                    findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.VISIBLE
                    amariView.text = (sumBox%spinnerValue).toString()
                }else{findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.GONE}
            }
        })

        hiyou_valueView3.addTextChangedListener(object: CustomTextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(isNumber(hiyou_valueView3.text.toString())){
                    val3 = Integer.parseInt(hiyou_valueView3.text.toString())
                }else{
                    val3 = 0
                }
                sumBox = val1 + val2 + val3 + val4 + val5 + val6 + val7 + val8+ val9 + val10
                divBox = sumBox/spinnerValue
                sumValue.text = sumBox.toString()
                divValue.text = divBox.toString()
                if(sumBox%spinnerValue>0){
                    findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.VISIBLE
                    amariView.text = (sumBox%spinnerValue).toString()
                }else{findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.GONE}
            }
        })

        hiyou_valueView4.addTextChangedListener(object: CustomTextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(isNumber(hiyou_valueView4.text.toString())){
                    val4 = Integer.parseInt(hiyou_valueView4.text.toString())
                }else{
                    val4 = 0
                }
                sumBox = val1 + val2 + val3 + val4 + val5 + val6 + val7 + val8+ val9 + val10
                divBox = sumBox/spinnerValue
                sumValue.text = sumBox.toString()
                divValue.text = divBox.toString()
                if(sumBox%spinnerValue>0){
                    findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.VISIBLE
                    amariView.text = (sumBox%spinnerValue).toString()
                }else{findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.GONE}
            }
        })

        hiyou_valueView5.addTextChangedListener(object: CustomTextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(isNumber(hiyou_valueView5.text.toString())){
                    val5 = Integer.parseInt(hiyou_valueView5.text.toString())
                }else{
                    val5 = 0
                }
                sumBox = val1 + val2 + val3 + val4 + val5 + val6 + val7 + val8+ val9 + val10
                divBox = sumBox/spinnerValue
                sumValue.text = sumBox.toString()
                divValue.text = divBox.toString()
                if(sumBox%spinnerValue>0){
                    findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.VISIBLE
                    amariView.text = (sumBox%spinnerValue).toString()
                }else{findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.GONE}
            }
        })

        hiyou_valueView6.addTextChangedListener(object: CustomTextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(isNumber(hiyou_valueView6.text.toString())){
                    val6 = Integer.parseInt(hiyou_valueView6.text.toString())
                }else{
                    val6 = 0
                }
                sumBox = val1 + val2 + val3 + val4 + val5 + val6 + val7 + val8+ val9 + val10
                divBox = sumBox/spinnerValue
                sumValue.text = sumBox.toString()
                divValue.text = divBox.toString()
                if(sumBox%spinnerValue>0){
                    findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.VISIBLE
                    amariView.text = (sumBox%spinnerValue).toString()
                }else{findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.GONE}
            }
        })

        hiyou_valueView7.addTextChangedListener(object: CustomTextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(isNumber(hiyou_valueView7.text.toString())){
                    val7 = Integer.parseInt(hiyou_valueView7.text.toString())
                }else{
                    val7 = 0
                }
                sumBox = val1 + val2 + val3 + val4 + val5 + val6 + val7 + val8+ val9 + val10
                divBox = sumBox/spinnerValue
                sumValue.text = sumBox.toString()
                divValue.text = divBox.toString()
                if(sumBox%spinnerValue>0){
                    findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.VISIBLE
                    amariView.text = (sumBox%spinnerValue).toString()
                }else{findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.GONE}
            }
        })

        hiyou_valueView8.addTextChangedListener(object: CustomTextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(isNumber(hiyou_valueView8.text.toString())){
                    val8 = Integer.parseInt(hiyou_valueView8.text.toString())
                }else{
                    val8 = 0
                }
                sumBox = val1 + val2 + val3 + val4 + val5 + val6 + val7 + val8+ val9 + val10
                divBox = sumBox/spinnerValue
                sumValue.text = sumBox.toString()
                divValue.text = divBox.toString()
                if(sumBox%spinnerValue>0){
                    findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.VISIBLE
                    amariView.text = (sumBox%spinnerValue).toString()
                }else{findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.GONE}
            }
        })

        hiyou_valueView9.addTextChangedListener(object: CustomTextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(isNumber(hiyou_valueView9.text.toString())){
                    val9 = Integer.parseInt(hiyou_valueView9.text.toString())
                }else{
                    val9 = 0
                }
                sumBox = val1 + val2 + val3 + val4 + val5 + val6 + val7 + val8+ val9 + val10
                divBox = sumBox/spinnerValue
                sumValue.text = sumBox.toString()
                divValue.text = divBox.toString()
                if(sumBox%spinnerValue>0){
                    findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.VISIBLE
                    amariView.text = (sumBox%spinnerValue).toString()
                }else{findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.GONE}
            }
        })

        hiyou_valueView10.addTextChangedListener(object: CustomTextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(isNumber(hiyou_valueView10.text.toString())){
                    val10 = Integer.parseInt(hiyou_valueView10.text.toString())
                }else{
                    val10 = 0
                }
                sumBox = val1 + val2 + val3 + val4 + val5 + val6 + val7 + val8+ val9 + val10
                divBox = sumBox/spinnerValue
                sumValue.text = sumBox.toString()
                divValue.text = divBox.toString()
                if(sumBox%spinnerValue>0){
                    findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.VISIBLE
                    amariView.text = (sumBox%spinnerValue).toString()
                }else{findViewById<View>(resources.getIdentifier("layoutAmari", "id", packageName)).visibility = View.GONE}
            }
        })

    }

    fun isNumber(num: String): Boolean {
        return try {
            num.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    //書き込み関数
    private fun writePref(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        pref.edit{

            for (i in 1..layoutMax){
                putString("HIYOU_TEXTBOX$i",findViewById<TextView>(resources.getIdentifier("hiyou_textBox$i", "id", packageName)).text.toString())
                putString("HIYOU_VALUEVIEW$i",findViewById<TextView>(resources.getIdentifier("hiyou_valueView$i", "id", packageName)).text.toString())

            }

            //どこまでtextviewを追加したのか
            putInt("HIYOU_VISIBILITY_STAT",layoutVisibilityFlg)
            putInt("HIYOU_VISIBILITY_STAT_SAVEPOINT",layoutVisibilityFlg)

            //sumとdivの値
            putInt("HIYOU_SUM_VALUE",sumBox)
            putInt("HIYOU_DIV_VALUE",divBox)

            //スピナーの値
            putInt("HIYOU_SPINNER_VALUE",spinnerValue)
        }
    }

    //読み込み関数
    private fun setPref(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        layoutVisibilityFlg = pref.getInt("HIYOU_VISIBILITY_STAT_SAVEPOINT",1)

        if(pref.getInt("HIYOU_VISIBILITY_STAT_SAVEPOINT",0) == layoutMax){floatingAddButton.visibility = View.GONE} //追加ボタンを表示
        else{floatingAddButton.visibility = View.VISIBLE}

        for(i in 1..layoutMax){
            findViewById<View>(resources.getIdentifier("layout$i", "id", packageName)).visibility = View.GONE //一度すべて非表示
        }

        for(i in 1..pref.getInt("HIYOU_VISIBILITY_STAT_SAVEPOINT",1)){
            findViewById<View>(resources.getIdentifier("layout$i", "id", packageName)).visibility = View.VISIBLE //savepointまで表示
        }

        pref.edit{
            putInt("HIYOU_VISIBILITY_STAT",pref.getInt("HIYOU_VISIBILITY_STAT_SAVEPOINT",0))//上書き
        }

        for (i in 1..layoutMax){
            findViewById<TextView>(resources.getIdentifier("hiyou_textBox$i", "id", packageName)).setText(pref.getString("HIYOU_TEXTBOX"+i,""))
            findViewById<TextView>(resources.getIdentifier("hiyou_valueView$i", "id", packageName)).setText(pref.getString("HIYOU_VALUEVIEW"+i,""))
        }

        //test
        if(isNumber(hiyou_valueView1.text.toString())){
            val1 = Integer.parseInt(hiyou_valueView1.text.toString())
        }else{
            val1 = 0
        }
        if(isNumber(hiyou_valueView2.text.toString())){
            val2 = Integer.parseInt(hiyou_valueView2.text.toString())
        }else{
            val2 = 0
        }
        if(isNumber(hiyou_valueView3.text.toString())){
            val3 = Integer.parseInt(hiyou_valueView3.text.toString())
        }else{
            val3 = 0
        }
        if(isNumber(hiyou_valueView4.text.toString())){
            val4 = Integer.parseInt(hiyou_valueView4.text.toString())
        }else{
            val5 = 0
        }
        if(isNumber(hiyou_valueView5.text.toString())){
            val5 = Integer.parseInt(hiyou_valueView5.text.toString())
        }else{
            val5 = 0
        }
        if(isNumber(hiyou_valueView6.text.toString())){
            val6 = Integer.parseInt(hiyou_valueView6.text.toString())
        }else{
            val6 = 0
        }
        if(isNumber(hiyou_valueView7.text.toString())){
            val7 = Integer.parseInt(hiyou_valueView7.text.toString())
        }else{
            val7 = 0
        }
        if(isNumber(hiyou_valueView8.text.toString())){
            val8 = Integer.parseInt(hiyou_valueView8.text.toString())
        }else{
            val8 = 0
        }
        if(isNumber(hiyou_valueView9.text.toString())){
            val9 = Integer.parseInt(hiyou_valueView9.text.toString())
        }else{
            val9 = 0
        }
        if(isNumber(hiyou_valueView10.text.toString())){
            val10 = Integer.parseInt(hiyou_valueView10.text.toString())
        }else{
            val10 = 0
        }

        //初期画面用
        sumBox = pref.getInt("HIYOU_SUM_VALUE",0)
        sumValue.text = sumBox.toString()
        divBox = pref.getInt("HIYOU_DIV_VALUE",0)
        divValue.text = divBox.toString()
    }

    private fun deleteFun(layoutLine:Int){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        findViewById<TextView>(resources.getIdentifier("hiyou_textBox$layoutLine", "id", packageName)).text=""
        findViewById<TextView>(resources.getIdentifier("hiyou_valueView$layoutLine", "id", packageName)).text=""

        for(i in layoutLine..layoutMax-1){//削除行-表示されている最大行-1

            //下の行の内容を上に持ってくる
            findViewById<TextView>(resources.getIdentifier("hiyou_textBox$i", "id", packageName)).text =
                findViewById<TextView>(resources.getIdentifier("hiyou_textBox"+(i+1), "id", packageName)).text

            findViewById<TextView>(resources.getIdentifier("hiyou_valueView$i", "id", packageName)).text =
                findViewById<TextView>(resources.getIdentifier("hiyou_valueView"+(i+1), "id", packageName)).text
        }

        //最低行の内容を削除
        findViewById<TextView>(resources.getIdentifier("hiyou_textBox$layoutMax", "id", packageName)).text=""
        findViewById<TextView>(resources.getIdentifier("hiyou_valueView$layoutMax", "id", packageName)).text=""


        //行の非表示まわり
        if(findViewById<View>(resources.getIdentifier("layout"+pref.getInt("HIYOU_VISIBILITY_STAT",1), "id", packageName)) == null){
            findViewById<View>(resources.getIdentifier("layout1", "id", packageName)).visibility = View.GONE
        }else{
            findViewById<View>(resources.getIdentifier("layout"+pref.getInt("HIYOU_VISIBILITY_STAT",1), "id", packageName)).visibility = View.GONE}
        layoutVisibilityFlg = pref.getInt("HIYOU_VISIBILITY_STAT",0)-1

        if(pref.getInt("HIYOU_VISIBILITY_STAT",0) == layoutMax) floatingAddButton.visibility = View.VISIBLE //Max行目を消した時追加ボタンを表示

        pref.edit{putInt("HIYOU_VISIBILITY_STAT",layoutVisibilityFlg)}
    }

}