package com.lymtechcrm.app.asynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

public class GetAsyncProcess extends AsyncTask<String, Void, String> {
	private String server_response = "";
	protected int responseCode = 0;

	@Override
	protected String doInBackground(String... strings) {

		URL url;
		HttpURLConnection urlConnection = null;

		try {
			url = new URL(strings[0]);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			responseCode = urlConnection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				server_response = readStream(urlConnection.getInputStream());
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return server_response;
	}

	// Converting InputStream to String

	private String readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuffer response = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return response.toString();
	}
}