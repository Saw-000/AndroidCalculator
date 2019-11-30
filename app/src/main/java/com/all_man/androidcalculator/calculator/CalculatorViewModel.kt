package com.all_man.androidcalculator.calculator

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorViewModel : ViewModel() {

    // Display_formula_text
    // 初期値がnullなら初期化不要っぽい。(てか、初期化でnull代入できなかった。)
    private val _displayedFormula = MutableLiveData<String?>()
    val displayedFormula : LiveData<String?>
        get() = _displayedFormula

    // Display_formula_textの最後の一文字
    // LiveDataなんだけど、格納してる型であるStringとして扱えた。(というかLiveData型では扱えなかった。)
    val lastCharactor = Transformations.map(displayedFormula) {
        when (it) {
            null -> null
            else -> it.substring(it.length-1)
        }
    }
    // Calculated_Answer in Double
    private val _calculatedAnswer = MutableLiveData<Double?>()
    val calculatedAnswer : LiveData<Double?>
        get() = _calculatedAnswer

    val displayedAnswer = Transformations.map(calculatedAnswer) {
       when (it) {
           null -> null
           else -> "Ans= ${it}"
       }
    }



    // =押した時のerror_messageを表示するトリガー
    private val _showErrorMessageStart = MutableLiveData<Boolean>()
    val showErrorMessage : LiveData<Boolean>
        get() = _showErrorMessageStart

    /** 初期化 */
    init {
        _showErrorMessageStart.value = false
    }




    /**
     *
     * methods for num_pad buttons.
     * 主に [lastCharactor] の値によって、表示テキストの変更処理をするかを場合分け
     *    ↓   ↓   ↓
     * */

    //AC: displayedText=nullにして、2回目でAns=0にする。
    fun allClear() {
        if (null == displayedFormula.value) {
            _calculatedAnswer.value = null
        } else {
            _displayedFormula.value = null
        }
    }

    // del: displayedTextがnullじゃなければ、１文字消す。
    // 末尾が"s"なら、３文字("Ans")消す。
    fun delete() {
        if (null != displayedFormula.value) {
            when (lastCharactor.value) {
                "s" -> _displayedFormula.value!!.substring(0, lastCharactor.value.toString().length-3)
                else -> _displayedFormula.value!!.substring(lastCharactor.value.toString().length-1)
                //!!: _displayedFormula.valueは実際null出ないと保証されているけど、外の変数にスマートキャスト働かないからなあ。。
            }

        }
    }

    // %: _calculatedAnswer を100分の１する
    fun percent() {
        // なんとなくスマートキャスト使いたいから、ローカル関数に変換
        val calcAnswer = calculatedAnswer.value
        if (null != calcAnswer) {
            _calculatedAnswer.value = calcAnswer / 100
        }
    }

    // =: 末尾が、数字かAnsなら、Display_formula_textの計算値をCalculated_Answerに渡す
    // それ以外はエラーテキストをToastで表示
    fun equal() {
        when (lastCharactor.value) {
            null, ".", "+", "-", "*", "/" -> _showErrorMessageStart.value = true
            else -> {
                // まず式中に"Ans"があれば[calculatedAnswer]に入れ替える
                //　そして、式の評価を _calculatedAnswer へ渡す
                _calculatedAnswer.value =
                when (displayedFormula.value!!.contains("Ans")) {
                    true -> ExpressionBuilder(displayedFormula.value)
                                .variable("Ans")
                                .build()
                                .setVariable("Ans", calculatedAnswer.value!!.toDouble())
                                .evaluate()
                    // !!: 実際"Ans"を計算式に入れられる時点でcalculatedAnswerはnon_nullなのだが。スマキャしないから。。

                    false -> ExpressionBuilder(displayedFormula.value)
                                 .build()
                                 .evaluate()
                }

                // 計算式をリセット
                _displayedFormula.value = null
            }
        }
    }

    // .: textに.が含まれないかつ末尾が数字、なら＋
    fun addDot(view: View) {
        displayedFormula.value?.let {
            if(!it.contains(".")) {
                // TODO:toInt()処理ができない時点でエラーになる。。
                when (lastCharactor.value?.toInt()) {
                    is Int -> addChar(view)
                }
            }
        }
    }

    // +=*/: 末尾が、数字かAns、なら＋
    // - は [_displayedformula] がnullでも打てる
    fun addOperator(view: View) {
        val textView = view as TextView
        //　スマートキャスト用ローカル変数
        val lastChar = lastCharactor.value
        when(lastChar){
            // _displayedformula == null の時は - が押された時だけ＋
            null -> if (textView.text == "-") _displayedFormula.value = "-"
            // 末尾が、数字かAns、なら＋
            "s" -> addChar(view)
            else ->  {
                // TODO:toInt()処理ができない時点でエラーになる。。
                if (lastChar.toInt() is Int) {
                    addChar(view)
                }
            }
        }
    }

    // 0~9: 末尾が、".", operator, 数字の時は＋
    // ただし 末尾が0 の時は、計算式が2文字以上かつ、2文字前が0ではない、ことが条件
    fun addNum(view: View) {
        val textView = view as TextView
        when (lastCharactor.value) {
            null, ".", "+", "-", "*", "/" -> addChar(view)
            else ->  {
                // 末尾が数字の場合
                if (lastCharactor.value?.toInt() is Int) {
                    when (lastCharactor.value) {
                        "0" -> {
                            val length = displayedFormula.value.toString().length
                            if (length >= 2){
                                if (displayedFormula.value?.substring(length-2, length-2) != "0" ){
                                    addChar(view)
                                }
                            }
                        }
                        else -> addChar(view)
                    }
                }
            }

        }
    }

    // Ans: calculatedAnswerがnullではないとき、かつ、計算式の末尾がoperator
    fun addAns(view: View) {
        // 答えがnon nullでないと無反応
        if (null != calculatedAnswer.value) {
            when (lastCharactor.value) {
                null, "+", "-", "*", "/" -> addChar(view)
            }
        }

    }


    /** 押されたボタンの文字を計算式に追加する処理 */
    fun addChar(view: View) {
        val textView = view as TextView
        if (_displayedFormula.value == null) {
            _displayedFormula.value = textView.text.toString()
        } else {
            _displayedFormula.value += textView.text.toString()
        }
    }

    /** =ボタンのerror_messageを表示した後の処理 */
    fun showErrorMessageFinished() { _showErrorMessageStart.value = false }
}