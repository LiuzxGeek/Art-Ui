package com.art.uilibrary.pullrefresh;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.art.uilibrary.R;


public class HTML5WebView extends WebView {

    private static final String LOGTAG = "HTML5WebView";
    static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    private Context mContext;
    private MyWebChromeClient mWebChromeClient;
    private View mCustomView;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private FrameLayout mBrowserFrameLayout;
    private boolean isCanBack = true;
    private IOnPickPhotoListener listener;

    public HTML5WebView(Context context) {
        super(context);
        init(context);
    }

    public HTML5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HTML5WebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        Activity a = (Activity) mContext;
        // mLayout = new FrameLayout(context);
        mBrowserFrameLayout = (FrameLayout) LayoutInflater.from(a).inflate(R.layout.h5_screen, null);
        FrameLayout contentView = (FrameLayout) mBrowserFrameLayout.findViewById(R.id.main_content);
        mCustomViewContainer = (FrameLayout) mBrowserFrameLayout.findViewById(R.id.fullscreen_custom_content);
        // mLayout.addView(mBrowserFrameLayout, COVER_SCREEN_PARAMS);
        mWebChromeClient = new MyWebChromeClient();
        setWebChromeClient(mWebChromeClient);
        setWebViewClient(new MyWebViewClient());
        // Configure the webview
        WebSettings s = getSettings();
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 扩大比例的缩放
        s.setUseWideViewPort(false);
        s.setLoadWithOverviewMode(true);
        //s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);
        // enable navigator.geolocation
        s.setGeolocationEnabled(true);
        s.setGeolocationDatabasePath(context.getApplicationContext().getCacheDir().getAbsolutePath() + "/geo/");
        // enable Web Storage: localStorage, sessionStorage
        // 支持h5的localStorage
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setDatabasePath(context.getApplicationContext().getCacheDir().getAbsolutePath() + "/db/");
        // 开启应用程序缓存
        s.setAppCacheEnabled(true);
        // 设置应用缓存的路径
        s.setAppCachePath(context.getApplicationContext().getCacheDir().getAbsolutePath());
        // 设置缓存的模式
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setDefaultTextEncodingName("utf-8");
        contentView.addView(this);
    }

    public FrameLayout getLayout() {
        return mBrowserFrameLayout;
    }

    public boolean inCustomView() {
        return (mCustomView != null);
    }

    public void hideCustomView() {
        mWebChromeClient.onHideCustomView();
    }

    public void disableCanBack() {
        isCanBack = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isCanBack) {
            if ((mCustomView == null) && canGoBack()) {
                goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public WebChromeClient getChromeClient() {
        return mWebChromeClient;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        invalidate();//加入 mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);防止闪烁后必须在测试
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    public void setOnPickPhotoListener(IOnPickPhotoListener listener) {
        this.listener = listener;
    }

    public interface IOnPickPhotoListener {
        void showConfirmDialog(ValueCallback<Uri> uploadMsg);

        void getMultiImage(ValueCallback<Uri[]> uploadMsg);
    }

    private class MyWebChromeClient extends WebChromeClient {
        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            listener.showConfirmDialog(uploadMsg);
            /*
             * 选择文件上传 浏览本地的文件系统 Intent i = new
			 * Intent(Intent.ACTION_GET_CONTENT);
			 * i.addCategory(Intent.CATEGORY_OPENABLE); i.setType("image/*");
			 * .this.startActivityForResult(Intent.createChooser(i,
			 * "File Chooser"), FILECHOOSER_RESULTCODE);
			 */
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            listener.showConfirmDialog(uploadMsg);
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            listener.showConfirmDialog(uploadMsg);
        }

        // For Android 5.0 openFileChooser已经标记过时
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
            listener.getMultiImage(uploadMsg);
            return true;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // Log.i(LOGTAG, "here in on ShowCustomView");
            HTML5WebView.this.setVisibility(View.GONE);

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mCustomViewContainer.addView(view);
            mCustomView = view;
            mCustomViewCallback = callback;
            mCustomViewContainer.setVisibility(View.VISIBLE);
        }

        @Override
        public void onHideCustomView() {

            if (mCustomView == null)
                return;
            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);
            // Remove the custom view from its container.
            mCustomViewContainer.removeView(mCustomView);
            mCustomView = null;
            mCustomViewContainer.setVisibility(View.GONE);
            mCustomViewCallback.onCustomViewHidden();
            HTML5WebView.this.setVisibility(View.VISIBLE);
        }

        @Override
        public Bitmap getDefaultVideoPoster() {
            // Log.i(LOGTAG, "here in on getDefaultVideoPoster");
            if (mDefaultVideoPoster == null) {
                mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(), R.drawable.anim_progress_round);
            }
            return mDefaultVideoPoster;
        }

        @Override
        public View getVideoLoadingProgressView() {
            // Log.i(LOGTAG, "here in on getVideoLoadingPregressView");
            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                mVideoProgressView = inflater.inflate(R.layout.h5_loading, null);
            }
            return mVideoProgressView;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            ((Activity) mContext).setTitle(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            ((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }

        // 处理javascript中的alert
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            // 构建一个Builder来显示网页中的对话框
            Builder builder = new Builder(mContext);
            builder.setTitle("Alert");
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            builder.setCancelable(false);
            builder.create();
            builder.show();
            return true;
        }

        // 处理javascript中的confirm
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            Builder builder = new Builder(mContext);
            builder.setTitle("confirm");
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            builder.setCancelable(false);
            builder.create();
            builder.show();
            return true;
        }

        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota,
                                            WebStorage.QuotaUpdater quotaUpdater) {
            quotaUpdater.updateQuota(estimatedSize * 2);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(LOGTAG, "shouldOverrideUrlLoading: " + url);
            view.loadUrl(url);
            return true;
        }
    }

}