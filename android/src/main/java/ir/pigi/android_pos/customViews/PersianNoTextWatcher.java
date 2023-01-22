package ir.pigi.android_pos.customViews;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;


public class PersianNoTextWatcher implements TextWatcher {

    private EditText phoneTxt;
    private Activity caller;
    private int threshold;
    private boolean isPersianDate;
    private boolean thousandSep;
    private int sdk = android.os.Build.VERSION.SDK_INT;
    TextChangedListener _listener;
    public PersianNoTextWatcher(EditText phonetxt, int thresh, boolean isPersianDate,boolean thousandSep, TextChangedListener listener){
        this.phoneTxt = phonetxt;
        this.phoneTxt.setOnKeyListener((v, keyCode, event) -> {isBackspace = keyCode == KeyEvent.KEYCODE_DEL?true:false;return false;});

        _listener = listener;
        //this.caller = caler;
        this.threshold = thresh;
        this.isPersianDate = isPersianDate;
        this.thousandSep = thousandSep;
    }

    String lastInput="";
    boolean isBackspace =false;
    int cPosition = 0;
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        lastInput = charSequence.toString();

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        /*if (et_change) {
            et_change = false;
            return;
        }
        et_change = true;


        String s = toPersianNumber(charSequence.toString());
        phoneTxt.setText(s);
        phoneTxt.setSelection(s.length());*/

        cPosition = phoneTxt.getSelectionStart();
    }

    /*@RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void afterTextChanged(Editable s) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("ar","EG"));
        if(s.toString().matches("\\d+(?:\\.\\d+)?")){
        String res= nf.format(Long.parseLong(s.toString()));
            phoneTxt.removeTextChangedListener(this);
            phoneTxt.setText(res);
            phoneTxt.addTextChangedListener(this);
            phoneTxt.setSelection(res.length());
        }
    }*/

     @Override
     public void afterTextChanged(Editable editable) {

         // Remove spacing char

         String txt = editable.toString();
         /*editable.clear();
         editable.insert(0, txt.replaceAll("\\D","").replaceAll("(\\d{4}(?!$))","$1-"));
         txt = editable.toString();*/
         boolean txtLengthChanged = false;
         int txtLength;
         try{
             phoneTxt.removeTextChangedListener(this);

             if(!isBackspace && isPersianDate)
                 txt = formatPersianDateStr(txt);
             if(thousandSep && !isPersianDate) {
                 txtLength = txt.length();
                 //if(thousandSep && !isPersianDate && txt!= null && !txt.equals(""))
                 txt = String.format("%,d", Long.parseLong(removeComma(txt)));
                 if(txtLength != txt.length())
                     txtLengthChanged = true;
             }

             txt = toPersianNumbers(txt);
             /*if(txt.length() == 1 && lastInput.length()<txt.length())
                phoneTxt.setText(lastInput + txt);
            else*/
            phoneTxt.setText(txt);
            if(_listener!= null)
                _listener.OnTextChanged(txt);
            phoneTxt.addTextChangedListener(this);
        }
        catch (Exception ex){
            phoneTxt.addTextChangedListener(this);
        }
        String finalTxt = txt;
        /*phoneTxt.postDelayed(new Runnable() {
            @Override
            public void run() {
                phoneTxt.setSelection(phoneTxt.length());
            }
        },1000);*/
         if(isBackspace && cPosition == 0) {
             phoneTxt.setSelection(txt.length());
             return;
         }
        if(!isBackspace)
        phoneTxt.setSelection(txt.length());
        else if(cPosition <= txt.length())
            phoneTxt.setSelection(cPosition);
        else if(txtLengthChanged && cPosition!= 0)
            phoneTxt.setSelection(cPosition - 1);
        /*if(isBackspace && thousandSep)
        {
            phoneTxt.addTextChangedListener(this);
            finalTxt = txt;
            phoneTxt.setText("");
            phoneTxt.addTextChangedListener(this);
            isBackspace = false;
            phoneTxt.setText(finalTxt);
        }*/

        if(editable.length() == threshold){
            //Utils.hideSoftKeyboard(caller);
        }
    }
    boolean et_change;

    public String toPersianNumbers(String a){
        String[] pNum =new String[]{"۰","۱","۲","۳","۴","۵","۶","۷","۸","۹" };
        a=a.replace("0",pNum[0]);
        a=a.replace("1",pNum[1]);
        a=a.replace("2",pNum[2]);
        a=a.replace("3",pNum[3]);
        a=a.replace("4",pNum[4]);
        a=a.replace("5",pNum[5]);
        a=a.replace("6",pNum[6]);
        a=a.replace("7",pNum[7]);
        a=a.replace("8",pNum[8]);
        a=a.replace("9",pNum[9]);
        return a;
    }
    public static String formatPersianDateStr(String pDate)
    {
        String[] segments = pDate.split("/");
        if(segments.length != 3)
            return  pDate;

        String year = segments[0];
        String month = String.format("%02d",Short.parseShort(segments[1]));
        String day = String.format("%02d", Short.parseShort(segments[2]));
        return  year+"/"+month+"/"+day;
    }

    private static String[] persianNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};

    public static String toPersianNumber(String text) {
        if (text.isEmpty())
            return "";
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            } else if (c == '٫') {
                out += '،';
            } else {
                out += c;
            }
        }
        return out;
    }

    private String removeComma(CharSequence original)
    {
        if(original!= null)
            return original.toString().replaceAll(",","");
        return null;
    }

}
