package edu.calvin.mn29.homework1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    // Inputs.
    private EditText m_val1_entry;
    private EditText m_val2_entry;
    private Spinner m_operatorSel;

    // Saved state.
    private SharedPreferences m_state;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_state = getSharedPreferences("calculator", MODE_PRIVATE);

        m_val1_entry = (EditText)findViewById(R.id.val1_entry);
        m_val2_entry = (EditText)findViewById(R.id.val2_entry);
        m_operatorSel = (Spinner)findViewById(R.id.operator_sel);
        final TextView outputTxt = (TextView)findViewById(R.id.output_txt);

        Button calcBtn = (Button)findViewById(R.id.calc_btn);
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Parse inputs for integral values.
                long val1 = 0;
                try
                {
                    val1 = Long.parseLong(m_val1_entry.getText().toString());
                }
                catch (NumberFormatException e)
                {
                    m_val1_entry.setText("");
                }

                long val2 = 0;
                try
                {
                    val2 = Long.parseLong(m_val2_entry.getText().toString());
                }
                catch (NumberFormatException e)
                {
                    m_val2_entry.setText("");
                }

                // Compute the selected operation.
                String display = "";
                switch (m_operatorSel.getSelectedItemPosition())
                {
                    default:
                    case 0:
                        display += val1 + val2;
                        break;

                    case 1:
                        display += val1 - val2;
                        break;

                    case 2:
                        display += val1 * val2;
                        break;

                    case 3:
                        try
                        {
                            display += val1 / (double)val2;
                        }
                        catch (ArithmeticException e)
                        {
                            display = "NaN";
                        }
                        break;
                }

                outputTxt.setText(display);
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences.Editor cfg = m_state.edit();
        cfg.putString("val1", m_val1_entry.getText().toString());
        cfg.putString("val2", m_val2_entry.getText().toString());
        cfg.putInt("operatorID", m_operatorSel.getSelectedItemPosition());
        cfg.apply();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        m_val1_entry.setText(m_state.getString("val1", ""));
        m_val2_entry.setText(m_state.getString("val2", ""));
        m_operatorSel.setSelection(m_state.getInt("operatorID", 0));
    }
}
