package in.frisc.distroid;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import okhttp3.internal.Util;

/**
 * Created by irshad on 08/03/18.
 */

public class DistroidServer extends NanoHTTPD{

    private static final String TAG = "DISTROIDServer";

    Context mContext;
    public DistroidServer(String address, int port){
        super(address, port);
    }

    public DistroidServer(Context context,  int port) {
        super(null, port);
        mContext = context;
        try {
            start();
            Log.w(TAG, "Server started");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public Response serve(IHTTPSession session) {
        Response res = null;
        Log.w(TAG, "Connection req");
        try {

            String url = session.getUri();
            Log.d(TAG, "request uri: " + url);
            if (TextUtils.isEmpty(url) || url.equals("/") || url.contains("/register"))
                res = createHtmlResponse(session.getHeaders().get("remote-addr"));
//            else if (url.equals("/status"))
//                res = new NanoHTTPD.Response(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "Available");
//            else if (url.equals("/apk"))
//                res = createApkResponse(session.getHeaders().get("http-client-ip"));
//            else if (url.equals("/logo") || url.equals("/favicon.ico"))
//                res = createLogoResponse();
//            else if (url.equals("/files"))
//                res = createFilePathsResponse();
//            else if (url.contains("/file/")) {
//                int index = Integer.parseInt(url.replace("/file/", ""));
//                if (index != -1)
//                    res = createFileResponse(m_filesTobeHosted[index], session.getHeaders().get("http-client-ip"));
//            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
            Log.w(TAG, "IOE: " + ioe.toString());
            res = createErrorResponse(Response.Status.FORBIDDEN, ioe.getMessage());
        } finally {
            if (null == res)
                res = createForbiddenResponse();
        }
        res.addHeader("Accept-Ranges", "bytes");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        Log.w(TAG, session.getHeaders().get("remote-addr"));
        return res;
    }

    private Response createHtmlResponse(String clientIp) {
        StringBuilder answer = new StringBuilder();
        for(int i = 0; i < Utils.connectedIps.size(); i++){
            answer.append(Utils.connectedIps.get(i) + "\n");

        }
        Utils.connectedIps.add(clientIp);
//        try {
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(mContext.getAssets().open("web_talk.html")));
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                answer += line;
//            }
//        } catch (IOException ioe) {
//            Log.e("NanoHTTPD", ioe.toString());
//        }
        return new NanoHTTPD.Response(answer.toString());
    }

    private Response createErrorResponse(Response.Status status, String message) {
        Log.e(TAG, "error while creating response: " + message);
        return new Response(status, NanoHTTPD.MIME_PLAINTEXT, message);
    }

    private Response createForbiddenResponse() {
        return createErrorResponse(Response.Status.FORBIDDEN,
                "FORBIDDEN: Reading file failed.");
    }

}

