package ir.pigi.android_pos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;

import com.google.gson.Gson;
import com.kishcore.sdk.hybrid.api.HostApp;
import com.kishcore.sdk.hybrid.api.SDKManager;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** AndroidPosPlugin */
public class AndroidPosPlugin implements FlutterPlugin, MethodCallHandler ,ActivityAware, PluginRegistry.ActivityResultListener{
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  Context context;
  PosCommFactory factory;
  Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "android_pos");
    channel.setMethodCallHandler(this);

    context = flutterPluginBinding.getApplicationContext();
    factory = new PosCommFactory();
    IPosComm pos = factory.getPosComm();
    pos.registerPayCallback(context,res->{
      Gson gson = new Gson();
      String json = gson.toJson(res);
      if(res.IsPaperCheck){
        res.IsPaperCheck = false;
        channel.invokeMethod("paperCheckResult", json);
      }
      else if(res.IsCameraEvent){
        res.IsCameraEvent = false;
        channel.invokeMethod("cameraResult", json);
      }
      else
      channel.invokeMethod("paymentResult", json);
    });
//    SDKManager.purchase(context, HostApp.UNKNOWN,1200,1,0,false,null,null,null,());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    IPosComm pos = factory.getPosComm();
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }
    else if(call.method.equals("startPaymentTxn")){
      String storeName = call.argument("storeName");
      String amount = call.argument("amount");
      pos.startPayTxn(context,activity,storeName,amount);
    }
    else if(call.method.equals("checkPaper")){
      String storeName = call.argument("storeName");
      pos.checkPaper(context,storeName);
    }
    else if(call.method.equals("startCamera")){
      String storeName = call.argument("storeName");
      pos.startCamera(context,activity,storeName);
    }
    else if(call.method.equals("printBmp")){
      //Log.d("argument:",call.argument);
      String bmp = call.argument("bmp");
      String storeName = call.argument("storeName");

      Log.d("argument:",bmp);

      pos.print(context,activity,bmp,storeName);
     // LayoutInflater.from(context).inflate(R.layout.layout_inv_print,null);
    }
    else if(call.method.equals("printInv")){
      //Log.d("argument:",call.argument);
      String args = call.argument("arg");
      Gson gson = new Gson();
      InvoiceReqVM reqModel = gson.fromJson(args,InvoiceReqVM.class);

      pos.printInvoice(context,activity,reqModel);
      //String storeName = call.argument("storeName");

      Log.d("args:",args);

      //pos.print(context,activity,bmp,storeName);
      // LayoutInflater.from(context).inflate(R.layout.layout_inv_print,null);
    }
    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    IPosComm pos = factory.getPosComm();
    pos.dispose(context);
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
    // TODO: your plugin is now attached to an Activity
    activity = activityPluginBinding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    // TODO: the Activity your plugin was attached to was destroyed to change configuration.
    // This call will be followed by onReattachedToActivityForConfigChanges().
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {
    // TODO: your plugin is now attached to a new Activity after a configuration change.
  }

  @Override
  public void onDetachedFromActivity() {
    // TODO: your plugin is no longer associated with an Activity. Clean up references.
  }

  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {


    if (requestCode == 1002 && data != null) {
      if (resultCode == Activity.RESULT_OK) {

//        String qrCodeData = data.getStringExtra("BARCODE");
//        pendingResult.success(qrCodeData);

      } else {

       // pendingResult.success("");
      }

      return true;
    }

    return false;

  }

}
