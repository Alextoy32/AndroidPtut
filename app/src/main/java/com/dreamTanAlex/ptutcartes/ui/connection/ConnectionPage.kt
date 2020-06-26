package com.dreamTanAlex.ptutcartes.ui.connection

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dreamTanAlex.ptutcartes.R
import com.dreamTanAlex.ptutcartes.ui.partyGames.WifiDirectBroadcastReceiver
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.connection_page_fragment.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket


open class ConnectionPage : Fragment() {

    companion object {
        fun newInstance() = ConnectionPage()
    }

    private lateinit var viewModel: ConnectionPageViewModel
    lateinit var mManager: WifiP2pManager
    lateinit var mChannel: WifiP2pManager.Channel
    private val intentFilter = IntentFilter()


    private var peers = mutableListOf<WifiP2pDevice>()

    fun MESSAGE_READ() : Int = 1

   /* lateinit var serverClass : ServerClass;
    lateinit var clientClass: ClientClass;
    lateinit var sendReceive: SendReceive;*/
    lateinit var wifiManager : WifiManager
    lateinit var handlers : Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.connection_page_fragment, container, false)

        val btnOnOff = view?.findViewById<Button>(R.id.buttonOnOff)
        val btnDiscover = view?.findViewById<Button>(R.id.buttonDiscover)
        val btnSend= view?.findViewById<Button>(R.id.buttonSend)
        val listView =  view?.findViewById<ListView>(R.id.liste_device)
        var readMsg = view?.findViewById<TextView>(R.id.read_msg)
        val writeMsg = view?.findViewById<EditText>(R.id.editText)
        val connectionStatus = view?.findViewById<TextView>(R.id.connection_Status)

        mManager = context?.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        mChannel = mManager.initialize(context, Looper.getMainLooper(), null)
        wifiManager = requireContext().applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

        val stateChangedAction = intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        val peersChangedAction = intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        val connectionChangedAction = intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        val thisDeviceChangedAction = intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        //Change button text
        btnOnOff?.setOnClickListener {
            if(wifiManager.isWifiEnabled){
                println("Déconnecté");
                btnOnOff.setText("ON")
                wifiManager.setWifiEnabled(false)
            }else {
                println("Connecté");
                btnOnOff.setText("OFF")
                wifiManager.setWifiEnabled(true)
            }
        }

        btnDiscover?.setOnClickListener {
            mManager.discoverPeers(mChannel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    connectionStatus?.text = "Discovery Started"
                }
                override fun onFailure(i: Int) {
                    connectionStatus?.text = "Discovery Starting Failed"

                }
            })
        }

        listView?.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->

                // Picking the first device found on the network.
                val device = peers[i]


                val config = WifiP2pConfig().apply {
                    deviceAddress = device.deviceAddress
                    wps.setup = WpsInfo.PBC
                }

                mManager.connect(mChannel, config, object : WifiP2pManager.ActionListener {

                    override fun onSuccess() {
                        Toast.makeText(
                            context,
                            "Connected to "+device.deviceName,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(reason: Int) {
                        Toast.makeText(
                            context,
                            "Connect failed. Retry.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

        }

        /*btnSend?.setOnClickListener(View.OnClickListener {
            /*fun onClick(view: View) {*/
                var msg : String = writeMsg.toString()
                /*sendReceive.write(msg.toByteArray())*/
        })

        handlers = Handler(Handler.Callback {
            fun handleMessage(msg : Message) : Boolean {
                var readBuff : ByteArray? = null;
                var tempMsg : String;

                if (msg.what == MESSAGE_READ()){
                    readBuff = msg.obj as ByteArray;
                    tempMsg = String(readBuff, 0, msg.arg1)
                    readMsg?.text = tempMsg;
                }
                return true
            }
            return@Callback true;
        })*/

        return view
    }



    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->

        val refreshedPeers = peerList.deviceList

        if (refreshedPeers != peers) {
            peers.clear()
            peers.addAll(refreshedPeers);

            //(listAdapter as WiFiPeerListAdapter).notifyDataSetChanged()

            //ICI
            val deviceNameArray = arrayOfNulls<String>(refreshedPeers.size);
            val deviceArray = arrayOfNulls<WifiP2pDevice>(refreshedPeers.size);
            var index = 0;

            for(device in refreshedPeers){
                deviceNameArray[index] = device.deviceName;
                deviceArray[index] = device;
                index ++;
            }

            //ICI
            val adpater = context?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, deviceNameArray) }
            liste_device.adapter = adpater

        }

        if (peers.isNotEmpty()){
            Toast.makeText(context, "Device Found", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Device Found")
            return@PeerListListener
        }
        if (peers.isEmpty()) {
            Toast.makeText(context, "No Device Found", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "No Device Found")
            return@PeerListListener
        }
    }

    private val connectionListener = WifiP2pManager.ConnectionInfoListener { info ->
        val groupOwnerAddress: String = info.groupOwnerAddress.hostAddress

        if (info.groupFormed && info.isGroupOwner) {

            connection_Status.setText("GAME-MASTER")
           /* serverClass = ServerClass(sendReceive)
            serverClass.run()*/

        } else if (info.groupFormed) {

            connection_Status.setText("Player")
            /*clientClass = ClientClass(groupOwnerAddress, sendReceive)
            clientClass.run()*/

        }
    }


    private lateinit var mReceiver : BroadcastReceiver

    override fun onResume() {
        super.onResume()
        mReceiver = WifiDirectBroadcastReceiver(mManager, mChannel, this, peerListListener, connectionListener)
        context?.registerReceiver(mReceiver, intentFilter)
        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        val mReceiver = WifiDirectBroadcastReceiver(mManager, mChannel, this, peerListListener, connectionListener)
        context?.unregisterReceiver(mReceiver)
        Log.i(TAG, "onPause")
    }

   /*  class ServerClass(sendReceive: SendReceive) : Thread() {

         lateinit var socket: Socket;
         lateinit var serverSocket: ServerSocket;
         var sendReceives = sendReceive

         override fun run() {
            try {
                serverSocket = ServerSocket(8888)
                socket = serverSocket.accept()
                sendReceives = SendReceive(socket)
                sendReceives.run()
            } catch (e : IOException)
            {
                e.printStackTrace();
            }
         }
    }


    class SendReceive(skt: Socket) : ConnectionPage() {
        private var socket : Socket = skt;
        private lateinit var inputStream : InputStream;
        private lateinit var outputStream : OutputStream;

        init {
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (e : IOException) {
                e.printStackTrace();
            }
        }
         fun run() {
            var buffer : ByteArray = ByteArray(1024);
            var bytes : Int;

            while (socket !=null){
                try {
                    bytes = inputStream.read(buffer);
                    if(bytes>0){
                        handlers.obtainMessage(MESSAGE_READ(), bytes, -1, buffer).sendToTarget();
                    }
                }catch (e : IOException) {
                    e.printStackTrace();
                }
            }
        }

         fun write(bytes : ByteArray) {
             try {
                 outputStream.write(bytes);
             } catch (e : IOException) {
                 e.printStackTrace();
             }
         }
    }

     class ClientClass(hostAddress: String, sendReceive: SendReceive) : Thread() {

          var socket : Socket;
          var hostAdd : String;
          var sendReceives = sendReceive

        init {
            hostAdd = hostAddress
            socket = Socket()
        }
         override fun run() {
            try {
                socket.connect(InetSocketAddress(hostAdd, 8888), 500)
                sendReceives = SendReceive(socket)
                sendReceives.run();
            } catch (e : IOException) {
                e.printStackTrace()
            }
        }
    }*/



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConnectionPageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
