package com.example.andrew.snapchef;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Andrew on 5/2/2017.
 */

public class HttpCallsClass extends AsyncTask<String, Void, String> {
    private Bitmap imageBM;
    private File img;

    public HttpCallsClass(Bitmap b, File f) {
        imageBM = b;
        img = f;
    }

    @Override
    protected String doInBackground(String... params) {

        /*
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBM.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            ByteArrayBody bab = new ByteArrayBody(byteArray, "Image.jpg"); //may have to put an @ sign in front
            entity.addPart("Image.jpg", bab);
            URL url = new URL("http://api.cloudsightapi.com/image_requests");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Connection", "Keep-Alive");
            OutputStream os = conn.getOutputStream();



        } catch(Exception g){

        }
        */



        String response = "";
        try {
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize= 1024 * 1024;
            int serverResponseCode = 0;

            System.setProperty("http.keepAlive", "false");

            /////////////////////////////////////////////
            ////UPLOADING PICTURE AND DATA

            //try multipart

            //download picture from the source
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBM.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // open a URL connection to the Servlet
            ByteArrayInputStream fileInputStream = new ByteArrayInputStream(byteArray);
            URL url = new URL("http://api.cloudsightapi.com/image_requests");

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs

            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Encoding", "");
            //conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("Authorization", "CloudSight 5l5nJ2_Uc4SHC37eeF1uow");
            conn.setRequestProperty("image_request[image]", "@Image.jpg");
            conn.setRequestProperty("image_request[locale]", "en-US");

            dos = new DataOutputStream(conn.getOutputStream());

            //third parameter - password
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"image_request[locale]\"" + lineEnd + lineEnd
                    + "en-US" + lineEnd);

            //forth parameter - filename
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"image_request[image]\";filename=\""
                    + "@Image.jpg" + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            //dos.write(byteArray);

            //byte[] pixels = new byte[imageBM.getWidth() * imageBM.getHeight()];
            //for(int i = 0; i < imageBM.getWidth(); i++){
            //  for(int j = 0; j < imageBM.getHeight(); j++){
            //pixels[i + j] = (byte)((imageBM.getPixel(i, j)) >> 7);
            //}
            //}
            Log.d("Image: ", imageBM.toString());

            //dos.write(pixels);




            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }




            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();

            String serverResponseMessage = conn.getResponseMessage();

            //System.out.println(conn.getRequestMethod());
            //System.out.println(conn.getHeaderFields());
            //System.out.println(conn.getRequestProperties());

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
            conn.disconnect();
            return serverResponseMessage;



            /*
            final String boundary = "==================";
            final String twoHyphens = "--";
            final String crlf = "\r\n";
            final String mimeType = "image/jpeg";
            final int IMAGE_QUALITY = 100;

            URL url = null;
            HttpURLConnection urlConnection = null;
            DataOutputStream dos;
            response = null;

            try {
                url = new URL(params[0] + "/image_requests");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);     ///
                urlConnection.setRequestProperty("Authorization", "CloudSight 5l5nJ2_Uc4SHC37eeF1uow");
                urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                urlConnection.setRequestProperty("image_request[image]","@Image.jpg"); //snagged from question about credits
                //urlConnection.setRequestProperty("Content-Type", "image/jpeg");
                urlConnection.setRequestMethod("POST");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBM.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, stream);
                byte[] byteArray = stream.toByteArray();

                //try printing this info out to look for the issue
                //try waiting longer for response
                dos = new DataOutputStream(urlConnection.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + crlf);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + "@Image.jpg" + "\"" + crlf);
                dos.writeBytes("Content-Type: " + mimeType + crlf);
                dos.writeBytes(crlf);
                //dos.writeBytes("image_request[image]="); //tried this
                dos.write(byteArray);
                dos.writeBytes(crlf);
                dos.writeBytes(twoHyphens + boundary + twoHyphens);
                dos.flush();
                dos.close();

                int responseC = urlConnection.getResponseCode();

                //String line = null;
                //InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                //BufferedReader reader = new BufferedReader(isr);
                //StringBuilder sb = new StringBuilder();
                //while ((line = reader.readLine()) != null) {
                    //sb.append(line).append("\n");
                //}
                //response = sb.toString();

                //isr.close();
                //reader.close();
                response += " codetoken " + responseC;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                response = "Malformed URL";

            } catch (IOException e) {
                e.printStackTrace();
                response = "IO Exception";
            }

            Log.d("RESPONSE: ", response);
            return response;
            */



        } catch (Exception d) {
            Log.d("FUCKED", d.toString());
        }
        return response;


    }

}
