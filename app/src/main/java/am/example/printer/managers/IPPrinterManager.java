package am.example.printer.managers;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import am.example.printer.R;
import am.example.printer.data.PrinterTester;
import am.example.printer.util.ImmUtils;
import am.example.printer.util.StringUtils;
import am.util.printer.PrinterWriter58mm;
import am.util.printer.PrinterWriter80mm;

/**
 * 固定IP打印机
 * Created by Alex on 2016/6/21.
 */
public class IPPrinterManager implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {
    private Activity activity;
    private EditText edtIp;
    private EditText edtPort;
    private int type = PrinterWriter80mm.TYPE_80;
    private PrinterTester tester;

    public IPPrinterManager(Activity activity) {
        this.activity = activity;
        edtIp = (EditText) activity.findViewById(R.id.printer_edt_ip);
        edtPort = (EditText) activity.findViewById(R.id.printer_edt_port);
        ((RadioGroup) activity.findViewById(R.id.printer_rg_type)).setOnCheckedChangeListener(this);
        activity.findViewById(R.id.printer_btn_test_ip).setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.printer_rb_80:
                type = PrinterWriter80mm.TYPE_80;
                break;
            case R.id.printer_rb_58:
                type = PrinterWriter58mm.TYPE_58;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.printer_btn_test_ip:
                testIp();
                break;
        }
    }

    private void testIp() {
        String ip = edtIp.getText().toString().trim();
        if (ip.length() <= 0) {
            Toast.makeText(activity, R.string.printer_edit_toast_1, Toast.LENGTH_SHORT).show();
            edtIp.requestFocus();
            edtIp.requestFocusFromTouch();
            ImmUtils.showImm(activity, edtIp);
            return;
        } else if (!StringUtils.isIp(ip)) {
            Toast.makeText(activity, R.string.printer_edit_toast_2, Toast.LENGTH_SHORT).show();
            edtIp.setText(null);
            edtIp.requestFocus();
            edtIp.requestFocusFromTouch();
            ImmUtils.showImm(activity, edtIp);
            return;
        }
        int port;
        String portStr = edtPort.getText().toString().trim();
        if (portStr.length() <= 0) {
            Toast.makeText(activity, R.string.printer_edit_toast_3, Toast.LENGTH_SHORT).show();
            edtPort.requestFocus();
            edtPort.requestFocusFromTouch();
            ImmUtils.showImm(activity, edtPort);
            return;
        } else {
            try {
                port = Integer.valueOf(portStr);
            } catch (Exception e) {
                port = -1;
            }
            if (port < 0 || port > 65535) {
                Toast.makeText(activity, R.string.printer_edit_toast_4, Toast.LENGTH_SHORT).show();
                edtPort.setText(null);
                edtPort.requestFocus();
                edtPort.requestFocusFromTouch();
                ImmUtils.showImm(activity, edtPort);
                return;
            }
        }
        edtIp.clearFocus();
        edtPort.clearFocus();
        ImmUtils.closeImm(activity);
        if (tester == null) {
            tester = new PrinterTester(activity);
        }
        tester.startTest(ip, port, type,
                ((EditText) activity.findViewById(R.id.printer_edt_code)).getText().toString());
    }
}
