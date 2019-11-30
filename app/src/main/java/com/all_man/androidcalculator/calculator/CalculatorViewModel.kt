package com.all_man.androidcalculator.calculator

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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

    // String? for displaying answer.
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
        var dispFormula = displayedFormula.value // スマキャ用ローカル関数
        if (null != dispFormula) {
            dispFormula = when (lastCharactor.value) {
                "s" -> dispFormula.substring(0, dispFormula.length - 3)
                else -> dispFormula.substring(0, dispFormula.length - 1)
                }
            _displayedFormula.value =
                if (dispFormula.isEmpty()){  // displayedTextが0になったらnullを代入
                    null
                } else {
                    dispFormula
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


    // TODO: .: 末尾が数字かつ、一番後ろの数字に.が含まれない
    fun addDot(view: View) {
        displayedFormula.value?.let {
            when (lastCharactor.value) {
                //TODO: 数字列挙うざいなー
                "0","1","2","3","4","5","6","7","8","9" -> {
                    var dispFormula =displayedFormula.value.toString()
                            .substring(0, displayedFormula.value.toString().length)
                    val originalDispFormulaLength = dispFormula.length
                    loop@ for (i in 1..originalDispFormulaLength) {
                        when (dispFormula.substring(dispFormula.length-1)) {
                            "." -> {
                                break@loop
                            }
                            "+", "-", "*", "/" -> {
                                addChar(view)
                                break@loop
                            }
                            else -> {
                                if (i == originalDispFormulaLength) {
                                    addChar(view)
                                } else {
                                    dispFormula = dispFormula.substring(0, dispFormula.length - 1)
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    // +=*/: 末尾が、数字かAns、なら＋
    // - は _displayedformula がnullでも打てる
    fun addOperator(view: View) {
        when(lastCharactor.value){
            // _displayedformula == null の時は - が押された時だけ＋
            null -> {
                val textView = view as TextView
                if (textView.text == "-") _displayedFormula.value = "-"
            }
            // 末尾が、数字かAns、なら＋
            //TODO: 数字列挙うざいなー
            "0","1","2","3","4","5","6","7","8","9",
            "s"
            ->  addChar(view)
        }
    }

    // 0~9: 末尾が、".", operator, 数字の時は＋
    // ただし 末尾が0 の時は、計算式が2文字以上かつ、2文字前が0ではない、ことが条件
    fun addNum(view: View) {
        when (lastCharactor.value) {
            "s" -> { /** 何もしない */}
            "0" -> {
                val length = displayedFormula.value.toString().length
                if (length >= 2){
                    when (displayedFormula.value!!.substring(length-2, length-1)){
                        // 2つ前はoprator/./numがあり得る => operatorじゃなければ＋
                        "0","1","2","3","4","5","6","7","8","9",
                        "."
                        -> addChar(view)
                    }
                }
            }
            else -> addChar(view)
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