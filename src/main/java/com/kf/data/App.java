package com.kf.data;

import com.kf.data.bill.parser.CibBill;
import com.kf.data.bill.parser.CiticbankBill;
import com.kf.data.bill.parser.CmbChinaBill;
import com.kf.data.bill.parser.CmbcBill;
import com.kf.data.bill.parser.HkbeaBill;
import com.kf.data.bill.parser.HsbcBill;
import com.kf.data.bill.parser.HxbBill;
import com.kf.data.bill.parser.IcbcBill;

public class App {

	public static void main(String[] args) {

		// new HkbeaBill().spider();
		// new HsbcBill().spider();
		// new CmbChinaBill().spider();
		// new CibBill().spider();
		// new CmbcBill().spider();
		// new CiticbankBill().spider();
		// new HxbBill().spider();
		//
		new IcbcBill().spider();
	}
}
