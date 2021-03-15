package com.arbonik.helper.admin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.arbonik.helper.R
import com.arbonik.helper.StatisticsFireBase
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_statisics_admin.*


class StatisicsFragment_admin : Fragment(), StatisticsFireBase.getStatistics
{
    lateinit var graph: GraphView
    lateinit var _context: Context
    lateinit var layout_statisics: LinearLayout
    lateinit var layout_requests: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) }

    override fun getData(document: String, field: String)
    {
        val data = StatisticsFireBase.db.document(document).get().result?.get(field)
//        val example: MutableList<LineGraphSeries<DataPoint>> = mutableListOf(
//            LineGraphSeries(arrayOf<DataPoint>(DataPoint(1.0, 3.0), DataPoint(2.0, 6.0), DataPoint(3.0, 6.0))),
//            LineGraphSeries(arrayOf<DataPoint>(DataPoint(2.0, 4.0), DataPoint(3.0, 5.0), DataPoint(5.0, 7.0))),
//            LineGraphSeries(arrayOf<DataPoint>(DataPoint(1.0, 5.0), DataPoint(2.0, 9.0), DataPoint(3.0, 8.0))))
//        drawGraph(example)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View?
    {
        var root =  inflater.inflate(R.layout.fragment_statisics_admin, container, false)
        initViews(root)
        graph.legendRenderer.isVisible = true
        root.findViewById<Button>(R.id.bb).setOnClickListener{
            getData("", "")
//            S.reverseData(S.USERS_TAG, S.Users(4,4,4))
            Toast.makeText(context, "test", Toast.LENGTH_SHORT).show()
        }
        return root
    }

    private fun checked(view: CheckBox)
    {
        if (!view.isChecked) return
        var checkUsers = mutableListOf<String>()
        var checkRequests = mutableListOf<String>()
        when (view.id)
        {
            R.id.all_users -> checkUsers.add(getString(R.string.all_users))
            R.id.veterans -> {   }
            R.id.volonteers -> {    }
            R.id.all_requests -> {  }
            R.id.refused_requests -> {  }
            R.id.complited_requests -> {    }
            else -> {   }
        }
        text.text = ""
        var t = ""
        checkUsers?.forEach {
         t+=it+"\n"
        }
        text.text=t
    }
    private fun drawGraph(series: MutableList<LineGraphSeries<DataPoint>> //,
        //                          titles: MutableList<String>
    )
    {
            for (current in 0 until series.size)
            {
//                series[current].title = titles[current]
                series[current].color = when (current)
                {
                    0 -> (resources.getColor(R.color.red))
                    1 -> (resources.getColor(R.color.green))
                    2 -> (resources.getColor(R.color.blue))
                    else -> TODO()
                }
                graph?.addSeries(series[current])
            }
    }

    private fun initViews(root: View)
    {
        root.apply {
            _context = context
            graph = findViewById<GraphView>(R.id.graph)
            layout_statisics = findViewById<LinearLayout>(R.id.layoutForUsersStatistic)
            layout_requests = findViewById<LinearLayout>(R.id.layoutForRequestsStatistics)
            mutableListOf<CheckBox>(
                findViewById(R.id.all_users),
                findViewById(R.id.volonteers),
                findViewById(R.id.veterans)/*,
                findViewById(R.id.requests)*/
            )
                .forEach{ it.setOnClickListener{ checked( it as CheckBox ) } }
        }
    }
}