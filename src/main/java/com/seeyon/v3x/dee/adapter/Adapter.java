package com.seeyon.v3x.dee.adapter;

import com.seeyon.v3x.dee.Document;
import com.seeyon.v3x.dee.TransformException;

/**
 * @author zhangfb
 */
public interface Adapter {
    Document execute(Document document) throws TransformException;
}
