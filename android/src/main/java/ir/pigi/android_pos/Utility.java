package ir.pigi.android_pos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ir.pigi.android_pos.customViews.FontTextView;

public class Utility {
    public static boolean isPosP1000()
    {
        Log.d("model***: ", Build.MODEL);
        return Build.MODEL.equals(Values.PosP1000ModelName);
    }

    public static boolean isPosKS8223()
    {
        Log.d("model***: ", Build.MODEL);
        return Build.MODEL.equals(Values.PosKS8223ModelName);
    }

    public static boolean isPosPaxA80()
    {
        return Build.MODEL.equals(Values.PosA80ModelName);
    }

    public static Bitmap Base64ImgToBmp(String b64Img){
        byte[] decodedString = Base64.decode(b64Img, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Bitmap loadBitmapFromView(View v, int width)
    {

        v.measure(View.MeasureSpec.makeMeasureSpec(width,
                        View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED));
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        //v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.layout(0, 0, 300, 600);
        v.draw(c);
        return b;
    }

    public static View inflateLayout(Activity context, InvoiceReqVM model){
        context.setTheme(R.style.LaunchTheme);
        View mainView = LayoutInflater.from(context).inflate(R.layout.print_main_layout,null);

        ViewGroup vg = (TableLayout) mainView.findViewById(R.id.tblPrintInv);

        //View detailView = inflater2.inflate(R.layout.print_detail_layout,null);

        FontTextView tvStoreName = mainView.findViewById(R.id.tvPrintStoreName);
        FontTextView tvUsername = mainView.findViewById(R.id.tvPrintUsername);
        FontTextView tvDate = mainView.findViewById(R.id.tvPrintInvDate);
        FontTextView tvCashTitle = mainView.findViewById(R.id.tvPrintCounter);
        FontTextView tvAddr = mainView.findViewById(R.id.tvPrintAddr);
        FontTextView tvTotalFee = mainView.findViewById(R.id.tvPrintTotalFee);
        FontTextView tvInvNo = mainView.findViewById(R.id.tvPrintInvNo);
        FontTextView tvTax = mainView.findViewById(R.id.tvPrintTaxFee);
        FontTextView tvDelivery = mainView.findViewById(R.id.tvPrintDeliveryFee);
        ImageView img = mainView.findViewById(R.id.logo);

        tvTax.setText(String.format("%,d", Integer.parseInt(model.tax)));
        tvDelivery.setText(String.format("%,d", Integer.parseInt(model.transportCost)));

        Bitmap logo = Base64ImgToBmp(model.logo);
        img.setImageBitmap(Bitmap.createScaledBitmap(logo, 32, 32, false));

        String cDate = model.date;
        //holder.productInvoiceHeader.tbInvDate.setText(cDate);
        String dateStr = "تاریخ: " +model.date;
        tvDate.setText(dateStr);
        tvStoreName.setText(model.customer);
        //tvCashTitle.setText(LoginInfo.Data.Data.CashTitle);
        //tvUsername.setText(prf.getUsername());
        tvAddr.setText(model.marketer);
        tvInvNo.setText("شماره فاکتور: "  +model.invoiceNumber);
        //FontTextView
        //vg.addView(detailView,vg.getChildCount() - 1);

        int qtyTotal = 0;
        for (InvoiceDetailVM dtl :
                model.products) {
            int dLyt = R.layout.print_detail_layout;
            View detailView = LayoutInflater.from(context).inflate(dLyt,null);
            FontTextView tvProductTitle = detailView.findViewById(R.id.tvPrintProductTitle);
            FontTextView tvQty = detailView.findViewById(R.id.tvPrintQty);
            FontTextView tvFee = detailView.findViewById(R.id.tvPrintFee);

            tvProductTitle.setText(dtl.name);
            tvQty.setText(String.valueOf(dtl.count));
            tvFee.setText(String.format("%,d", dtl.buyPrice));
            vg.addView(detailView,vg.getChildCount() - 3);

        }
        tvTotalFee.setText(String.format("%,d", Integer.parseInt(model.totalPayAmount)));

        return mainView;
    }
}
