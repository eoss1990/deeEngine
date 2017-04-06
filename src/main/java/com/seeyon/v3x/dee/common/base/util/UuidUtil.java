package com.seeyon.v3x.dee.common.base.util;

import java.util.UUID;

public class UuidUtil {
	public static String uuid(){
		return String.valueOf(UUID.randomUUID().getMostSignificantBits());
	}

}
