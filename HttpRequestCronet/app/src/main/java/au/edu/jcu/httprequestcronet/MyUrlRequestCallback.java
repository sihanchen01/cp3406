//package au.edu.jcu.httprequestcronet;
//
//import android.util.Log;
//
//import org.chromium.net.CronetException;
//import org.chromium.net.UrlRequest;
//import org.chromium.net.UrlResponseInfo;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.nio.ByteBuffer;
//
//public class MyUrlRequestCallback extends UrlRequest.Callback {
//        private static final String TAG = "MyUrlRequestCallback";
//
////        public OnFinishRequest <JSONObject> delegate;
//        public String responseBody;
//
////        public MyUrlRequestCallback(OnFinishRequest<JSONObject> onFinishRequest) {
////            delegate = onFinishRequest;
////        }
//
//        @Override
//        public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String
//        newLocationUrl) throws Exception {
//            Log.i(TAG, "onRedirectReceived method called.");
//            request.followRedirect();
//        }
//
//        @Override
//        public void onResponseStarted(UrlRequest request, UrlResponseInfo info) throws Exception {
//            Log.i(TAG, "onResponseStarted method called.");
//            // You should call the request.read() method before the request can be
//            // further processed. The following instruction provides a ByteBuffer object
//            // with a capacity of 102400 bytes for the read() method. The same buffer
//            // with data is passed to the onReadCompleted() method.
//            request.read(ByteBuffer.allocateDirect(102400));
//        }
//
//        @Override
//        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer
//        byteBuffer) throws Exception {
//            Log.i(TAG, "onReadCompleted method called.");
//            // You should keep reading the request until there's no more data.
//            request.read(byteBuffer);
//
////            byte[] bytes;
////            if(byteBuffer.hasArray()) {
////                bytes = byteBuffer.array();
////            } else {
////                bytes = new byte[byteBuffer.remaining()];
////                byteBuffer.get(bytes);
////            }
////
////            String responseBodyString = new String(bytes);
////
////            //Properly format the response String
////            responseBodyString = responseBodyString.trim().replaceAll("
// (\r\n|\n\r|\r|\n|\r0|\n0)", "");
////            if (responseBodyString.endsWith("0")) {
////                responseBodyString = responseBodyString.substring(0, responseBodyString
// .length()-1);
////            }
////
////            responseBody = responseBodyString;
////
////            JSONObject results = new JSONObject();
////            try {
////                results.put("body", responseBodyString);
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////
////            delegate.onFinishRequest(results);
//        }
//
//        @Override
//        public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
//            Log.i(TAG, "onSucceeded method called.");
//        }
//
//        @Override
//        public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
//            Log.i(TAG, "onFailed method called.");
//        }
//
////        public interface OnFinishRequest<JSONObject> {
////            public void onFinishRequest(JSONObject result);
////       }
//    }
//
