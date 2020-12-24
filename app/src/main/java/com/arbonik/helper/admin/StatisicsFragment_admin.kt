package com.arbonik.helper.admin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import com.arbonik.helper.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_statisics_admin.*


class StatisicsFragment_admin : Fragment()
{
    lateinit var graph: GraphView
    lateinit var _context: Context
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View?
    {
        var root =  inflater.inflate(R.layout.fragment_statisics_admin, container, false)
        .apply {
            _context = context
            graph = findViewById<GraphView>(R.id.graph)
            mutableListOf<CheckBox>(
                findViewById(R.id.all_users),
                findViewById(R.id.volonters),
                findViewById(R.id.veterans)/*,
                findViewById(R.id.requests)*/
            )
                .forEach{ it.setOnClickListener{ checked() } }
        }
        graph.legendRenderer.isVisible = true

        return root
    }

    private fun checked()
    {
        text.text = ""
        var checks = mutableListOf<String>()
        if (all_users.isChecked) checks.add(resources.getString(R.string.all_users))
        if (veterans.isChecked) checks.add(resources.getString(R.string.veterans))
        if (volonters.isChecked) checks.add(resources.getString(R.string.volonteers))
        if (requests.isChecked) checks.add("refused_requests")
        var t = ""
        checks?.forEach {
         t+=it+"\n"
        }
        text.text=t
    }
    private fun drawGraph(
        series: MutableList<LineGraphSeries<DataPoint>>,
        titles: MutableList<String>)
    {
            for (current in 0 until series.size)
            {
                series[current].title = titles[current]
                series[current].color = when(current)
                {
                    0 -> (resources.getColor(R.color.red))
                    1 -> (resources.getColor(R.color.green))
                    2 -> (resources.getColor(R.color.blue))
                    else -> TODO()
                }
                graph?.addSeries(series[current])
            }
    }

}

