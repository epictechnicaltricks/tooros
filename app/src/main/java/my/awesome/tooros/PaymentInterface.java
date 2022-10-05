package my.awesome.tooros;

import android.webkit.JavascriptInterface;

public interface PaymentInterface {

    @JavascriptInterface
    public void success(String data);

    @JavascriptInterface
    public void error(String data);

}
