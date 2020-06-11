package com.dreamTanAlex.ptutcartes.ui.partyGames

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import android.widget.Toast
import com.dreamTanAlex.ptutcartes.ui.connection.ConnectionPage
import kotlinx.android.synthetic.main.connection_page_fragment.*

class WifiDirectBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val activity: ConnectionPage,
    private val peerListListener : WifiP2pManager.PeerListListener,
    private val connectionListener : WifiP2pManager.ConnectionInfoListener

) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action: String? = intent.action;
        when (action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
               val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                if (state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                    Toast.makeText(context, "Wifi is ON",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Wifi is OFF",Toast.LENGTH_SHORT).show();
                }
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                manager?.requestPeers(channel, peerListListener)
                Log.d(TAG, "P2P peers changed")
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                // Respond to new connection or disconnections
                manager?.let { manager ->

                    val networkInfo: NetworkInfo? = intent
                        .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO) as NetworkInfo

                    if (networkInfo?.isConnected == true) {
                        // We are connected with the other device, request connection
                        // info to find group owner IP
                        manager.requestConnectionInfo(channel, connectionListener)
                    }else {
                        activity.connection_Status.setText("Device Disconnected")
                    }
                }
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                // Respond to this device's wifi state changing
            }
        }
    }

}