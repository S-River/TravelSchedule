package jp.ac.nkc_ct02.travelschedule

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : FragmentActivity(), DatePickerDialog.OnDateSetListener {

    var layoutVisibilityFlg = 0 //textviewをどこまで追加したか記録
    val layoutMax = 10 //
    var datePicFlg = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        //デバッグ用preference削除 使い終わったらコメントアウト
//        pref.edit{
//            clear()
//        }

        //前回までに表示したtextview等を再表示
        setPref()

        //dataText削除ボタン
        dataTextDelBtn.setOnClickListener {
            dateTextView.text = "--/--/--"
            dateTextView2.text = "--/--/--"
        }

        //スケジュール遷移
        koumokuButton2.setOnClickListener {
            val intent = Intent(applicationContext, RyokouSakuei::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        textView11.setOnClickListener {
            val intent = Intent(applicationContext, RyokouSakuei::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        //持ち物遷移
        koumokuButton3.setOnClickListener {
            val intent = Intent(applicationContext, MotimonoGamen::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        textView12.setOnClickListener {
            val intent = Intent(applicationContext, MotimonoGamen::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        //費用遷移
        koumokuButton4.setOnClickListener{
            val intent4 = Intent(applicationContext,HiyoukanriGamen::class.java)
            startActivity(intent4)
            overridePendingTransition(0, 0)
        }
        textView8.setOnClickListener {
            val intent4 = Intent(applicationContext,HiyoukanriGamen::class.java)
            startActivity(intent4)
            overridePendingTransition(0, 0)
        }

        saveBtn1.setOnClickListener {
            writePref()
        }

        readBtn1.setOnClickListener {
            setPref()
        }

        //追加ボタン
        floatingAddButton.setOnClickListener {

            //現状Min-Max行目の範囲で操作
            for (i in 1..layoutMax) {
                //idを取得し処理
                if(findViewById<View>(resources.getIdentifier("layout"+i, "id", packageName)).visibility == View.GONE){

                    findViewById<View>(resources.getIdentifier("layout"+i, "id", packageName)).visibility = View.VISIBLE
                    //どこまで表示したか記録
                    layoutVisibilityFlg = i
                    if(i==layoutMax) floatingAddButton.visibility= View.GONE //Max行目を出すした時追加ボタンを非表示
                    break
                }
            }

            pref.edit{
                putInt("SEISAKU_VISIBILITY_STAT",layoutVisibilityFlg)
            }
        }

        //削除ボタン群
        for(i in 1..layoutMax){
            findViewById<View>(resources.getIdentifier("removeBtn"+i, "id", packageName)).setOnClickListener { deleteFun(i) }
        }

    }

    //書き込み関数
    private fun writePref(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        pref.edit{
            //textviewの取得
            putString("SEISAKU_DAY1_DATE",dateTextView.text.toString())
            putString("SEISAKU_DAY2_DATE",dateTextView2.text.toString())

            for (i in 1..layoutMax){
                putString("SEISAKU_TEXTBOX"+(i*2-1),findViewById<TextView>(resources.getIdentifier("textBox"+(i*2-1), "id", packageName)).text.toString())
                putString("SEISAKU_TEXTBOX"+i*2,findViewById<TextView>(resources.getIdentifier("textBox"+i*2, "id", packageName)).text.toString())
            }

            //どこまでtextviewを追加したのか
            putInt("SEISAKU_VISIBILITY_STAT",layoutVisibilityFlg)
            putInt("SEISAKU_VISIBILITY_STAT_SAVEPOINT",layoutVisibilityFlg)
        }
    }

    //読み込み関数
    private fun setPref(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        layoutVisibilityFlg = pref.getInt("SAKUSEI_VISIBILITY_STAT_SAVEPOINT",1)

        //日程
        dateTextView.text=pref.getString("SEISAKU_DAY1_DATE","--/--/--")
        dateTextView2.text=pref.getString("SEISAKU_DAY2_DATE","--/--/--")

        if(pref.getInt("SEISAKU_VISIBILITY_STAT_SAVEPOINT",0) == layoutMax){floatingAddButton.visibility = View.GONE} //追加ボタンを表示
        else{floatingAddButton.visibility = View.VISIBLE}

        for(i in 1..layoutMax){
            findViewById<View>(resources.getIdentifier("layout"+i, "id", packageName)).visibility = View.GONE //一度すべて非表示
        }

        for(i in 1..pref.getInt("SEISAKU_VISIBILITY_STAT_SAVEPOINT",1)){
            findViewById<View>(resources.getIdentifier("layout"+i, "id", packageName)).visibility = View.VISIBLE //savepointまで表示
        }

        pref.edit{
            putInt("SEISAKU_VISIBILITY_STAT",pref.getInt("SEISAKU_VISIBILITY_STAT_SAVEPOINT",0))//上書き
        }

        for (i in 1..layoutMax){
            findViewById<TextView>(resources.getIdentifier("textBox"+(i*2-1), "id", packageName)).text=(pref.getString("SEISAKU_TEXTBOX"+(i*2-1),""))
            findViewById<TextView>(resources.getIdentifier("textBox"+i*2, "id", packageName)).text=(pref.getString("SEISAKU_TEXTBOX"+i*2,""))
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        val str = String.format(Locale.US, "%d/%d/%d", year, monthOfYear+1, dayOfMonth)
        if(datePicFlg == 1) dateTextView.text = str
        if(datePicFlg == 2) dateTextView2.text = str

    }

    fun showDatePickerDialog(v: View) {
        val newFragment = DatePick()
        datePicFlg = 1
        newFragment.show(supportFragmentManager, "datePicker")

    }

    fun showDatePickerDialog2(v: View) {
        val newFragment = DatePick()
        datePicFlg = 2
        newFragment.show(supportFragmentManager, "datePicker")
    }

    private fun deleteFun(layoutLine:Int){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        findViewById<TextView>(resources.getIdentifier("textBox"+(layoutLine*2-1).toString(), "id", packageName)).text=""
        findViewById<TextView>(resources.getIdentifier("textBox"+(layoutLine*2).toString(), "id", packageName)).text=""

        for(i in layoutLine..layoutMax-1){//削除行-表示されている最大行-1

            //下の行の内容を上に持ってくる
            findViewById<TextView>(resources.getIdentifier("textBox"+(i*2-1).toString(), "id", packageName)).text =
                    findViewById<TextView>(resources.getIdentifier("textBox"+((i+1)*2-1), "id", packageName)).text

            findViewById<TextView>(resources.getIdentifier("textBox"+(i*2), "id", packageName)).text =
                    findViewById<TextView>(resources.getIdentifier("textBox"+((i+1)*2), "id", packageName)).text
        }

        //最低行の内容を削除
        findViewById<TextView>(resources.getIdentifier("textBox"+(layoutMax*2-1).toString(), "id", packageName)).text=""
        findViewById<TextView>(resources.getIdentifier("textBox"+(layoutMax*2).toString(), "id", packageName)).text=""

        //行の非表示まわり
        if(findViewById<View>(resources.getIdentifier("layout"+pref.getInt("SEISAKU_VISIBILITY_STAT",0), "id", packageName)) == null){
            findViewById<View>(resources.getIdentifier("layout1", "id", packageName)).visibility = View.GONE
        }else{
            findViewById<View>(resources.getIdentifier("layout"+pref.getInt("SEISAKU_VISIBILITY_STAT",0), "id", packageName)).visibility = View.GONE}
        layoutVisibilityFlg = pref.getInt("SEISAKU_VISIBILITY_STAT",0)-1

        if(pref.getInt("SEISAKU_VISIBILITY_STAT",0) == layoutMax) floatingAddButton.visibility = View.VISIBLE //Max行目を消した時追加ボタンを表示

        pref.edit{putInt("SEISAKU_VISIBILITY_STAT",layoutVisibilityFlg)}
    }
}