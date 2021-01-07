package jp.ac.nkc_ct02.travelschedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.floatingAddButton
import kotlinx.android.synthetic.main.activity_main.readBtn1
import kotlinx.android.synthetic.main.activity_main.saveBtn1
import kotlinx.android.synthetic.main.activity_ryokousakusei.*

class RyokouSakuei : AppCompatActivity(){
    var layoutVisibilityFlg = 0 //textviewをどこまで追加したか記録
    val layoutMax = 10 //表示させる最大行

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ryokousakusei)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        //デバッグ用preference削除 使い終わったらコメントアウト
//        pref.edit{
//            clear()
//        }

        //前回までに表示したtextview等を再表示
        setPref()

        //作成遷移
        rKoumokuButton.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        textView21.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        //持ち物遷移
        rKoumokuButton3.setOnClickListener{
            val intent = Intent(applicationContext, MotimonoGamen::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        textView23.setOnClickListener {
            val intent = Intent(applicationContext, MotimonoGamen::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        //費用遷移
        rKoumokuButton4.setOnClickListener{
            val intent = Intent(applicationContext, HiyoukanriGamen::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        textView22.setOnClickListener {
            val intent = Intent(applicationContext, HiyoukanriGamen::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        //保存
        saveBtn1.setOnClickListener {
            writePref()
        }

        //読み込み
        readBtn1.setOnClickListener {
            setPref()
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
                putInt("SAKUSEI_VISIBILITY_STAT",layoutVisibilityFlg)
            }
        }

        //削除ボタン群
        for(i in 1..layoutMax){
            findViewById<View>(resources.getIdentifier("removeBtn$i", "id", packageName)).setOnClickListener { deleteFun(i) }
        }

    }

    //書き込み関数
    private fun writePref(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        pref.edit{

            for (i in 1..layoutMax){
                putString("SAKUSEI_STARTTEXT$i",findViewById<TextView>(resources.getIdentifier("startText$i", "id", packageName)).text.toString())
                putString("SAKUSEI_TIMETEXT$i",findViewById<TextView>(resources.getIdentifier("timeText$i", "id", packageName)).text.toString())
                putString("SAKUSEI_ENDTEXT$i",findViewById<TextView>(resources.getIdentifier("endText$i", "id", packageName)).text.toString())

            }

            //どこまでtextviewを追加したのか
            putInt("SAKUSEI_VISIBILITY_STAT",layoutVisibilityFlg)
            putInt("SAKUSEI_VISIBILITY_STAT_SAVEPOINT",layoutVisibilityFlg)
        }
    }

    //読み込み関数
    private fun setPref(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        layoutVisibilityFlg = pref.getInt("SAKUSEI_VISIBILITY_STAT_SAVEPOINT",1)

        if(pref.getInt("SAKUSEI_VISIBILITY_STAT_SAVEPOINT",0) == layoutMax){floatingAddButton.visibility = View.GONE} //追加ボタンを表示
        else{floatingAddButton.visibility = View.VISIBLE}

        for(i in 1..layoutMax){
            findViewById<View>(resources.getIdentifier("layout$i", "id", packageName)).visibility = View.GONE //一度すべて非表示
        }

        for(i in 1..pref.getInt("SAKUSEI_VISIBILITY_STAT_SAVEPOINT",1)){
            findViewById<View>(resources.getIdentifier("layout$i", "id", packageName)).visibility = View.VISIBLE //savepointまで表示
        }

        pref.edit{
            putInt("SAKUSEI_VISIBILITY_STAT",pref.getInt("SAKUSEI_VISIBILITY_STAT_SAVEPOINT",0))//上書き
        }

        for (i in 1..layoutMax){
            findViewById<TextView>(resources.getIdentifier("startText$i", "id", packageName)).setText(pref.getString(
                "SAKUSEI_STARTTEXT$i",""))
            findViewById<TextView>(resources.getIdentifier("timeText$i", "id", packageName)).setText(pref.getString(
                "SAKUSEI_TIMETEXT$i",""))
            findViewById<TextView>(resources.getIdentifier("endText$i", "id", packageName)).setText(pref.getString(
                "SAKUSEI_ENDTEXT$i",""))
        }
    }

    private fun deleteFun(layoutLine:Int){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        findViewById<TextView>(resources.getIdentifier("startText$layoutLine", "id", packageName)).text=""
        findViewById<TextView>(resources.getIdentifier("timeText$layoutLine", "id", packageName)).text=""
        findViewById<TextView>(resources.getIdentifier("endText$layoutLine", "id", packageName)).text=""

        for(i in layoutLine..layoutMax-1){//削除行-表示されている最大行-1

            //下の行の内容を上に持ってくる
            findViewById<TextView>(resources.getIdentifier("startText$i", "id", packageName)).text =
                findViewById<TextView>(resources.getIdentifier("startText"+(i+1), "id", packageName)).text

            findViewById<TextView>(resources.getIdentifier("timeText$i", "id", packageName)).text =
                findViewById<TextView>(resources.getIdentifier("timeText"+(i+1), "id", packageName)).text

            findViewById<TextView>(resources.getIdentifier("endText$i", "id", packageName)).text=
                findViewById<TextView>(resources.getIdentifier("endText"+(i+1), "id", packageName)).text
        }

        //最低行の内容を削除
        findViewById<TextView>(resources.getIdentifier("startText$layoutMax", "id", packageName)).text=""
        findViewById<TextView>(resources.getIdentifier("timeText$layoutMax", "id", packageName)).text=""
        findViewById<TextView>(resources.getIdentifier("endText$layoutMax", "id", packageName)).text=""

        //行の非表示まわり
        if(findViewById<View>(resources.getIdentifier("layout"+pref.getInt("SAKUSEI_VISIBILITY_STAT",1), "id", packageName)) == null){
            findViewById<View>(resources.getIdentifier("layout1", "id", packageName)).visibility = View.GONE
        }else{
            findViewById<View>(resources.getIdentifier("layout"+pref.getInt("SAKUSEI_VISIBILITY_STAT",1), "id", packageName)).visibility = View.GONE}
        layoutVisibilityFlg = pref.getInt("SAKUSEI_VISIBILITY_STAT",0)-1

        if(pref.getInt("SAKUSEI_VISIBILITY_STAT",0) == layoutMax) floatingAddButton.visibility = View.VISIBLE //Max行目を消した時追加ボタンを表示

        pref.edit{putInt("SAKUSEI_VISIBILITY_STAT",layoutVisibilityFlg)}
    }
}
