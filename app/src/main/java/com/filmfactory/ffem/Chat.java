package com.filmfactory.ffem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.filmfactory.ffem.pojo.ChatMessage;
import com.filmfactory.ffem.pojo.Employee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Chat extends AppCompatActivity {

    DatabaseReference chatRef, messageRef, chatMessageRef;
    String ref1,ref2, user1,user1Name,user2,user2Name;

    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        user1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user2 = getIntent().getStringExtra("chatWith");
        user2Name = getIntent().getStringExtra("chatWithName");

        getSupportActionBar().setTitle(user2Name);

        ref1 = user1 + "_" +user2;
        ref2 = user2 + "_" +user1;

        chatRef = FirebaseDatabase.getInstance().getReference("Chat");
        messageRef = FirebaseDatabase.getInstance().getReference("Messages");
        chatMessageRef = FirebaseDatabase.getInstance().getReference("ChatMessage");

        sharedPreferences = getSharedPreferences("userRole",MODE_PRIVATE);
        user1Name = sharedPreferences.getString("name","null");

        init();
    }

    private void init(){
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageArea.getText().toString().trim();

                if(!messageText.isEmpty()){
                    messageArea.setText("");

                    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
                    Calendar calobj = Calendar.getInstance();

                    ChatMessage chatMessage = new ChatMessage(messageText,user1, df.format(calobj.getTime()));
                    String messageKey = chatMessageRef.push().getKey();
                    chatMessageRef.child(messageKey).setValue(chatMessage);


                    //Message references
                    chatRef.child(user1).child("chatList").child(ref1).child("chatWith").setValue(user2Name);
                    chatRef.child(user1).child("chatList").child(ref1).child("chatWithUID").setValue(user2);
                    chatRef.child(user1).child("chatList").child(ref1).child("date").setValue(chatMessage.getMessageTime());
                    chatRef.child(user1).child("chatList").child(ref1).child("lastMessage").setValue(chatMessage.getMessageText());
                    chatRef.child(user1).child("chatList").child(ref1).child("chatMessages").child(messageKey).setValue(true);

                    chatRef.child(user2).child("chatList").child(ref2).child("chatWith").setValue(user1Name);
                    chatRef.child(user2).child("chatList").child(ref2).child("chatWithUID").setValue(user1);
                    chatRef.child(user2).child("chatList").child(ref2).child("lastMessage").setValue(chatMessage.getMessageText());
                    chatRef.child(user2).child("chatList").child(ref2).child("date").setValue(chatMessage.getMessageTime());
                    chatRef.child(user2).child("chatList").child(ref2).child("chatMessages").child(messageKey).setValue(true);

                }
            }
        });

        chatRef.child(user1).child("chatList").child(ref1).child("chatMessages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();

                chatMessageRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);

                        if(user1.equalsIgnoreCase(chatMessage.getMessageUser())){
                            addMessageBox(chatMessage.getMessageText(),chatMessage.getMessageTime(),2);
                        }
                        else
                            addMessageBox(chatMessage.getMessageText(),chatMessage.getMessageTime(),1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addMessageBox(String message, String date,int type){
        TextView textView = new TextView(Chat.this);
        textView.setTextSize(18);
        textView.setPadding(20,20,20,20);

        TextView dateView = new TextView(Chat.this);
        dateView.setTextSize(12);
        dateView.setText(date);


        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(0,25,0,0);
        lp2.weight = 1.0f;

        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.setMargins(0,0,0,0);
        lp3.weight = 1.0f;


        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            lp3.gravity = Gravity.LEFT;
            textView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            textView.setText(message);
            textView.setTextColor(Color.WHITE);


        }
        else{
            lp2.gravity = Gravity.RIGHT;
            lp3.gravity = Gravity.RIGHT;
            textView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            textView.setTextColor(Color.BLACK);
            textView.setText("You:\n"+message);
        }
        textView.setLayoutParams(lp2);
        dateView.setLayoutParams(lp3);
        layout.addView(textView);
        layout.addView(dateView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    /*
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, EmployeeActivity.class));
        finish();
    }*/
}
