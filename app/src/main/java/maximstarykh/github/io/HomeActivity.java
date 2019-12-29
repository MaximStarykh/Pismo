package maximstarykh.github.io;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class HomeActivity  extends AppCompatActivity {

    private static int SIGN_IN_CODE = 1;
    private RelativeLayout activity_home;
    private FirebaseListAdapter<Message> adapter; //адаптація данних з БД в об'єкти
    private EmojiconEditText emojiconEditText;
    private ImageView emojiButton, sendButton;
    private EmojIconActions emojIconActions;
    //private Button backToChat;



    public HomeActivity() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //ініціалізація меню

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //саме меню
        int id = item.getItemId();

        switch(id){
            case R.id.infopage:
                // setContentView(R.layout.info_page);
                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout: { // логаут та виклик авторизації знову
                FirebaseAuth.getInstance().signOut();
                if(FirebaseAuth.getInstance().getCurrentUser() == null) { // юезр не авторизований
                    Intent i = new Intent(HomeActivity.this, InfoActivity.class);
                startActivity(i);} // авторизація юзера
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        activity_home = findViewById(R.id.activity_home);
        sendButton = findViewById(R.id.send_btn);
        emojiButton = findViewById(R.id.emoji_btn);
        emojiconEditText = findViewById(R.id.textField);
        emojIconActions = new EmojIconActions(getApplicationContext(), activity_home, emojiconEditText, emojiButton); //визов клавіатури з емодзі
        emojIconActions.ShowEmojIcon();
        // backToChat = (Button) findViewById(R.id.back_to_chat);





        sendButton.setOnClickListener(new View.OnClickListener() { //обробник подій при натисканні на кнопку
            @Override
            public void onClick(View v) {

                if(emojiconEditText.getText().toString().equals("")) // якщо повідомлення пусте, то нічого не відбудеться
                    return;

                FirebaseDatabase.getInstance().getReference().push().setValue( // підключаємось до БД та добавляємо повідомелення у неї
                        new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                emojiconEditText.getText().toString()
                        )
                );
                emojiconEditText.setText("");
            }
        });



        if(FirebaseAuth.getInstance().getCurrentUser() == null) // юезр не авторизований
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE); // авторизація юзера
        else {
            Snackbar.make(activity_home, "Раді Вас бачити у Pismo!", Snackbar.LENGTH_LONG).show();
            displayAllMessages();
        }
    }



    private void displayAllMessages() {
        ListView listOfMessages =  findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()) { //визиваємо метод класу, файл з розміткую та підключення до БД
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView msg_user, msg_time;
                BubbleTextView msg_text;

                msg_user = v.findViewById(R.id.message_user); //v. - знаходимо потрібні данні всередині вікна з яким працюємо
                msg_time = v.findViewById(R.id.message_time);
                msg_text = v.findViewById(R.id.message_text);

                msg_user.setText(model.getUserName());
                msg_text.setText(model.getTextMessage());
                msg_time.setText(DateFormat.format("dd-mm-yyy HH:mm:ss", model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }



}
