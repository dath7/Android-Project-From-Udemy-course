package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText result;
    private EditText newNum;
    private TextView displayOperation;

    private static final String STATE_PENDING_OPERATION = "PendingOperation";
    private static final String STATE_OPERAND1 = "Operand1";

    // Variables to hold the operands and type of calculation
    private Double operand1 = null;
    private String pendingOperation = "=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (EditText) findViewById(R.id.result);
        newNum = (EditText) findViewById(R.id.newNum);
        displayOperation = (TextView) findViewById(R.id.operator);
        Button button0 = (Button) findViewById(R.id.button0);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button buttonDot = (Button) findViewById(R.id.buttonDot);


        Button buttonDivide = (Button) findViewById(R.id.buttonDivide);
        Button buttonMultiply = (Button) findViewById(R.id.buttonMutiply);
        Button buttonEquals = (Button) findViewById(R.id.buttonEqual);
        Button buttonPlus = (Button) findViewById(R.id.buttonPlus);
        Button buttonMinus = (Button) findViewById(R.id.buttonMinus);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                newNum.append(b.getText().toString());
            }
        };
        button0.setOnClickListener(listener);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
        button8.setOnClickListener(listener);
        button9.setOnClickListener(listener);
        buttonDot.setOnClickListener(listener);

        View.OnClickListener opListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                String op = b.getText().toString();
                String value = newNum.getText().toString();
                // catch NumberFormat error
                try {
                    Double doubleValue = Double.valueOf(value);
                    performOperation(doubleValue);
                } catch (NumberFormatException e) {
                    newNum.setText("");
                }
                pendingOperation = op;
                displayOperation.setText(pendingOperation);
            }
        };

        buttonEquals.setOnClickListener(opListener);
        buttonPlus.setOnClickListener(opListener);
        buttonMinus.setOnClickListener(opListener);
        buttonDivide.setOnClickListener(opListener);
        buttonMultiply.setOnClickListener(opListener);


        Button buttonNeg =  (Button) findViewById(R.id.buttonNegative);
        buttonNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = newNum.getText().toString();
                if (value.length() == 0) {
                    newNum.setText("-");
                }
                else {
                    try {
                        Double doubleValue = Double.valueOf(value);
                        doubleValue *= -1;
                        newNum.setText(doubleValue.toString());
                    }
                    catch (NumberFormatException e) {
                        newNum.setText("");}
                    }
                }

         });
    };



    private void performOperation(Double value) {
        if (operand1 == null) {
            operand1 = value;
        } else {


            switch (pendingOperation) {
                case "=":
                    operand1 = value;
                    break;
                case "+":
                    operand1 += value;
                    break;
                case "-":
                    operand1 -= value;
                    break;
                case "*":
                    operand1 *= value;
                    break;
                case "/":
                    if (value == 0) {
                        operand1 = 0.0;
                    }
                    operand1 /= value;
            }
        }
        result.setText(operand1.toString());
        newNum.setText("");
    };




    // deal with landscape mode
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_PENDING_OPERATION,pendingOperation);
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1,operand1);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION);
       operand1 = savedInstanceState.getDouble(STATE_OPERAND1);
       displayOperation.setText(pendingOperation);
    }
}
