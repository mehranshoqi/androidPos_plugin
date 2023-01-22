package ir.pigi.android_pos;

import java.util.List;

public class InvoiceReqVM {
    public String tax, transportCost, totalPayAmount, invoiceNumber, date;
    public List<InvoiceDetailVM> products;
    public String customer, description, marketer,logo;
}
