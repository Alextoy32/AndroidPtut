package com.dreamTanAlex.ptutcartes.ui.connection

import android.content.BroadcastReceiver;
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dreamTanAlex.ptutcartes.MainActivity
import com.dreamTanAlex.ptutcartes.R
import com.dreamTanAlex.ptutcartes.ui.partyGames.WifiDirectBroadcastReceiver
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.ln
import kotlin.math.log


class ConnectionPage : Fragment() {

    companion object {
        fun newInstance() = ConnectionPage()
    }

    private lateinit var viewModel: ConnectionPageViewModel
    private val intentFilter = IntentFilter()

    /*private val mManager: WifiP2pManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().applicationContext.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    }*/

    /*private val mManager = requireContext().applicationContext.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    private val mChannel = mManager.initialize(context, Looper.getMainLooper(),null)
    private val mReceiver = WifiDirectBroadcastReceiver(mManager, mChannel, MainActivity())*/

     private val mManager: WifiP2pManager by lazy(LazyThreadSafetyMode.NONE) {
         requireContext().applicationContext.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    }

    private var mChannel: WifiP2pManager.Channel? = null
    private var mReceiver: BroadcastReceiver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.connection_page_fragment, container, false)
        val btnOnOff = view.findViewById<Button>(R.id.buttonOnOff)
        val btnDiscover = view.findViewById<Button>(R.id.buttonDiscover)
        val btsSend= view.findViewById<Button>(R.id.buttonSend)
        val listView =  view.findViewById<ListView>(R.id.liste_device)
        val readMsg = view.findViewById<TextView>(R.id.read_msg)
        val writeMsg = view.findViewById<TextInputLayout>(R.id.textInputLayout)
        val connectionStatus = view.findViewById<TextView>(R.id.connection_Status)

        val wifiManager = requireContext().applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        //val mManager = requireContext().applicationContext.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        //val mChannel = mManager.initialize(context, Looper.getMainLooper(),null)
        //val mReceiver = WifiDirectBroadcastReceiver(mManager, mChannel, MainActivity())
        mChannel = mManager.initialize(context, Looper.getMainLooper(),null)
        mChannel?.also { channel ->
            mReceiver = WifiDirectBroadcastReceiver(mManager, channel, this)
        }

        val stateChangedAction = intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        val peersChangedAction = intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        val connectionChangedAction = intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        val thisDeviceChangedAction = intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        btnOnOff.setOnClickListener {
            if(wifiManager.isWifiEnabled){
                println("Connecté");
                wifiManager.setWifiEnabled(false)
            }else {
                println("Déconnecté");
                wifiManager.setWifiEnabled(true)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
/*       mReceiver?.also { receiver ->
           registerReceiver(receiver, intentFilter)
        }*/
        //LocalBroadcastManager.getInstance(MainActivity()).registerReceiver(mReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
/*        mReceiver?.also { receiver ->
            LocalBroadcastManager.getInstance(MainActivity()).unregisterReceiver(receiver)
        }*/
        //LocalBroadcastManager.getInstance(MainActivity()).unregisterReceiver(mReceiver)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConnectionPageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
