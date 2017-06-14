package in.codingninjas.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView _screen;
    private String display="";
    private String currOperator="";
    DecimalFormat df = new DecimalFormat( "##########.###");
    boolean decimal=false;
    boolean operator=true;
    boolean exception=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _screen=(TextView) findViewById(R.id.textView);
        _screen.setText(display);
    }

    private void updateScreen() {
        _screen.setText(display);
    }

    public void onClickNumber(View v) {
        if(exception) {
            clear();
            exception=false;
        }
        operator=false;
        Button button = (Button)v;
        display+=button.getText().toString();
        updateScreen();
    }

    public void onClickDecimal(View v) {
        if(decimal)
            return;
        Button button = (Button)v;
        display+=button.getText().toString();
        decimal=true;
        updateScreen();
    }

    public void onClickOperator(View v) {
        if(exception)
            clear();
        decimal=false;
        if(display.length()<1)
            return;
        if(operator)
            display = display.substring(0, display.length() - 1);
        if(currOperator!="") {
            onClickEquals(v);
            if(exception) {
                clear();
                return;
            }
        }
        operator=true;
        Button button = (Button)v;
        display+=button.getText().toString();
        currOperator=button.getText().toString();
        updateScreen();
    }

    private double operateArithematic(String a,String b, String op) {
        try {
            switch (op) {
                case "+":
                    return Double.parseDouble(df.format(Double.parseDouble(a) + Double.parseDouble(b)));
                case "-":
                    return Double.parseDouble(df.format(Double.parseDouble(a) - Double.parseDouble(b)));
                case "*":
                    return Double.parseDouble(df.format(Double.parseDouble(a) * Double.parseDouble(b)));
                case "/":
                    if (Double.parseDouble(a) == 0 && Double.parseDouble(b) == 0) {
                        exception = true;
                        return -1;
                    }
                    try {
                        return Double.parseDouble(df.format((Double.parseDouble(a) / Double.parseDouble(b))));
                    } catch (Exception e) {
                        Log.d("Divide", e.getMessage());
                        exception = true;
                    }
                case "%":
                    return Double.parseDouble(df.format((Double.parseDouble(a) % Double.parseDouble(b))));
                default:
                    return -1;
            }
        } catch (Exception e) {
            Log.d("operateArithematc", e.getMessage());
            exception=true;
            return -1;
        }
    }

    public void onClickEquals(View v) {
        if(currOperator=="")
            return;
        decimal=true;
        operator=false;
        String[] operation=display.split(Pattern.quote(currOperator));
        Double result;
        if(operation.length<2)
            return;
        result=operateArithematic(operation[0],operation[1],currOperator);
        currOperator="";
        if(!exception)
            _screen.setText(display+"\n"+Double.toString(result));
        else
            _screen.setText(display+"\n"+"Error");
        display=Double.toString(result);
    }

    private void clear() {
        decimal=false;
        operator=true;
        exception=false;
        display="";
        currOperator="";
    }

    public void onClickClear(View v) {
        clear();
        updateScreen();
    }
}