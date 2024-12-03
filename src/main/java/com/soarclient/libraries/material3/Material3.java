package com.soarclient.libraries.material3;

import com.soarclient.libraries.material3.dynamiccolor.DynamicScheme;
import com.soarclient.libraries.material3.hct.Hct;
import com.soarclient.libraries.material3.scheme.SchemeTonalSpot;

public class Material3 {

	public static DynamicScheme getDynamicScheme(Hct hct, boolean dark, double contrast) {
		return new SchemeTonalSpot(hct, dark, contrast);
	}
}
