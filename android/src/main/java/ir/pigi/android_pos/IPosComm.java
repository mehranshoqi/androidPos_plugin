package ir.pigi.android_pos;

import android.app.Activity;
import android.content.Context;

public interface IPosComm {

    void checkPaper(Context context,String storeName);

    void startCamera(Context context,Activity activity, String storeName);

    void startPayTxn(Context context, Activity activity, String storeName, String amount);

    void registerPayCallback(Context context,IPosRespCallback callback);

    void print(Context context,Activity activity,String b64Img,String storeName);

    void printInvoice(Context context,Activity activity,InvoiceReqVM model);

    void dispose(Context context);
}
