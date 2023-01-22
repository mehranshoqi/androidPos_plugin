package ir.pigi.android_pos.ks8223;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.kishcore.sdk.hybrid.api.GeneralPaymentCallback;
import com.kishcore.sdk.hybrid.api.HostApp;
//import com.kishcore.sdk.hybrid.api.SDKManager;

import com.kishcore.sdk.hybrid.api.SDKManager;
import com.kishcore.sdk.hybrid.api.WaterMarkLanguage;
import com.kishcore.sdk.hybrid.util.AnnounceDialog;
import com.kishcore.sdk.pna.rahyab.api.PaymentCallback;
import com.szzt.sdk.device.Constants;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import ir.pigi.android_pos.IPosComm;
import ir.pigi.android_pos.IPosRespCallback;
import ir.pigi.android_pos.InvoiceReqVM;
import ir.pigi.android_pos.PosResponseVM;
import ir.pigi.android_pos.Utility;

public class PosCommKS8223 implements IPosComm {
    private HostApp hostApp;
    IPosRespCallback _callback;

    @Override
    public void checkPaper(Context context, String storeName) {

    }

    @Override
    public void startCamera(Context context,Activity activity, String storeName) {
        SDKManager.openBarcodeScanner(true, true, objs -> {
            String data = (String) objs[0];
            //com.kishcore.sdk.hybrid.util.Tools.displaySafeDialog(activity, new AnnounceDialog(activity, "SCANNED BARCODE:" + data, null, 0, null), null);
            PosResponseVM vm = new PosResponseVM();
            vm.status = 10003;
            vm.Message = data;
            vm.IsCameraEvent = true;
            _callback.onResult(vm);
        }, data -> {
            int ret = (int) data[0];
            if (ret == Constants.Error.TIMEOUT) {
                com.kishcore.sdk.hybrid.util.Tools.displaySafeDialog(activity,
                        new AnnounceDialog(activity,
                                "زمان اسکن پایان یافت.",
                                null, 0, null), null);
            } else if (ret == Constants.Error.DEVICE_FORCE_CANCLE) {
                //TODO nothing

            } else if (ret == Constants.Error.DEVICE_USED) {
                com.kishcore.sdk.hybrid.util.Tools.displaySafeDialog(activity,
                        new AnnounceDialog(activity,
                                "اسکنر مشغول است. لطفا مجددا تلاش نمایید.",
                                null, 0, null), null);
            } else {
                com.kishcore.sdk.hybrid.util.Tools.displaySafeDialog(activity,
                        new AnnounceDialog(activity,
                                "خطا در اسکن. لطفا مجددا تلاش نمایید.",
                                null, 0, null), null);
            }
        });
    }

    public void startPayTxn(Context context,Activity activity,String storeName, String amount) {
        String merchantIndex = "-1";                        //just for Irankish   can be changed
        boolean has3rdPartyVerify = false;                  //just for FanavaCard can be changed
        //int purchaseType = PurchaseType.NORMAL_CARD.value;  //just for Sepehr     can be changed
Log.d("startPayTxn", "get called");



Intent intent = new Intent("ir.co.pna.pos.view.cart.IAPCActivity");
PNAPurchaseRequestVM req = new PNAPurchaseRequestVM();
req.AndroidPosMessageHeader = "@@PNA@@";
req.ECRType = "1";
req.Amount = amount;
req.TransactionType = "00";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AndroidPosMessageHeader","@@PNA@@");
            jsonObject.put("ECRType","1");
            jsonObject.put("Amount",amount);
            jsonObject.put("TransactionType","00");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }


//        Gson gson = new Gson();
//        String json = gson.toJson(gson);
        Bundle bundle = new Bundle();
        bundle.putString("Data",jsonObject.toString());
        intent.putExtras(bundle);
        activity.startActivityForResult(intent,1002);

//try {
//    SDKManager.purchase(activity,  "0", amount, has3rdPartyVerify, new PaymentCallback() {
//        @Override
//        public void onPaymentInitializationFailed(int status, String statusDescription, String reserveNumber, String maskedPan, String panHash) {
//            //startActivity(ResultActivity.getIntent(MainActivity.this, String.format(Locale.ENGLISH, "آغاز فرایند پرداخت با کد فاکتور %s با خطا مواجه شد. \nکد وضعیت: %d \n" + statusDescription, reserveNumber, status), String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan), String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
//        }
//
//        @Override
//        public void onPaymentSucceed(String terminalNo, String merchantId, String posSerial, String reserveNumber, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
////                startActivity(ResultActivity.getIntent(MainActivity.this, "پرداخت با موفقیت انجام شد.",
////                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
////                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
////                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
////                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
////                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
////                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
//        }
//
//        @Override
//        public void onPaymentFailed(int errorCode, String errorDescription, String terminalNo, String merchantId, String posSerial, String reserveNumber, String traceNumber, String rrn, String ref, String amount, String txnDate, String txnTime, String maskedPan, String panHash) {
////                startActivity(ResultActivity.getIntent(MainActivity.this, "پرداخت با خطا مواجه شد.",
////                        String.format(Locale.ENGLISH, "کد خطا: %d", errorCode),
////                        String.format(Locale.ENGLISH, "شرح خطا: %s", errorDescription),
////                        String.format(Locale.ENGLISH, "کد فاکتور: %s", reserveNumber),
////                        String.format(Locale.ENGLISH, "کد پیگیری: %s", traceNumber),
////                        String.format(Locale.ENGLISH, "شماره مرجع بازیابی: %s", rrn),
////                        String.format(Locale.ENGLISH, "مبلغ تراکنش: %s", amount),
////                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
////                        String.format(Locale.ENGLISH, "هش کارت کاربر:\n %s", panHash)));
//        }
//
//        @Override
//        public void onPaymentCancelled(String reserveNumber, String maskedPan, String panHash) {
////                startActivity(ResultActivity.getIntent(MainActivity.this, String.format(Locale.ENGLISH, "فرایند پرداخت با کد فاکتور %s توسط کاربر لغو شد.", reserveNumber),
////                        String.format(Locale.ENGLISH, "شماره کارت کاربر:\n %s", maskedPan),
////                        String.format(Locale.ENGLISH, "هش کارت کاربر: %s", panHash)));
//
//            PosResponseVM responseVM = new PosResponseVM();
//            responseVM.status = 10002;
//            responseVM.Message = "پرداخت ناموفق";
//            responseVM.CardNo = maskedPan;
//            _callback.onResult(responseVM);
////                vm.RRN = response._RRN;
////                vm.SerialNo = response.SN;
////                vm.DateTime = response.DT;
////                vm.TerminalNo = response.TN;
//        }
//    });
//}catch (Exception ex){
//    Log.e("ex***: ", "startPayTxn: ", ex);
//}

    }

    @Override
    public void registerPayCallback(Context context,IPosRespCallback callback) {
        android.util.Log.d("HostApp***:", "init hostApp");
        _callback = callback;
        hostApp = SDKManager.init(context);


    }

    @Override
    public void print(Context context,Activity activity, String b64Img,String storeName) {
        final Bitmap[] bm = {null};
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                bm[0] = Utility.Base64ImgToBmp(b64Img);
                SDKManager.printBitmap(activity, bm[0], true, 75, WaterMarkLanguage.ENGLISH, true, data -> {
                    //End of print
                });
            }
        }, 100);
    }

    @Override
    public void printInvoice(Context context,Activity activity,InvoiceReqVM model) {
        View view = Utility.inflateLayout(activity,model);
        Bitmap bmp = Utility.loadBitmapFromView(view,300);

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                SDKManager.printBitmap(activity, bmp, true, 75, WaterMarkLanguage.ENGLISH, true, data -> {
                    //End of print
                });
            }
        }, 100);
    }

    @Override
    public void dispose(Context context){
        SDKManager.closePrinter();
    }

}
