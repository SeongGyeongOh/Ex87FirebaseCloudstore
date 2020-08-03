package com.osg.ex87firebasecloudstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
    }

    public void clickSave(View view) {
        //Firestore db에 저장 - Map 방식 Collection을 통으로 저장!
        //저장할 데이터를 Map으로 만들기

        Map<String, Object> user = new HashMap<>(); //키값은 String으로, 넣는 값은 Object 타입으로(모든 자료형-클래스도 가능함!)
        user.put("name", "sam");
        user.put("age", 20);
        user.put("address", "seoul");

        //Firestore db에 저장하기 위해 객체 소환
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        //"users"라는 이름의 Collection(자식노드 같은 개념) 참조
        CollectionReference userRef = firebaseFirestore.collection("users"); //컬렉션 참조객체 - 하위 폴더 생성같은 느낌
        Task task = userRef.add(user);

        task.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void clickLoad(View view) {
        //Firestore db에서 get()메소드를 이용해서 DB값 읽기
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference userRef = firebaseFirestore.collection("users");

        Task<QuerySnapshot> task = userRef.get(); //제너릭의 QuerySnapshot -> 이전의 DataSnapshot과 동일한 의미
        task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot snapshots = task.getResult();
                    //이 users 폴더 안에 문서가 여러개임!
                    for(QueryDocumentSnapshot snapshot : snapshots){
                        Map<String, Object> user = snapshot.getData();
                        String name = user.get("name").toString(); //value 값이 object니까 형변환 또는 toString
                        long age = (long) user.get("age");
                        String address = user.get("address").toString();

                        tv.append(name +"\n"+age+"\n"+address+"\n==========\n");
                    }

                }
            }
        });


    }
}