package com.soarclient.libraries.materialyou;

import com.soarclient.libraries.materialyou.dynamiccolor.DynamicScheme;
import com.soarclient.libraries.materialyou.hct.Hct;
import com.soarclient.libraries.materialyou.scheme.SchemeTonalSpot;

public class MaterialYou {

	public static DynamicScheme getDynamicScheme(Hct hct, boolean dark, double contrast) {
		return new SchemeTonalSpot(hct, dark, contrast);
	}
}
