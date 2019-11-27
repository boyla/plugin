package top.wifistar.imagelib;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.Keep;

/**
 * Created by boyla on 2019/9/8.
 */
@Keep
public class UnsplashResult implements Serializable {
    List<UnsplashData> list;
}
