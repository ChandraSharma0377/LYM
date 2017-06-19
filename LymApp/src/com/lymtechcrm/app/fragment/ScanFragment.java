package com.lymtechcrm.app.fragment;

import java.util.ArrayList;

import com.lymtechcrm.app.R;
import com.lymtechcrm.app.asynctask.GetAsyncProcess;
import com.lymtechcrm.app.helper.ShowAlertInformation;
import com.lymtechcrm.app.main.MainActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ScanFragment extends Fragment implements OnClickListener {

	private TextView tv_purchase, tv_waranty, tv_status, tv_name, tv_address, tv_mobile, tv_complaint,
			tv_complaint_status;
	private Button btnpending, btnremark, btnupdate, btnscan, btnedtscan;
	private UpdateTask bat;
	private ProgressDialog progressDialog;
	private Spinner spstatus;
	private String status, callid;
	private EditText edtremark;
	private LinearLayout firstll, secondll;
	private View seprator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.lay_scan, container, false);
		tv_purchase = (TextView) rootView.findViewById(R.id.tv_purchase);
		tv_waranty = (TextView) rootView.findViewById(R.id.tv_waranty);
		tv_status = (TextView) rootView.findViewById(R.id.tv_status);
		tv_name = (TextView) rootView.findViewById(R.id.tv_name);
		tv_address = (TextView) rootView.findViewById(R.id.tv_address);
		tv_mobile = (TextView) rootView.findViewById(R.id.tv_mobile);
		tv_complaint = (TextView) rootView.findViewById(R.id.tv_complaint);
		tv_complaint_status = (TextView) rootView.findViewById(R.id.tv_complaint_status);
		firstll = (LinearLayout) rootView.findViewById(R.id.firstll);
		secondll = (LinearLayout) rootView.findViewById(R.id.secondll);
		seprator = (View) rootView.findViewById(R.id.seprator);
		btnpending = (Button) rootView.findViewById(R.id.btnpending);
		btnremark = (Button) rootView.findViewById(R.id.btnremark);
		btnupdate = (Button) rootView.findViewById(R.id.btnupdate);
		btnscan = (Button) rootView.findViewById(R.id.btnscan);
		btnedtscan = (Button) rootView.findViewById(R.id.btnedtscan);
		spstatus = (Spinner) rootView.findViewById(R.id.spstatus);
		edtremark = (EditText) rootView.findViewById(R.id.edtremark);
		edtremark.setMovementMethod(new ScrollingMovementMethod());
		edtremark.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		btnedtscan.setOnClickListener(this);
		btnpending.setOnClickListener(this);
		btnremark.setOnClickListener(this);
		btnupdate.setOnClickListener(this);
		btnscan.setOnClickListener(this);
		((LinearLayout) rootView.findViewById(R.id.ll_scan)).setVisibility(View.GONE);
		Bundle b = getArguments();
		status = b.getString("status");
		String name = b.getString("name");
		String address = b.getString("address");
		String cellno = b.getString("cellno");
		callid = b.getString("callid");
		String complaintdetails = b.getString("complaintdetails");
		String complaint_status = b.getString("complaint_status");
		tv_complaint.setText(complaintdetails);
		tv_name.setText(name);
		tv_address.setText(address);
		tv_mobile.setText(cellno);
		tv_complaint_status.setText("Status : " + complaint_status);
		ArrayList<String> countryList = new ArrayList<>();
		countryList.add("Select");
		countryList.add("Complete");
		countryList.add("Pending");
		if (MainActivity.getMainScreenActivity().getUSerID().equals("ADMIN")) {
			btnedtscan.setVisibility(View.INVISIBLE);
			btnscan.setVisibility(View.INVISIBLE);
		} else {
			btnedtscan.setVisibility(View.VISIBLE);
			btnscan.setVisibility(View.VISIBLE);
		}
		ArrayAdapter<String> adapter_country = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, countryList);
		spstatus.setAdapter(adapter_country);
		spstatus.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		if (status.equalsIgnoreCase("Complete")) {
			spstatus.setSelection(1);
			spstatus.setEnabled(false);
			btnupdate.setEnabled(false);
		} else
			spstatus.setSelection(2);
		String purchase_text = " Date of Purchase/AMC:  ";

		tv_purchase.setText(Html.fromHtml(purchase_text));
		String warranty_text = " Warranty: ";

		tv_waranty.setText(Html.fromHtml(warranty_text));
		String status_text = " Status: ";

		tv_status.setText(Html.fromHtml(status_text));
		this.secondll.setVisibility(View.VISIBLE);
		this.seprator.setVisibility(View.VISIBLE);
		this.firstll.setVisibility(View.VISIBLE);
		this.tv_purchase.setVisibility(View.GONE);
		this.tv_status.setVisibility(View.GONE);
		this.tv_waranty.setVisibility(View.GONE);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		MainActivity.getMainScreenActivity().hideHomeIcon();
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.btnpending:

			break;
		case R.id.btnedtscan:
			createBarcodeDia(getActivity());
			break;
		case R.id.btnupdate:
			if (spstatus.getSelectedItemPosition() == 0) {
				ShowAlertInformation.showDialog(getActivity(), "Error", "Please select status.");
			} else if (edtremark.getText().toString().trim().equals("")) {
				ShowAlertInformation.showDialog(getActivity(), "Error", "Please enter remark.");
			} else {
				if (!tv_mobile.getText().toString().equals("")) {
					String url = "http://174.143.34.193/SendSMS/SingleSMS.aspx?usr=info@lymtec.com&pass=lymtec&msisdn="
							+ tv_mobile.getText().toString()+ "&msg="
							+ edtremark.getText().toString() + "&sid=LYMTEC&mt=4";
					new UpdateTask().execute(url);
				}
			}
			break;

		case R.id.btnscan:
			String packageString = "com.graftronicscrm.app";
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.setPackage(packageString);
			intent.putExtra("SCAN_MODE", "SCAN_MODE");
			startActivityForResult(intent, 123);
			break;
		default:
			break;
		}

	}

	private class UpdateTask extends GetAsyncProcess {
		private boolean bCancelled = false;

		public UpdateTask() {
			super();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...");
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setOnCancelListener(cancelListener);
		}

		@Override
		protected String doInBackground(String... params) {
			return super.doInBackground(params);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (200 == responseCode) {
				try {
					String status = " Dear " + tv_name.getText().toString()
							+ ",\nGreetings from Lym, Masters of CCTV Cameras.Your complaint No: " + callid
							+ " is resolved.\nplease re-register if any work pending.";

					Toast.makeText(MainActivity.getMainScreenActivity(), status, Toast.LENGTH_LONG).show();
					MainActivity.getMainScreenActivity().getSupportFragmentManager().popBackStack();

					progressDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
					progressDialog.dismiss();
				}
				System.out.println("BarcodeTask result is : " + (result == null ? "" : result));
				progressDialog.dismiss();

				System.out.println("BarcodeTask result is : " + (result == null ? "" : result));

				progressDialog.dismiss();
			} else {
				Log.i("UpdateTask response", result == null ? "" : result);
				ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
				progressDialog.dismiss();
			}

		}

		OnCancelListener cancelListener = new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				bCancelled = true;
				if (null != bat) {
					bat.cancel(true);
					System.out.println("refe" + bat.isCancelled());
					bat = null;
					// activity.getSupportFragmentManager().popBackStack();
				}
			}
		};
	}

	private void createBarcodeDia(Context mContext) {
	}
}