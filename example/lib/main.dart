import 'dart:convert';

import 'package:android_pos/models/buy_invoice_product.dart';
import 'package:android_pos/models/invoice_receipt.dart';
import 'package:android_pos/models/pos_resp_p1000_model.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:android_pos/android_pos.dart';
import 'package:image_picker/image_picker.dart';
import 'package:http/http.dart' as http;

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _androidPosPlugin = AndroidPos();
  PosRespModel? _model;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _androidPosPlugin.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child:Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
              Text('Running on: $_platformVersion\n'),
              ElevatedButton(onPressed: ()async=>await _androidPosPlugin.startPaymentTxn("test","11000",(model) {
                setState(() {
                  _model = model;
                });
              }), child: Text("Pay         ")),
                ElevatedButton(onPressed: ()async=>await _androidPosPlugin.checkPaper("test",(model) {
                  setState(() {
                    _model = model;
                  });
                }), child: Text("Check paper ")),
                ElevatedButton(onPressed: ()async=>await _androidPosPlugin.startCamera("test",(model) {
                  setState(() {
                    _model = model;
                  });
                }), child: Text("Start Scanner ")),
                ElevatedButton(onPressed: () async{
                  final ImagePicker _picker = ImagePicker();
                  final XFile? image = await _picker.pickImage(source: ImageSource.gallery);
                  var list = await image?.readAsBytes();
                  String base64Image = base64Encode(list!);
                  await _androidPosPlugin.printBitmap(base64Image, "test");
                }, child: Text("Print Image ")),
                ElevatedButton(onPressed: () async{
                  var bytes = await rootBundle.load('assets/images/coffee_cup_64.png');
                  var logo = base64Encode(bytes.buffer.asUint8List());
                  List<BuyInvoiceProduct> details = [];
                  details.add(BuyInvoiceProduct('1','2',3,5000,'343','3434',"کتاب اول بسیار خوب"));
                  details.add(BuyInvoiceProduct('1','2',1,3000,'343','3434',"کتاب دوم بسیار بد"));
                  var model = InvoiceReceiptParams(tax: '222',transportCost: '4000',totalPayAmount: '18000',invoiceNumber: '123',date: '1401/11/24 8:51',
                      products: details,customer: "فروشگاه کتاب دارسیس",marketer: "خیابان انقلاب بعد اذ آزادی پلاک ۹۸ تلفن ۴۴۵۶۲۷۸۳",logo: logo);
                  await _androidPosPlugin.printInvoice(model);
                }, child: Text("Print Invoice ")),
              SizedBox(height: 25,),
              Text("Message: ${_model?.Message}"),
              Text("status: ${_model?.status}"),
              Text("RRN: ${_model?.RRN}"),
              Text("Date: ${_model?.DateTime}"),
              Text("ResponseCode: ${_model?.ResponseCode}"),
              Text("CardNo: ${_model?.CardNo}"),
              Text("TerminalNo: ${_model?.TerminalNo}"),
          ],)

        ),
      ),
    );
  }
}
