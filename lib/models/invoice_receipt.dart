import 'buy_invoice_product.dart';

class InvoiceReceiptParams{
  final String tax, transportCost, totalPayAmount, invoiceNumber, date;
  final List<BuyInvoiceProduct> products;
  final String? customer, description, marketer,logo;

  InvoiceReceiptParams({
    required this.tax,
    required this.transportCost,
    required this.totalPayAmount,
    required this.invoiceNumber,
    required this.date,
    required this.products,
    this.customer,
    this.description,
    this.marketer,
    this.logo,
  });

  Map<String, dynamic> toJson() =>  {
    'tax': tax,
    'transportCost': transportCost,
    'totalPayAmount': totalPayAmount,
    'invoiceNumber': invoiceNumber,
    'invoiceNumber': invoiceNumber,
    'date': date,
    'products':products,
    'customer': customer,
    'description': description,
    'marketer': marketer,
    'logo':logo
  };
}
