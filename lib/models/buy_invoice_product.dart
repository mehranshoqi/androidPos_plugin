
// ignore: must_be_immutable
class BuyInvoiceProduct {
  String productId;
  String? warehouseId;
  int count, buyPrice;

  String? id, invoiceId, name;

  int? totalPrice;

  int? totalDiscount;

  int? totalPriceWithDiscount;

  BuyInvoiceProduct(
    this.productId,
    this.warehouseId,
    this.count,
    this.buyPrice,
    this.id,
    this.invoiceId,
      this.name,
  );

  Map<String, dynamic> toJson() =>  {
    'productId': productId,
    'warehouseId': warehouseId,
    'count': count,
    'buyPrice': buyPrice,
    'id': id,
    'invoiceId': invoiceId,
    'name':name
  };
}
