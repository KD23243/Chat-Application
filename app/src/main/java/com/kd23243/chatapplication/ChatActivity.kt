package com.kd23243.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendBtn: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    private lateinit var nameHeader: TextView

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)


        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().reference

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        chatRecyclerView = findViewById(R.id.charRecycleView)
        messageBox = findViewById(R.id.messageBox)
        sendBtn = findViewById(R.id.SentButton)
        nameHeader = findViewById(R.id.nameHeader)

        nameHeader.text = name

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapShot in snapshot.children){

                        val message = postSnapShot.getValue(Message::class.java)
                        messageList.add(message!!)

                    }

                    messageAdapter.notifyDataSetChanged()
                    chatRecyclerView.smoothScrollToPosition(messageList.size);
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        sendBtn.setOnClickListener(){

            try {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: java.lang.Exception) {
                Log.d("Keyboard Hiding", "Failed")
            }


            val message = messageBox.text.toString()
            val messageObject = Message(message,senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")

        }


    }
}