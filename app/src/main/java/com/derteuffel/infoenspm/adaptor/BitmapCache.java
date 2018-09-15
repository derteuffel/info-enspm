package com.derteuffel.infoenspm.adaptor;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.util.LruCache;

public class BitmapCache extends android.support.v4.util.LruCache<String, Bitmap> implements ImageLoader.ImageCache {
    /**
     * Constructor for LruCache.
     *
     * @param size The maximum size of the cache, the units must match the units used in {@link
     *             #sizeOf(Object, Object)}.
     */
    public BitmapCache(int size) {
        super(size);
    }

    public BitmapCache() {
        this(getDefaultCacheSize());
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
    put(url,bitmap);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes()*value.getHeight()/1024;
    }

    public static int getDefaultCacheSize(){
        final int maxMemory=(int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize=maxMemory/8;
        return cacheSize;
    }
}
