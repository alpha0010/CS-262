package edu.calvin.mn29.lab03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TextWatcher
{
    private ImageView m_secretImage;
    private TextView m_invalidMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_secretImage = (ImageView)findViewById(R.id.secret_image);
        m_invalidMsg = (TextView)findViewById(R.id.invalid_msg);

        EditText passwdField = (EditText)findViewById(R.id.passwd_field);
        passwdField.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    /***
     * As the user enters the password, show the image when the password is "password". Otherwise
     * report invalid password.
     *
     * @param s The password field.
     */
    @Override
    public void afterTextChanged(Editable s)
    {
        if (s.toString().equals("password"))
        {
            m_invalidMsg.setVisibility(View.INVISIBLE);
            m_secretImage.setVisibility(View.VISIBLE);
        }
        else
        {
            m_secretImage.setVisibility(View.INVISIBLE);
            m_invalidMsg.setVisibility(View.VISIBLE);
        }
    }
}
