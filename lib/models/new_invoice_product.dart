import 'package:equatable/equatable.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'new_invoice_product.g.dart';

@JsonSerializable(explicitToJson: true)
// ignore: must_be_immutable
class NewInvoiceProduct extends Equatable {
  String productId;
  String? warehouseId;
  int count, buyPrice;

  String? id, invoiceId, name;

  @JsonKey(ignore: true)
  int? totalPrice;

  @JsonKey(ignore: true)
  int? totalDiscount;

  @JsonKey(ignore: true)
  int? totalPriceWithDiscount;

  @JsonKey(includeIfNull: false)
  String? warehouseName;

  NewInvoiceProduct(
    this.productId,
    this.warehouseId,
    this.count,
    this.buyPrice,
    this.id,
    this.invoiceId,
    this.warehouseName,
  );

  Map<String, dynamic> toJson() => _$NewInvoiceProductToJson(this);

  factory NewInvoiceProduct.fromJson(Map<String, dynamic> data) =>
      _$NewInvoiceProductFromJson(data);

  @override
  List<Object?> get props => [productId, buyPrice, count, id, totalPrice];
}
