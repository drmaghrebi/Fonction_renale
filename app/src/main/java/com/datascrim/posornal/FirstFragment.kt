package com.datascrim.posornal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_first.*
import java.lang.Math.pow
import kotlin.comparisons.*
import kotlin.math.*
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.calcDFG).setOnClickListener {

            try {
                DFG.text=calculDFG(rbman.isChecked,age.text.toString().toDouble(),weight.text.toString().toFloat(),creat.text.toString().toFloat(),race.isChecked, creatunit.isChecked)
            } catch (e: Exception) {
                DFG.text=""
                Toast.makeText(this.context, "Erreur : données manquantes",Toast.LENGTH_LONG).show()

            }

        }
        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            age.setText("")
            DFG.text=""
            rbman.isChecked=true
            weight.setText("")
            creat.setText("")
        }
    }
    fun calculDFG ( sex: Boolean, age: Double, w: Float,  creat: Float , race: Boolean , creatunit: Boolean): String {
        //var cgDFG = 0.1
        //var ckdDFG = 0.1
        //var mdrdDFG = 0.1
        var cgKsex = 0.1
        var mdrdKsex = 0.1
        var mdrdKrace= 0.1
        var ckdKsex= 0.1
        var ckdAlphasex= 0.1
        var ckdBetasex= 0.1
        var creatunitconv = 1.0
        var ckdKrace= 0.1
        val mdrdIDMS=0.94

        when(sex) {
            true -> {
                cgKsex=1.23
                mdrdKsex=1.0
                ckdKsex=80.0
                ckdAlphasex=-0.411
                ckdBetasex=1.0


            }
            false -> {
                cgKsex=1.04
                mdrdKsex= 0.742
                ckdKsex=62.0
                ckdAlphasex=-0.329
                ckdBetasex=1.018


            }
        }
        when(race) {
            true -> {
                mdrdKrace=1.21
                ckdKrace=1.159

            }
            false -> {
                mdrdKrace=1.0
                ckdKrace=1.0
            }
        }
        if (creatunit) creatunitconv = 88.4
        val cgDFG = cgKsex * (140 - age) * w / (creat*creatunitconv)
        val ckdDFG = (141f * pow(minOf(creat*creatunitconv / ckdKsex, 1.0), ckdAlphasex) * pow(
            maxOf(creat * creatunitconv / ckdKsex, 1.0),
            -1.209
        ) * pow(0.993, age))*ckdKrace*ckdBetasex
        val mdrdDFG = 186f * pow((creat*creatunitconv * 0.0113), -1.154) * pow(age, -0.203)*mdrdKsex*mdrdKrace * mdrdIDMS

        //if (race) ckdDFG=ckdDFG*1.159
        val DFG : String = "Cockcroft-Gault = %.2f".format(cgDFG) + "mL/min\nCKD-EPI = %.2f".format(ckdDFG) + "mL/min/1.73m2\nMDRD = %.2f".format(mdrdDFG)+"mL/min/1.73m2"
        return DFG
    }
}