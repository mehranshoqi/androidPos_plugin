// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'new_invoice_product.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

NewInvoiceProduct _$NewInvoiceProductFromJson(Map<String, dynamic> json) =>
    NewInvoiceProduct(
      json['productId'] as String,
      json['warehouseId'] as String?,
      json['count'] as int,
      json['buyPrice'] as int,
      json['id'] as String?,
      json['invoiceId'] as String?,
      json['warehouseName'] as String?,
    )..name = json['name'] as String?;

Map<String, dynamic> _$NewInvoiceProductToJson(NewInvoiceProduct instance) {
  final val = <String, dynamic>{
    'productId': instance.productId,
    'warehouseId': instance.warehouseId,
    'count': instance.count,
    'buyPrice': instance.buyPrice,
    'id': instance.id,
    'invoiceId': instance.invoiceId,
    'name': instance.name,
  };

  void writeNotNull(String key, dynamic value) {
    if (value != null) {
      val[key] = value;
    }
  }

  writeNotNull('warehouseName', instance.warehouseName);
  return val;
}
